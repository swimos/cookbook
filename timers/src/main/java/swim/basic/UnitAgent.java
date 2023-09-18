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

package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.concurrent.TimerRef;
import swim.structure.Value;

public class UnitAgent extends AbstractAgent {

  public UnitAgent() {
  }
    
  @SwimLane("minutesSincePublish")
  ValueLane<Integer> minutes = this.<Integer>valueLane()
          .didSet((n, o) -> {
            System.out.println((n * 1) + " seconds since last event");
          });
  private TimerRef timer;
  @SwimLane("publish")
  CommandLane<Value> publish = this.<Value>commandLane()
          .onCommand(v -> {
            this.minutes.set(0);
            resetTimer();
          });

  @Override
  public void didStart() {
    resetTimer();
  }

  @Override
  public void willStop() {
    cancelTimer();
  }

  private void resetTimer() {
    cancelTimer();
    this.timer = setTimer(1000, () -> {
      this.minutes.set(this.minutes.get() + 1);
      this.timer.reschedule(1000);
    });
  }

  private void cancelTimer() {
    if (this.timer != null) {
      this.timer.cancel();
    }
    this.timer = null;
  }

}
