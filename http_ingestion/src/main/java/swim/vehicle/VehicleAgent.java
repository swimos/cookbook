package swim.vehicle;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.structure.Value;

public class VehicleAgent extends AbstractAgent {

  @SwimLane("addMessage")
  CommandLane<Value> addMessage = this.<Value>commandLane()
      .onCommand(v -> {
        this.history.put(v.get("timestamp").longValue(), v);
      });

  @SwimLane("history")
  MapLane<Long, Value> history = this.<Long, Value>mapLane()
      .didUpdate((k, n, o) -> {
        System.out.println(nodeUri() + ": received " + n);
      });

  public VehicleAgent() {
  }

}
