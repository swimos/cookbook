package swim.basic.warp;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.ValueLane;

public class SourceAgent extends AbstractAgent {

  @SwimLane("val")
  ValueLane<String> val = this.<String>valueLane();
}
