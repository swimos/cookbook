package swim.tower;

import swim.api.SwimLane;
import swim.api.lane.ValueLane;
import swim.recon.Recon;
import swim.structure.Value;

public class TowerAgent extends AbstractTowerAgent {

  private TowerSummaryState state;

  @SwimLane("summary")
  ValueLane<Value> summary = this.<Value>valueLane()
      .didSet((n, o) ->
          System.out.println(nodeUri() + ": updated summary to " + Recon.toString(n)));

  @Override
  protected void updateSummary(long timestamp, Value v) {
    this.state.addValue(v.get("s_n_ratio").doubleValue(),
        v.get("disconnects").intValue());
    this.summary.set(this.state.getSummary());
  }

  @Override
  public void didStart() {
    this.state = new TowerSummaryState();
  }

}
