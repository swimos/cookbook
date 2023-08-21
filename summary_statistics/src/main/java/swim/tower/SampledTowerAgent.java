package swim.tower;

import java.util.HashMap;
import java.util.Map;
import swim.api.SwimLane;
import swim.api.lane.MapLane;
import swim.recon.Recon;
import swim.structure.Value;

public class SampledTowerAgent extends AbstractTowerAgent {

  private static final long SAMPLE_PERIOD_MS = 60000L;

  private Map<Long, TowerSummaryState> summaryStates;

  @SwimLane("summaries")
  MapLane<Long, Value> summaries = this.<Long, Value>mapLane()
      .didUpdate((k, n, o) ->
          System.out.println(nodeUri() + ": updated summary under " + k + " to " + Recon.toString(n)));

  @Override
  protected void updateSummary(long timestamp, Value v) {
    final double newValue = v.get("mean_ul_sinr").doubleValue();
    final long key = statesKey(timestamp);
    final TowerSummaryState state = this.summaryStates.getOrDefault(key, new TowerSummaryState());
    state.addValue(newValue);
    this.summaries.put(key, state.getSummary());
    this.summaryStates.put(key, state);
  }

  private static long statesKey(long timestamp) {
    // Floor div then multiplication quickly purges non-significant digits.
    // Logic may not work for long/awkward SAMPLE_PERIOD_MS.
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
