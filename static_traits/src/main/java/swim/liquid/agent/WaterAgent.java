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
import swim.liquid.LiquidResources;
import swim.structure.Value;

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

  // Fetch values of various property types belonging to the agents.
  void pour() {
    final String pourResources = getProp("pour").stringValue(null);
    if (pourResources != null) {
      final Value pourValue = LiquidResources.loadReconResource(pourResources);
      final Value waterInfo = pourValue.get("info").getSlot("name");
      logMessage("Water Property '" + waterInfo + "'");

      // Set Value for the sharedInfo SwimLane which is shared by Water and
      // Juice Agent
      this.sharedInfo.set("Pouring " + waterInfo);
    }
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
