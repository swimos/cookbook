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

import java.util.Random;
import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.ValueLane;
import swim.structure.Record;
import swim.structure.Value;

public class VehicleSimulatorAgent extends AbstractAgent {

  private static final String[] STATES = {"California", "Oregon"};
  private static final int MAX_SPEED = 100;
  private static final long UPDATE_INTERVAL = 10000L;

  @SwimLane("status")
  public ValueLane<Value> status = this.<Value>valueLane();

  private Value randomVehicleStatus() {

    final boolean isMoving = new Random().nextBoolean();

    return Record.create(3)
        .slot("state", randomState())
        .slot("isMoving", isMoving)
        .slot("speed", isMoving ? randomSpeed() : 0);
  }

  private String randomState() {
    return STATES[ new Random().nextInt(STATES.length) ];
  }

  private int randomSpeed() {
    return new Random().nextInt(MAX_SPEED) + 1;
  }

  private void setRandomStatusAndSetTimer() {
    this.status.set(randomVehicleStatus());
    setTimer(UPDATE_INTERVAL, this::setRandomStatusAndSetTimer);
  }

  @Override
  public void didStart() {
    setRandomStatusAndSetTimer();
  }

}
