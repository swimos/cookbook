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

  @SwimLane("sharedInfo")
  ValueLane<String> sharedInfo;

  @Override
  public void didStart() {
    System.out.println(nodeUri() + " didStart region");
    pour();
    close();
  }

  @Override
  public void willStop() {
    logMessage("willStop");
  }

  // Fetch value of property belonging to the /water/sparkling uri.
  void pour() {
    final String waterInfo = getProp("waterType").stringValue(null);
    if (waterInfo != null) {
      logMessage("Water Property '" + waterInfo + "'");

      // Set Value for the sharedInfo SwimLane which is shared by Liquid,
      // Water and Juice Agent.
      this.sharedInfo.set("Shared liquid is " + waterInfo);
    }
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
