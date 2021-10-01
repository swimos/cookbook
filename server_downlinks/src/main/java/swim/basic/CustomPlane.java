package swim.basic;

import swim.actor.ActorSpaceDef;
import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.service.web.WebServiceDef;
import swim.structure.Form;
import swim.structure.Text;
import swim.structure.Value;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/server-downlinks/">Server Downlinks</a> cookbook.
 * <p>
 * In this cookbook, two Swim servers create two different agents simulating a warehouse and a supplier. A downlink is created
 * between them so that the supplier can resupply any stock that falls below a given threshold.
 * <p>
 * See {@link BasicPlane}
 */
public class CustomPlane extends AbstractPlane {

    public final static String WAREHOUSE_HOST_URI = "warp://localhost:9001";

    @SwimRoute("/supplier")
    AgentRoute<SupplierAgent> supplierAgentType;

    public static void main(String[] args) throws InterruptedException {
        final Kernel kernel = ServerLoader.loadServerStack();

        final CustomPlane plane = kernel.openSpace(ActorSpaceDef.fromName("custom"))
                .openPlane("custom", CustomPlane.class);

        kernel.openService(WebServiceDef.standard().port(9002).spaceName("custom"));
        kernel.start();

        //Create a downlink to a different Swim server directly from this plane
        plane.downlinkValue()
                .valueForm(Form.forInteger())
                .hostUri(WAREHOUSE_HOST_URI)
                .nodeUri("/warehouse/cambridge")
                .laneUri("lastResupplyId")
                .didSet((newValue, oldValue) -> {
                    System.out.println("Latest supply id received at warehouse: " + newValue);
                })
                .open();

        //Take an item every second so the supplier can resupply when stock is low
        while(true){
            Thread.sleep(1000);
            plane.command(WAREHOUSE_HOST_URI, "/warehouse/cambridge", "takeItem", Text.from("foo"));
        }
    }

    @Override
    public void didStart() {
        super.didStart();
        // Immediately wake up SupplierAgent upon plane load
        context.command("/supplier", "wakeup", Value.absent());
        //Register the supplier to the warehouse
        context.command("/supplier", "register", Text.from("cambridge"));
    }
}
