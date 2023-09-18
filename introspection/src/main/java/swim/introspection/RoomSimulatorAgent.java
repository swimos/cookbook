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

import java.util.Random;
import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.ValueLane;
import swim.structure.Record;
import swim.structure.Value;

public class RoomSimulatorAgent extends AbstractAgent {

  private static final long SIMULATE_INTERVAL = 10000L;

  @SwimLane("info")
  ValueLane<Value> info = this.<Value>valueLane();

  @SwimLane("lights")
  ValueLane<Boolean> lights = this.<Boolean>valueLane();

  @SwimLane("occupied")
  ValueLane<Boolean> occupied = this.<Boolean>valueLane()
      .didSet((newValue, oldValue) -> {
        info(nodeUri() + ": now " + (newValue ? "in use" : "vacant"));

        if (!newValue && this.lights.get()) {
          warn(nodeUri() + ": lights on but vacant");
        }
      });

  @SwimLane("temperature")
  ValueLane<Integer> temperature = this.<Integer>valueLane();

  private void toggleLights() {
    this.lights.set(!this.lights.get());
  }

  private void toggleOccupied() {
    this.occupied.set(!this.occupied.get());
  }

  private void changeTemperature() {
    final int currentTemp = this.temperature.get();
    int tempChange;
    if (currentTemp >= 26) {
      tempChange = -1;
    } else if (currentTemp <= 15) {
      tempChange = +1;
    } else {
      tempChange =  new Random().nextInt(3) - 1;
    }
    if (tempChange != 0) {
      this.temperature.set(currentTemp + tempChange);
    }
  }

  private void simulate() {
    final Random random = new Random();

    if (random.nextBoolean()) {
      toggleLights();
    }

    if (random.nextBoolean()) {
      toggleOccupied();
    }

    if (random.nextBoolean()) {
      changeTemperature();
    }

    setTimer(SIMULATE_INTERVAL, this::simulate);
  }

  private void init() {
    this.lights.set(false);
    this.occupied.set(false);
    this.temperature.set(20);
    this.info.set(
        Record.create(2)
            .slot("id", this.getProp("roomId").intValue())
            .slot("description", "Meeting Room")
    );
  }

  @Override
  public void didStart() {
    init();
    simulate();
  }

}
