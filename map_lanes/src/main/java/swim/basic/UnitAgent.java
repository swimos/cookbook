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

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;

public class UnitAgent extends AbstractAgent {

  public UnitAgent() {
  }

  @SwimLane("shoppingCart")
  MapLane<String, Integer> shoppingCart = this.<String, Integer>mapLane()
      .didUpdate((key, newValue, oldValue) -> {
        logMessage(key + " count changed to " + newValue + " from " + oldValue);
      })
      .didRemove((key, oldValue) -> {
        logMessage("removed <" + key + "," + oldValue + ">");
      });

  @SwimLane("addItem")
  CommandLane<String> publish = this.<String>commandLane()
      .onCommand(msg -> {
        final int n = this.shoppingCart.getOrDefault(msg, 0) + 1;
        this.shoppingCart.put(msg, n);
      });

  @Override
  public void didStart() {
    System.out.println(nodeUri() + " didStart region");
    close();
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

}
