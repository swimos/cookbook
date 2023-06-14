package swim.basic;

import swim.actor.ActorSpace;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Form;
import swim.structure.Text;
import swim.structure.Value;

/**
 * The complimentary code as part of the <a
 * href="https://swimos.org/tutorials/server-downlinks/">Server Downlinks</a> cookbook.
 *
 * <p>In this cookbook, two Swim servers create two different agents simulating a warehouse and a
 * supplier. A downlink is created between them so that the supplier can resupply any stock that
 * falls below a given threshold.
 *
 * <p>See {@link WarehousePlane}
 */
public class SupplierPlane extends AbstractPlane {

  public static final String WAREHOUSE_HOST_URI = "warp://localhost:9001";

  public static void main(String[] args) {
    System.setProperty("swim.config", "supplier.recon");

    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("supplier");
    kernel.start();

    // Create a value downlink to a different Swim server directly from this plane
    space
        .downlinkValue()
        .valueForm(Form.forInteger())
        .hostUri(WAREHOUSE_HOST_URI)
        .nodeUri("/warehouse/cambridge")
        .laneUri("lastResupplyId")
        .didSet(
            (newValue, oldValue) -> {
              logMessage("latest supply id received at warehouse: " + newValue);
            })
        .open();

    // Create a map downlink to a different Swim server directly from this plane
    space
        .downlinkMap()
        .keyForm(Form.forString())
        .valueForm(Form.forInteger())
        .hostUri(WAREHOUSE_HOST_URI)
        .nodeUri("/warehouse/cambridge")
        .laneUri("stock")
        .didUpdate(
            ((key, newValue, oldValue) -> {
              logMessage(key + " stock at cambridge warehouse changed to: " + newValue);
            }))
        .open();
  }

  private static void logMessage(final String message) {
    System.out.println("plane: " + message);
  }

  @Override
  public void didStart() {
    super.didStart();
    // Immediately wake up SupplierAgent upon plane load
    context.command("/supplier", "wakeup", Value.absent());
    // Register the supplier to the warehouse
    context.command("/supplier", "register", Text.from("cambridge"));
    // Immediately wake up CustomerAgent upon plane load
    context.command("/customer/1", "wakeup", Value.absent());
    // Register the customer to the warehouse
    context.command("/customer/1", "register", Text.from("cambridge"));
  }
}
