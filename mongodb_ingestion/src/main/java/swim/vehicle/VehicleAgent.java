package swim.vehicle;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.structure.Value;

public class VehicleAgent extends AbstractAgent {

  @SwimLane("addMessage")
  CommandLane<Value> addMessage = this.<Value>commandLane()
      .onCommand(v -> this.metadata.set(v));

  @SwimLane("metadata")
  ValueLane<Value> metadata = this.<Value>valueLane()
      .didSet((n, o) -> System.out.println(nodeUri() + ": received " + n));

}
