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

package swim.liquid.agent;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.ValueLane;

public class LiquidAgent extends AbstractAgent {

  // Lane utilized for static and dynamic traits.
  @SwimLane("sharedInfo")
  ValueLane<String> sharedInfo;

  @Override
  public void didStart() {
    System.out.println(nodeUri() + " didStart");
    if (nodeUri().toString().contains("dynamic")) {
      pourDynamic();
    } else {
      pourStatic();
    }
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

  // 1. Fetch value of properties belonging to the /liquid/:trait/:id/:id pattern.
  // 2. Open agents dynamically.
  // 2. Fetch value of sharedInfo lane for the opened agents.
  void pourDynamic() {
    // Dynamically open either Water or Juice Agent at runtime.
    if (getProp("id1").stringValue().equals("water")) {
      openAgent("wAgent", WaterAgent.class);
    } else if (getProp("id1").stringValue().equals("juice")) {
      openAgent("jAgent", JuiceAgent.class);
    }
    logMessage(this.sharedInfo.get());
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

}
