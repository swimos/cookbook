package swim.basic;

import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/server-downlinks/">Server Downlinks</a> cookbook.
 * <p>
 * In this cookbook, two Swim servers create two different agents simulating a warehouse and a supplier. A downlink is created
 * between them so that the supplier can resupply any stock that falls below a given threshold.
 * <p>
 * See {@link SupplierPlane}
 */
public class WarehousePlane extends AbstractPlane {

    public static void main(String[] args) {
        System.setProperty("swim.config", "warehouse.recon");

        final Kernel kernel = ServerLoader.loadServer();
        System.out.println("Starting Warehouse server...");
        kernel.start();
        kernel.run();
    }

    @Override
    public void didStart() {
        super.didStart();
        // Immediately wake up Warehouse Agent upon plane load
        context.command("/warehouse/cambridge", "wakeup", Value.absent());
    }
}
