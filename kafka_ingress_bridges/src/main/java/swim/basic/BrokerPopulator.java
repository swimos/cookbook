// Copyright 2015-2022 SWIM.AI inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package swim.basic;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HttpsURLConnection;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import swim.codec.Utf8;
import swim.json.Json;
import swim.structure.Item;
import swim.structure.Record;
import swim.structure.Text;
import swim.structure.Value;

public class BrokerPopulator {

  private HttpsURLConnection conn;
  private String before;
  private Producer<String, String> producer;

  private BrokerPopulator() {
    final Properties props = new Properties();
    try (InputStream input = BrokerPopulator.class.getResourceAsStream("/producer.properties")) {
      props.load(input);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load producer.properties", e);
    }
    try {
      this.producer = new KafkaProducer<>(props);
    } catch (Exception e) {
      throw new RuntimeException("Failed to start BrokerPopulator.producer", e);
    }
  }

  private void openConn(String urlStr) {
    if (this.conn != null) {
      closeConn();
    }
    try {
      final URL url = new URL(urlStr);
      this.conn = (HttpsURLConnection) url.openConnection();
      this.conn.setDoOutput(true);
      this.conn.setConnectTimeout(5000);
      this.conn.setRequestMethod("GET");
      this.conn.setRequestProperty("User-Agent", "swimos cookbook bot run by [anonymous]");
    } catch (IOException e) {
      closeConn();
      throw new RuntimeException("Failed to open connection", e);
    }
  }

  private void closeConn() {
    if (this.conn != null) {
      this.conn.disconnect();
      this.conn = null;
    }
  }

  private static Value trimEntry(Value big) {
    if (big.length() == 0) {
      return big;
    }
    return Record.create(8)
        .slot("link_id", big.get("link_id").stringValue().split("_")[1])
        .slot("link_permalink", big.get("link_permalink"))
        .slot("link_author", big.get("link_author"))
        .slot("id", big.get("id"))
        .slot("author", big.get("author"))
        .slot("body", big.get("body"))
        .slot("permalink", big.get("permalink").isDistinct() ? Text.from("https://old.reddit.com" + big.get("permalink").stringValue())
            : Value.extant())
        .slot("created_utc", big.get("created_utc"));
  }

  private static Value trimRawResponse(Value raw) {
    if (raw.length() == 0) {
      return raw;
    }
    final Value toFlatten = raw.get("data").get("children");
    if (toFlatten.length() == 0) {
      return toFlatten;
    }
    final Record result = Record.create(toFlatten.length());
    for (Item entry : toFlatten) {
      result.add(trimEntry(entry.get("data")));
    }
    return result;
  }

  private Value fetchComments() {
    try (InputStream is = this.conn.getInputStream()) {
      return trimRawResponse(Utf8.read(is, Json.structureParser().documentParser()));
    } catch (IOException e) {
      System.out.println("Failed to fetch comments. Cause:");
      e.printStackTrace();
    }
    return Value.absent();
  }

  private void populateBrokerImpl() {
    try {
      if (this.before == null) {
        openConn(INITIAL_ENDPOINT);
      } else {
        openConn(String.format(SUBSEQUENT_FORMAT, SUBREDDIT, this.before));
      }
      final Value comments = fetchComments();
      if (comments.length() != 0) {
        System.out.println("Fetched " + comments.length() + " comments. Latest: " + Json.toString(comments.getItem(0)));
        this.before = "t1_" + comments.getItem(0).get("id").stringValue();
        for (Item comment : comments) {
          this.producer.send(new ProducerRecord<>(SUBREDDIT,
              comment.get("link_id").stringValue(), Json.toString(comment)),
              (meta, e) -> {
                if (e != null) {
                  e.printStackTrace();
                }
              });
        }
      } else {
        System.out.println("No new comments found");
      }
    } finally {
      closeConn();
    }
  }

  public static void populateBrokerLoop() {
    INSTANCE.populateBrokerImpl();
  }

  private static final BrokerPopulator INSTANCE = new BrokerPopulator();

  private static final String SUBREDDIT = "AskReddit";
  private static final String INITIAL_ENDPOINT = String.format("https://old.reddit.com/r/%s/comments.json?limit=100", SUBREDDIT);
  private static final String SUBSEQUENT_FORMAT = "https://old.reddit.com/r/%s/comments.json?before=%s&limit=100";

  private static final long INITIAL_DELAY = 1L * 1000;
  private static final long POLL_FREQUENCY = 15L * 1000;

  public static void main(String[] args) {
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(BrokerPopulator::populateBrokerLoop, INITIAL_DELAY, POLL_FREQUENCY, TimeUnit.MILLISECONDS);
  }

}