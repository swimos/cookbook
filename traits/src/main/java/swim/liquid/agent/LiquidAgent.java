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
import swim.structure.Form;
import swim.structure.Value;

public class LiquidAgent extends AbstractAgent {

  // Lane utilized for static traits.
  @SwimLane("sharedInfo")
  ValueLane<String> sharedInfo;

  // Lane utilized for dynamic traits.
  @SwimLane("sharedDynInfo")
  ValueLane<String> sharedDynInfo;

  @Override
  public void didStart() {
    System.out.println(nodeUri() + " didStart region");
    pourStatic();
    pourDynamic();
    close();
  }

  @Override
  public void willStop() {
    logMessage("willStop");
  }

  // Fetch info shared with other agents.
  void pourStatic() {
    // Fetching message via sharedInfo SwimLane.
    final String msg = this.sharedInfo.get();
    logMessage(msg);
  }

  // 1. Fetch value of properties belonging to the /liquid/:id pattern.
  // 2. Dynamically set value of sharedDynInfo lane for the opened agents.
  void pourDynamic() {
    String nodeString = nodeUri().toString();
    int c = nodeString.charAt(nodeString.length() - 1);

    final String waterDynInfo = getProp("waterDynType").stringValue(null);
    if (waterDynInfo != null) {
      logMessage("Dynamic Water Property '" + waterDynInfo + "'");
    }

    final String juiceDynInfo = getProp("juiceDynType").stringValue(null);
    if (juiceDynInfo != null) {
      logMessage("Dynamic Juice Property '" + juiceDynInfo + "'");
    }

    // Dynamically open either Water or Juice Agent at runtime.
    if(c%2 == 0) {
      this.sharedDynInfo.set(waterDynInfo);
      openAgent("wAgent", WaterAgent.class);
    }
    else {
      this.sharedDynInfo.set(juiceDynInfo);
      openAgent("jAgent", JuiceAgent.class);
    }
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
