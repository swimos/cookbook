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
import swim.api.lane.JoinValueLane;
import swim.api.lane.ValueLane;
import swim.structure.Record;
import swim.structure.Value;
import swim.uri.Uri;

public class StateAgent extends AbstractAgent {

  @SwimLane("addVehicle")
  public CommandLane<Uri> addVehicle = this.<Uri>commandLane()
      .onCommand(v ->
          this.vehicles
              .downlink(v)
              .nodeUri(v)
              .laneUri("status")
              .open());

  @SwimLane("removeVehicle")
  public CommandLane<Uri> removeVehicle = this.<Uri>commandLane()
      .onCommand(v ->
          this.vehicles
              .remove(v));

  @SwimLane("vehicles")
  public JoinValueLane<Uri, Value> vehicles = this.<Uri, Value>joinValueLane()
      .didUpdate((k, nv, ov) -> computeStatus());

  @SwimLane("status")
  public ValueLane<Value> status = this.<Value>valueLane();

  private void computeStatus() {

    int totalSpeed = 0, maxSpeed = 0, movingVehicles = 0;

    for (final Uri vehicleUri : this.vehicles.keySet()) {
      final Value vehicleStatus = this.vehicles.get(vehicleUri);
      final int vehicleSpeed = vehicleStatus.get("speed").intValue(0);

      totalSpeed += vehicleSpeed;

      if (vehicleSpeed > maxSpeed) {
        maxSpeed = vehicleSpeed;
      }

      if (vehicleStatus.get("isMoving").booleanValue(false)) {
        movingVehicles++;
      }

    }

    this.status.set(
        Record.create(5)
            .slot("vehicle_count", this.vehicles.size())
            .slot("moving_vehicle_count", movingVehicles)
            .slot("mean_speed", this.vehicles.size() == 0 ? 0 : totalSpeed / this.vehicles.size())
            .slot("mean_speed_of_moving_vehicles", movingVehicles == 0 ? 0 : totalSpeed / movingVehicles)
            .slot("top_speed", maxSpeed)
    );
  }

}
