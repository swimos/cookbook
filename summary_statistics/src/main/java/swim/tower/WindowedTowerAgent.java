package swim.tower;

import swim.api.SwimLane;
import swim.api.lane.MapLane;
import swim.recon.Recon;
import swim.structure.Value;

public class WindowedTowerAgent extends AbstractTowerAgent {

  private static final long SAMPLE_PERIOD_MS = 60000L;

  private TowerSummaryState currentState;
  private long currentBucket;

  @SwimLane("summaries")
  MapLane<Long, Value> summaries = this.<Long, Value>mapLane()
      .didUpdate((k, n, o) ->
          System.out.println(nodeUri() + ": updated summary under " + k + " to " + Recon.toString(n)));

  @Override
  protected long messageTimestamp(Value v) {
    return System.currentTimeMillis();
  }

  @Override
  protected void updateSummary(long timestamp, Value v) {
    final long key = bucket(timestamp);
    if (key != this.currentBucket) {
      resetState(timestamp);
    }
    this.currentState.addValue(v.get("mean_ul_sinr").doubleValue(),
        v.get("rrc_re_establishment_failures").intValue());
    this.summaries.put(key, this.currentState.getSummary());
  }

  private void resetState(long now) {
    this.currentState = new TowerSummaryState();
    this.currentBucket = bucket(now);
  }

  private static long bucket(long timestamp) {
    return timestamp / SAMPLE_PERIOD_MS * SAMPLE_PERIOD_MS;
  }

  @Override
  public void didStart() {
    resetState(System.currentTimeMillis());
  }

}
