package swim.basic;

import swim.api.agent.AbstractAgent;

public class UnitAgent extends AbstractAgent {

  @Override
  public void didStart() {
    logMessage("didStart");
  }

  @Override
  public void willStop() {
    logMessage("willStop");
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
