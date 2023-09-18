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

import swim.api.downlink.EventDownlink;
import swim.client.ClientRuntime;
import swim.structure.Num;
import swim.structure.Value;

final class CustomClient {

  private CustomClient() {
  }

  public static void main(String[] args) throws InterruptedException {
    final ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();

    final String hostUri = "warp://localhost:9001";
    final String nodeUri = "/unit/foo";
    // Reduce probability of startup race; no need to conflate this example
    // with proper synchronization just yet
    swimClient.command(hostUri, nodeUri, "WAKEUP", Value.absent());

    final EventDownlink<Value> link = swimClient.downlink()
        .hostUri(hostUri).nodeUri(nodeUri).laneUri("publishValue")
        .onEvent((Value event) -> {
          System.out.println("link received event: " + event);
        })
        .open();
    final Value msg = Num.from(9035768);
    // command() `msg` TO
    // the "publish" lane OF
    // the agent addressable by `/unit/foo` RUNNING ON
    // the plane with hostUri "warp://localhost:9001"    
    swimClient.command(hostUri, nodeUri, "publish", msg);
    Thread.sleep(2000);
    swimClient.stop();
  }

}
