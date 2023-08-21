package swim.tower;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.structure.Value;

public abstract class AbstractTowerAgent extends AbstractAgent {

  @SwimLane("addMessage")
  CommandLane<Value> addMessage = this.<Value>commandLane()
      .onCommand(v -> {
        updateSummary(messageTimestamp(v), v);
      });

  protected long messageTimestamp(Value v) {
    return v.get("timestamp").longValue();
  }

  protected abstract void updateSummary(long timestamp, Value newValue);

}
