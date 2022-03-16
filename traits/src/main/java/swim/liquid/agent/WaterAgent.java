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

package swim.liquid.agent;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.ValueLane;

public class WaterAgent extends AbstractAgent {

  // Lane utilized for static traits.
  @SwimLane("sharedInfo")
  ValueLane<String> sharedInfo;

  // Lane utilized for dynamic traits.
  @SwimLane("sharedDynInfo")
  ValueLane<String> sharedDynInfo;

  @Override
  public void didStart() {
    System.out.println(nodeUri() + " didStart");
    pourStatic();
    pourDynamic();
    close();
  }

  @Override
  public void willStop() {
    logMessage("willStop");
  }

  // Fetch value of property belonging to the /liquid/static/water/sparkling uri.
  void pourStatic() {
    final String waterInfo = getProp("waterType").stringValue(null);
    if (waterInfo != null) {
      logMessage("Static Water Property '" + waterInfo + "'");

      // Set Value for the sharedInfo SwimLane which is shared by Liquid,
      // Water and Juice Agent.
      this.sharedInfo.set("Statically shared liquid is '" + waterInfo + "'");
    }
  }

  // Fetch value of the sharedDynInfo lane belonging to the /liquid/dynamic/:id/:id pattern.
  void pourDynamic() {
    if (!this.sharedDynInfo.get().isEmpty()) {
      logMessage("Dynamically shared liquid is '" + this.sharedDynInfo.get() + "'");
    }
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
