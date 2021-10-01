package swim.basic;

import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
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
 * See {@link CustomPlane}
 */
public class BasicPlane extends AbstractPlane {

    @SwimRoute("/warehouse/:location")
    AgentRoute<WarehouseAgent> warehouseAgentType;

    public static void main(String[] args) throws InterruptedException {
        final Kernel kernel = ServerLoader.loadServer();

        kernel.start();
        System.out.println("Running Basic server...");
        kernel.run();
    }

    @Override
    public void didStart() {
        super.didStart();
        // Immediately wake up Warehouse Agent upon plane load
        context.command("/warehouse/cambridge", "wakeup", Value.absent());
    }
}
