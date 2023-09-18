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

package swim.introspection;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.JoinValueLane;
import swim.structure.Value;
import swim.uri.Uri;

public class BuildingAgent extends AbstractAgent {

  public BuildingAgent() {
  }
    
  @SwimLane("rooms")
  JoinValueLane<String, Value> rooms = this.<String, Value>joinValueLane();

  @SwimLane("addRoom")
  CommandLane<Uri> addRoom = this.<Uri>commandLane()
      .onCommand(uri ->
              this.rooms.downlink(uri.pathName())
                  .nodeUri(uri)
                  .laneUri("info")
                  .open());

  @Override
  public void didStart() {
    info(nodeUri() + " didStart");
  }

}
