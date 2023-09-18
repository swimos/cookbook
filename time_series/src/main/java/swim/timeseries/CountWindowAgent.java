package swim.timeseries;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.structure.Value;

public class CountWindowAgent extends AbstractAgent {

  public CountWindowAgent() {
  }

  private static final int MAX_HISTORY_SIZE = 10;

  @SwimLane("addEvent")
  private CommandLane<Value> addEvent = this.<Value>commandLane()
      .onCommand(v -> this.history.put(System.currentTimeMillis(), v));

  @SwimLane("history")
  private MapLane<Long, Value> history = this.<Long, Value>mapLane()
      .didUpdate((k, n, o) -> trimHistory());

  private void trimHistory() {
    final int dropCount = this.history.size() - MAX_HISTORY_SIZE;
    if (dropCount > 0) {
      this.history.drop(dropCount);
    }
  }

}
