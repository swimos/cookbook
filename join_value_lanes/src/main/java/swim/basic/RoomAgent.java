// Copyright 2015-2019 SWIM.AI inc.
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
import swim.api.lane.ValueLane;
import swim.structure.Value;

public class RoomAgent extends AbstractAgent {

  @SwimLane("lights")
  ValueLane<Boolean> lights = this.valueLane();
  @SwimLane("toggleLights")
  CommandLane<String> toggleLights = this.<String>commandLane().onCommand(msg -> {
    this.lights.set(!lights.get());
  });

  @Override
  public void didStart() {
    this.lights.set(false);
    register();
  }

  private void register() {
    String buildingUri = "/building/" + this.getProp("building").stringValue();
    Value roomId = this.getProp("room");
    command(buildingUri, "registerRoom", roomId);
  }

}
