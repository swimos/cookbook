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

package swim.aggregations;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.structure.Value;
import swim.uri.Uri;

public class VehicleAgent extends AbstractAgent {

  public VehicleAgent() {
  }

  private String currentState;

  @SwimLane("addEvent")
  private CommandLane<Value> addEvent = this.<Value>commandLane()
      .onCommand(v -> this.status.set(v));

  @SwimLane("status")
  private ValueLane<Value> status = this.<Value>valueLane()
      .didSet((nv, ov) -> joinState(nv.get("state").stringValue(null)));

  private void joinState(final String state) {
    if (isSameAsCurrentState(state)) {
      // If the new state is the same as the current state, then do nothing
      return;
    }

    if (this.currentState != null) {
      command("/state/" + this.currentState,
          "removeVehicle",
          Uri.form().mold(nodeUri()).toValue()
      );
    }

    if (state != null) {
      command(
          "/state/" + state,
          "addVehicle",
          Uri.form().mold(nodeUri()).toValue()
      );
    }

    this.currentState = state;
  }

  private boolean isSameAsCurrentState(final String state) {
    return (this.currentState == null && state == null)
        || (this.currentState != null && this.currentState.equals(state));
  }

}
