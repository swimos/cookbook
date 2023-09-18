package swim.timeseries;

import java.sql.Timestamp;
import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.MapLane;
import swim.recon.Recon;
import swim.structure.Value;

public class HistoryLoggingAgent extends AbstractAgent {

  public HistoryLoggingAgent() {
  }

  @SwimLane("history")
  private MapLane<Long, Value> history = this.<Long, Value>mapLane()
      .didRemove(this::logRemoval)
      .didDrop(this::logDrop);

  private void logRemoval(final long key, final Value oldValue) {
    info(
        new Timestamp(System.currentTimeMillis())
            + " "
            + nodeUri().toString()
            + ": "
            + "Removed record: "
            + "{ "
            + new Timestamp(key)
            + ": "
            + Recon.toString(oldValue)
            + " }"
    );
  }

  private void logDrop(final int lower) {
    info(
        new Timestamp(System.currentTimeMillis())
            + " "
            + nodeUri().toString()
            + ": "
            + "Dropped records: "
            + lower
            + " , new record count: "
            + this.history.size()
    );
  }

}
