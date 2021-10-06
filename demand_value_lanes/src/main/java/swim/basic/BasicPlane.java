package swim.basic;

import swim.actor.ActorSpace;
import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.downlink.ValueDownlink;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Form;
import swim.structure.Value;

public class BasicPlane extends AbstractPlane {

    @SwimRoute("/unit")
    AgentRoute<UnitAgent> unitAgentType;

    public static void main(String[] args) throws InterruptedException {
        final Kernel kernel = ServerLoader.loadServer();
        final ActorSpace space = (ActorSpace) kernel.getSpace("basic");

        System.out.println("Starting server...");
        kernel.start();
        kernel.run();

        final ValueDownlink<Integer> rawDownlink =
                space.downlinkValue()
                        .valueForm(Form.forInteger())
                        .nodeUri("/unit").laneUri("raw")
                        .didSet((n, o) -> System.out.println("raw updated from " + o + " to " + n))
                        .open();

        Thread.sleep(1000); //Sleeps are used to ensure logging is in correct order for clarity
        rawDownlink.set(1);
        Thread.sleep(1000);

        System.out.println("Creating downlink to data - raw will start being transformed.");
        final ValueDownlink<Integer> dataDownlnink =
                space.downlinkValue()
                        .valueForm(Form.forInteger())
                        .nodeUri("/unit").laneUri("data")
                        .didSet((n, o) -> System.out.println("data updated from " + o + " to " + n))
                        .open();

        Thread.sleep(1000);
        rawDownlink.set(2);
        Thread.sleep(1000);

        System.out.println("Closing downlnink to data - raw will no longer be transformed.");
        dataDownlnink.close();
        rawDownlink.set(3);
    }

    @Override
    public void didStart() {
        super.didStart();
        //Immediately wake up the Unit Agent on plane load
        command("/unit", "wakeup", Value.absent());
    }

}
