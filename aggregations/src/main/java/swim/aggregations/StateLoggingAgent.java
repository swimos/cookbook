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

package swim.aggregations;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.recon.Recon;
import swim.structure.Value;
import swim.uri.Uri;

public class StateLoggingAgent extends AbstractAgent {

  @SwimLane("status")
  public ValueLane<Value> status = this.<Value>valueLane()
      .didSet((nv, ov) -> info(nodeUri() + ": new status: " + Recon.toString(nv)));

  @SwimLane("addVehicle")
  public CommandLane<Uri> addVehicle = this.<Uri>commandLane()
      .onCommand(v -> info(nodeUri() + ": vehicle entering state: " + v));

  @SwimLane("removeVehicle")
  public CommandLane<Uri> removeVehicle = this.<Uri>commandLane()
      .onCommand(v -> info(nodeUri() + ": vehicle leaving state: " + v));

}
