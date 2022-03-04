// Copyright 2015-2019 SWIM.AI inc.
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

package swim.forex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import swim.api.agent.AgentContext;
import swim.json.Json;
import swim.structure.Item;
import swim.structure.Value;

public final class FreeForexApi {

  private HttpsURLConnection conn;

  private FreeForexApi() {
    openConn();
  }

  private void openConn() {
    if (this.conn != null) {
      closeConn();
    }
    try {
      final URL url = new URL(FREE_FOREX_API_URI);
      this.conn = (HttpsURLConnection) url.openConnection();
      this.conn.setDoOutput(true);
      this.conn.setConnectTimeout(5000);
      this.conn.setRequestMethod("GET");
    } catch (IOException e) {
      closeConn();
      throw new RuntimeException("Failed to open connection");
    }
  }

  private void closeConn() {
    if (this.conn != null) {
      this.conn.disconnect();
      this.conn = null;
    }
  }

  private Value fetchExchangeRates() throws IOException {
    try (InputStreamReader is = new InputStreamReader(this.conn.getInputStream());
         BufferedReader br = new BufferedReader(is)) {
      return Json.parse(br.readLine());
    }
  }

  public static void relayExchangeRates(AgentContext swim) {
    INSTANCE.openConn();
    try {
      final Value response = INSTANCE.fetchExchangeRates();
      for (Item i : response.get("rates")) {
        final String currency = i.key().stringValue().substring(3);
        swim.command("/currency/" + currency, "addEntry", i.toValue());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    INSTANCE.closeConn();
  }

  private static final String STANDARD = "USD";
  private static final String[] NON_STD_CURRENCIES = {"EUR", "JPY", "GBP", "CHF",
      "CAD", "AUD", "NZD", "ZAR"};
  private static final String[] CURRENCY_PAIRS = new String[NON_STD_CURRENCIES.length];

  static {
    for (int i = 0; i < CURRENCY_PAIRS.length; i++) {
      CURRENCY_PAIRS[i] = STANDARD + NON_STD_CURRENCIES[i];
    }
  }

  private static final String FREE_FOREX_API_URI = "https://www.freeforexapi.com/api/live"
      + "?pairs=" + String.join(",", CURRENCY_PAIRS);

  private static final FreeForexApi INSTANCE = new FreeForexApi();

}
