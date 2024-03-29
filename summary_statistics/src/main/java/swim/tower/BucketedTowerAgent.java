package swim.tower;

import java.util.HashMap;
import java.util.Map;
import swim.api.SwimLane;
import swim.api.lane.MapLane;
import swim.recon.Recon;
import swim.structure.Value;

public class BucketedTowerAgent extends AbstractTowerAgent {

  private static final long SAMPLE_PERIOD_MS = 60000L;

  private Map<Long, TowerSummaryState> summaryStates;

  @SwimLane("summaries")
  MapLane<Long, Value> summaries = this.<Long, Value>mapLane()
      .didUpdate((k, n, o) ->
          System.out.println(nodeUri() + ": updated summary under " + k + " to " + Recon.toString(n)));

  @Override
  protected void updateSummary(long timestamp, Value v) {
    final long key = bucket(timestamp);
    final TowerSummaryState state = this.summaryStates.getOrDefault(key, new TowerSummaryState());
    state.addValue(v.get("s_n_ratio").doubleValue(),
        v.get("disconnects").intValue());
    this.summaries.put(key, state.getSummary());
    this.summaryStates.put(key, state);
  }

  private static long bucket(long timestamp) {
    return timestamp / SAMPLE_PERIOD_MS * SAMPLE_PERIOD_MS;
  }

  @Override
  public void didStart() {
    if (this.summaryStates != null) {
      this.summaryStates.clear();
    }
    this.summaryStates = new HashMap<>();
  }

}
