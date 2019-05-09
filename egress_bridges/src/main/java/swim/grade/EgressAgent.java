package swim.grade;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.structure.Value;

public class EgressAgent extends AbstractAgent {

  @SwimLane("write")
  CommandLane<Value> write = this.<Value>commandLane()
      .onCommand(v -> {
        CustomDriver.updateGrade(
            v.get("id").intValue(),
            v.get("earned").intValue(),
            v.get("possible").intValue());
      });
}
