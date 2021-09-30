package swim.basic;

import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.downlink.MapDownlink;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Form;
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

    @SwimRoute("/supplier")
    AgentRoute<SupplierAgent> supplierAgentType;

    public static void main(String[] args) throws InterruptedException {
        final Kernel kernel = ServerLoader.loadServer();

        kernel.start();
        System.out.println("Running Basic server...");
        kernel.run();
    }

    @Override
    public void didStart() {
        super.didStart();
        // Immediately wake up SupplierAgent upon plane load
        context.command("/supplier", "wakeup", Value.absent());

        //Create a downlink from our join lane to log whenever stock changes in any warehouse
        final MapDownlink<String, Integer> totalStock = context.downlinkMap()
                .keyForm(Form.forString()).valueForm(Form.forInteger())
                .nodeUri("/supplier").laneUri("stock")
                .didUpdate(((key, newValue, oldValue) -> {
                    System.out.println(key + " updated from " + oldValue + " to " + newValue);
                }))
                .open();
    }
}
