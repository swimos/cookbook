package swim.timeseries;

import java.util.Iterator;
import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.structure.Value;

public class TimeWindowAgent extends AbstractAgent {

  private static final long TIME_INTERVAL_MS = 30000L;

  @SwimLane("addEvent")
  public CommandLane<Value> addEvent = this.<Value>commandLane()
      .onCommand(v -> this.history.put(System.currentTimeMillis(), v));

  @SwimLane("history")
  public MapLane<Long, Value> history = this.<Long, Value>mapLane()
      .didUpdate((k,n,o) -> trimHistory());

  private void trimHistory() {
    final long oldestAllowedTimestamp = this.history.lastKey() - TIME_INTERVAL_MS;
    final Iterator<Long> iterator = this.history.keyIterator();

    while (iterator.hasNext()) {
      final long key = iterator.next();
      if (key < oldestAllowedTimestamp) {
        // If the key is too old then remove it
        this.history.remove(key);
      } else {
        // Keys are ordered so stop when first key within allowed time is found
        break;
      }
    }
  }

}
