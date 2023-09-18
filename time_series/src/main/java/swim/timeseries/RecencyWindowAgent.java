package swim.timeseries;

import java.util.Iterator;
import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.concurrent.TimerRef;
import swim.structure.Value;

public class RecencyWindowAgent extends AbstractAgent {

  public RecencyWindowAgent() {
  }

  private static final long MAX_HISTORY_TIME_MS = 30000L;

  private TimerRef timer;

  @SwimLane("addEvent")
  private CommandLane<Value> addEvent = this.<Value>commandLane()
      .onCommand(v -> this.history.put(System.currentTimeMillis(), v));

  @SwimLane("history")
  private MapLane<Long, Value> history = this.<Long, Value>mapLane()
      .didUpdate((k, n, o) -> rescheduleNextTrim());

  private void trimHistory() {
    final long oldestAllowedTimestamp = System.currentTimeMillis() - MAX_HISTORY_TIME_MS;
    final Iterator<Long> iterator = this.history.keyIterator();

    while (iterator.hasNext()) {
      final long key = iterator.next();
      if (key < oldestAllowedTimestamp) {
        // If the key is too old then remove it
        this.history.remove(key);
      } else {
        // Keys are ordered so stop when first key within allowed time is found
        rescheduleNextTrim();
        break;
      }
    }
  }

  private void rescheduleNextTrim() {
    if (this.timer != null && this.timer.isScheduled()) {
      // The timer is already being handled
      return;
    }

    final long timeUntilNextTrim = (this.history.firstKey() + MAX_HISTORY_TIME_MS) - System.currentTimeMillis();
    if (timeUntilNextTrim > 0) {
      // Set a timer for when the next record needs to be dropped
      this.timer = setTimer(timeUntilNextTrim, this::trimHistory);
    } else if (!this.history.isEmpty()) {
      // A record needs to be dropped now
      trimHistory();
    }
  }

}
