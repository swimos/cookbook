package swim.basic;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import swim.api.agent.AbstractAgent;
import swim.concurrent.AbstractTask;
import swim.concurrent.TaskRef;
import swim.concurrent.TimerRef;
import swim.json.Json;
import swim.structure.Value;

public class KafkaConsumerAgent extends AbstractAgent {

  protected TimerRef timer;
  protected TaskRef poll;

  protected void initPoll() {
    if (this.consumer == null) {
      warn(nodeUri() + ": could not issue poll due to uninitialized KafkaConsumer");
      return;
    }
    this.poll = asyncStage().task(new AbstractTask() {

      @Override
      public void runTask() {
        taskDef();
      }

      @Override
      public boolean taskWillBlock() {
        return true;
      }
    });
  }

  protected void taskDef() {
    if (this.consumer == null) {
      return;
    }
    final long target = System.currentTimeMillis() + 5000L;
    final ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(2000));
    for (ConsumerRecord<String, String> record : records) {
      command("/post/" + record.key(), "addComment", Json.parse(record.value()));
    }
    this.timer.reschedule(Math.max(target - System.currentTimeMillis(), 0L));
  }

  private KafkaConsumer<String, String> consumer;

  private void stopConsumer() {
    if (this.consumer != null) {
      this.consumer.close();
    }
    this.consumer = null;
  }

  private Properties propsFromResource(String path) {
    final Properties prop = new Properties();
    try (InputStream stream = BasicPlane.class.getResourceAsStream(path)) {
      prop.load(stream);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load " + path, e);
    }
    return prop;
  }

  private void startKafkaConsumerFromConf() {
    final Value ingressConfVal = getProp("ingressConf");
    if (!ingressConfVal.isDistinct()) {
      warn(nodeUri() + ": no ingressConf found");
      return;
    }
    final String kafkaPropsPath = ingressConfVal.get("kafkaProps").stringValue(null);
    if (kafkaPropsPath == null) {
      warn(nodeUri() + ": no kafkaProps found");
      return;
    }
    final Properties props = propsFromResource(kafkaPropsPath);
    this.consumer = new KafkaConsumer<>(props);
    final String topic = ingressConfVal.get("topic").stringValue("WhatsThisBird");
    this.consumer.subscribe(Collections.singletonList(topic));
  }

  protected void abortPoll() {
    if (this.poll != null) {
      this.poll.cancel();
      this.poll = null;
    }
  }

  protected void scheduleTimer() {
    abortPoll();
    this.timer = setTimer(1000L, () -> {
      if (this.poll == null) {
        initPoll();
      }
      this.poll.cue();
    });
  }

  @Override
  public void willStop() {
    abortPoll();
  }

  @Override
  public void didStart() {
    stopConsumer();
    startKafkaConsumerFromConf();
    scheduleTimer();
  }
}
