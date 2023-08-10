package swim.timeseries;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.structure.Value;

public class TimeWindowAgent extends AbstractAgent {

  private static final long MAX_HISTORY_TIME_MS = 30000L;

  @SwimLane("addEvent")
  public CommandLane<Value> addEvent = this.<Value>commandLane()
      .onCommand(v -> this.history.put(System.currentTimeMillis(), v));

  @SwimLane("history")
  public MapLane<Long, Value> history = this.<Long, Value>mapLane()
      .didUpdate((k,n,o) -> startRemoveTimer(k));

  private void startRemoveTimer(final long key) {
    setTimer(MAX_HISTORY_TIME_MS, () -> {
      this.history.remove(key);
    });
  }

}
