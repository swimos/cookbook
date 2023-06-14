package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.structure.Value;

public class UnitAgent extends AbstractAgent {

  @SwimLane("info")
  ValueLane<Value> info =
      this.<Value>valueLane()
          .didSet(
              (newValue, oldValue) ->
                  logMessage("info changed from " + oldValue + " to " + newValue));

  @SwimLane("setInfo")
  CommandLane<Value> setInfo = this.<Value>commandLane().onCommand(body -> this.info.set(body));

  @SwimLane("adminInfo")
  ValueLane<Value> adminInfo =
      this.<Value>valueLane()
          .didSet(
              (newValue, oldValue) ->
                  logMessage("adminInfo changed from " + oldValue + " to " + newValue));

  @Override
  public void didStart() {
    super.didStart();
    System.out.println(nodeUri() + " didStart");
  }

  private void logMessage(final String message) {
    System.out.println(nodeUri() + ": " + message);
  }
}
