package swim.vehicle;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.structure.Value;

public class VehicleAgent extends AbstractAgent {

  @SwimLane("addMessage")
  CommandLane<Value> addMessage = this.<Value>commandLane()
      .onCommand(v -> {
        System.out.println(nodeUri() + ": received " + v);
      });

}
