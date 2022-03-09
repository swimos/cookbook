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

public class JuiceAgent extends AbstractAgent {

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

  // Fetch value of property belonging to the /juice uri.
  void pour() {
    final String juiceInfo = getProp("juiceType").stringValue(null);
    if (juiceInfo != null) {
      logMessage("Juice Property '" + juiceInfo + "'");

      // Set Value for the sharedJuiceInfo SwimLane which is shared by Liquid
      // and Juice Agent
      this.sharedJuiceInfo.set("Shared liquid is " + juiceInfo);
    }
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
