// Copyright 2015-2023 Nstream, inc.
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

import swim.api.downlink.MapDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;
import swim.structure.Text;

final class CustomClient {

  private CustomClient() {
  }

  public static void main(String[] args) throws InterruptedException {

    final ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();

    final String hostUri = "warp://localhost:9001";
    final String nodeUriPrefix = "/unit/";

    // Write-only downlink; note keepLinked is false
    final MapDownlink<String, Integer> link = swimClient.downlinkMap()
        .keyForm(Form.forString()).valueForm(Form.forInteger())
        .hostUri(hostUri).nodeUri(nodeUriPrefix + "0").laneUri("shoppingCart")
        .keepLinked(false)
        .open();
    link.put("FromClientLink", 25);

    Thread.sleep(1000);
    // Hereafter we will strictly use commands, so link is safe to close
    link.close();

    final String[] items = {"bat", "cat", "rat"};
    for (int i = 0; i < 50; i++) {
      // randomly add an item from `items` to the shopping carts of /unit/0, /unit/1, /unit/2 via commands
      swimClient.command(hostUri, nodeUriPrefix + (i % 3), "addItem", Text.from(items[(int) (Math.random() * 3)]));
    }

    System.out.println("Will shut down client in 2 seconds");
    Thread.sleep(2000);
    swimClient.stop();
  }

}
