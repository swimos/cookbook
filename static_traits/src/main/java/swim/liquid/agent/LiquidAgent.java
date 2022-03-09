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

public class LiquidAgent extends AbstractAgent {

  @SwimLane("sharedWaterInfo")
  ValueLane<String> sharedWaterInfo;

  @SwimLane("sharedJuiceInfo")
  ValueLane<String> sharedJuiceInfo;

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

  // Fetch info shared with other agents.
  void pour() {
    // Fetching shared info from WaterAgent
    final String waterMsg = this.sharedWaterInfo.get();
    // Fetching shared info from JuiceAgent
    final String juiceMsg = this.sharedJuiceInfo.get();
    logMessage(waterMsg + juiceMsg);
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
