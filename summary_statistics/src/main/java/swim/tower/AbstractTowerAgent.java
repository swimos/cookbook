package swim.tower;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.structure.Value;

public abstract class AbstractTowerAgent extends AbstractAgent {

  @SwimLane("addMessage")
  CommandLane<Value> addMessage = this.<Value>commandLane()
      .onCommand(v -> {
        // Your use case may require System.currentTimeMillis() instead
        final long t = v.get("timestamp").longValue();
        updateSummary(t, v);
      });

  protected abstract void updateSummary(long timestamp, Value newValue);

}
