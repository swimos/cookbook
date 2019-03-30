package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.concurrent.TimerRef;
import swim.structure.Value;

public class UnitAgent extends AbstractAgent {

  private TimerRef timer;

  @SwimLane("minutesSincePublish")
  ValueLane<Integer> minutes = this.<Integer>valueLane()
      .didSet((n, o) -> {
        System.out.println((n * 10) + " seconds since last event");
      });

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
    this.timer = setTimer(10000, () -> {
        this.minutes.set(this.minutes.get() + 1);
        this.timer.reschedule(10000);
      });
  }

  private void cancelTimer() {
    if (this.timer != null) {
      this.timer.cancel();
    }
  }
}
