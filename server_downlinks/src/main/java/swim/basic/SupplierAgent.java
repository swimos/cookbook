package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.structure.Form;
import swim.structure.Text;

public class SupplierAgent extends AbstractAgent {

  public SupplierAgent() {
  }
    
  private static final int RESUPPLY_THRESHOLD = 3;

  @SwimLane("register")
  CommandLane<String> register = this.<String>commandLane()
      .onCommand(this::addResupplyDownlink);

  /**
   * Creates a downlink to a given location that will resupply stock when it falls below the threshold
   */
  private void addResupplyDownlink(final String location) {
    final String warehouseNodeUri = "/warehouse/" + location;

    //Create a map downlink to a different Swim server from an agent
    this.downlinkMap()
        .keyForm(Form.forString()).valueForm(Form.forInteger())
        .hostUri(SupplierPlane.WAREHOUSE_HOST_URI)
        .nodeUri(warehouseNodeUri).laneUri("stock")
        .didUpdate((item, newValue, oldValue) -> {
          if (newValue < RESUPPLY_THRESHOLD) { //If the stock is too low then resupply it
            logResupply(item, newValue, location);
            this.command(SupplierPlane.WAREHOUSE_HOST_URI, warehouseNodeUri, "resupply", Text.from(item));
          }
        })
        .open();
  }

  @Override
  public void didStart() {
    logEvent("opened");
  }

  @Override
  public void willStop() {
    logEvent("stopped");
  }

  private void logResupply(final String item, final Integer stock, final String location) {
    logMessage("current stock of (" + item + ":" + stock + ") too low at " + location + " warehouse, resupplying");
  }

  private void logEvent(Object msg) {
    logMessage("supplier " + msg);
  }

  private void logMessage(final Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

}
