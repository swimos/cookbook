package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.structure.Value;

public class ControlAgent extends AbstractAgent {

  @SwimLane("command")
  CommandLane<Value> command =
      this.<Value>commandLane().onCommand(value -> logMessage("command: " + value.toString()));

  @Override
  public void didStart() {
    super.didStart();
    System.out.println(nodeUri() + " didStart");
  }

  private void logMessage(final String message) {
    System.out.println(nodeUri() + ": " + message);
  }
}
