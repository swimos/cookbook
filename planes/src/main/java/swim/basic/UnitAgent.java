package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;

public class UnitAgent extends AbstractAgent {

  @SwimLane("info")
  ValueLane<String> info = this.<String>valueLane();

  @SwimLane("publishInfo")
  CommandLane<String> publishInfo = this.<String>commandLane()
      .onCommand(msg -> {
        this.info.set("from publishInfo: " + msg);
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
