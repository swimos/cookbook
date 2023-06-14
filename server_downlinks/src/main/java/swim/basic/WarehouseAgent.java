package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.api.lane.ValueLane;

public class WarehouseAgent extends AbstractAgent {

  @SwimLane("stock")
  MapLane<String, Integer> stock = this.<String, Integer>mapLane();

  @SwimLane("takeItem")
  CommandLane<String> takeItem =
      this.<String>commandLane()
          .onCommand(
              item -> {
                int newValue = this.stock.getOrDefault(item, 1) - 1;
                if (newValue < 0) {
                  newValue = 0;
                }
                this.stock.put(item, newValue);
              });
  @SwimLane("lastResupplyId")
  ValueLane<Integer> lastResupplyId = this.<Integer>valueLane();
  @SwimLane("resupply")
  CommandLane<String> resupply =
      this.<String>commandLane()
          .onCommand(
              item -> {
                final int newValue = this.stock.getOrDefault(item, 0) + 5;
                this.stock.put(item, newValue);
                this.lastResupplyId.set(this.lastResupplyId.get() + 1);
                logResupply(item, newValue);
              });

  @Override
  public void didStart() {
    this.lastResupplyId.set(0);
    logEvent("opened");
  }

  @Override
  public void willStop() {
    logEvent("stopped");
  }

  private void logResupply(final String item, final Integer stock) {
    logMessage(
        "resupplied "
            + item
            + " in "
            + getProp("location").stringValue()
            + " warehouse to "
            + stock
            + " units");
  }

  private void logEvent(final Object msg) {
    logMessage(getProp("location").stringValue() + " warehouse " + msg);
  }

  private void logMessage(final Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
