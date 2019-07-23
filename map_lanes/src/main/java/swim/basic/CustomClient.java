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

package swim.basic;

import swim.api.downlink.MapDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;
import swim.structure.Text;

class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();
    final String hostUri = "warp://localhost:9001";
    final String nodeUri = "/unit/foo";
    // Reduce probability of startup race; no need to conflate this example
    // with proper synchronization just yet
    final MapDownlink<String, Integer> link = swimClient.downlinkMap()
        .keyForm(Form.forString()).valueForm(Form.forInteger())
        .hostUri(hostUri).nodeUri(nodeUri).laneUri("shoppingCart")
        .didUpdate((key, newValue, oldValue) -> {
          System.out.println("link watched " + key + " change to " + newValue + " from " + oldValue);
        })
        .open();    
    // Send using either the proxy command lane...
    swimClient.command(hostUri, nodeUri, "addItem", Text.from("FromClientCommand"));
    // ...or a downlink put()
    link.put("FromClientLink", 25);
    Thread.sleep(2000);
    link.remove("FromClientLink");

    System.out.println("Will shut down client in 2 seconds");
    Thread.sleep(2000);
    swimClient.stop();
  }
}
