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

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.structure.Text;

public class UnitAgent extends AbstractAgent {

  @SwimLane("shoppingCart")
  MapLane<String, Integer> shoppingCart =
      this.<String, Integer>mapLane()
          .didUpdate(
              (key, newValue, oldValue) -> {
                logMessage(key + " count changed to " + newValue + " from " + oldValue);
              });

  @SwimLane("addItem")
  CommandLane<String> publish =
      this.<String>commandLane()
          .onCommand(
              msg -> {
                final int n = this.shoppingCart.getOrDefault(msg, 0) + 1;
                this.shoppingCart.put(msg, n);
              });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

  @Override
  public void didStart() {
    command("/listener", "triggerListen", Text.from(nodeUri().toString()));
  }
}
