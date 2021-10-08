package swim.basic;

import swim.actor.ActorSpace;
import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.downlink.MapDownlink;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Form;
import swim.structure.Value;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/demand-map-lanes/">Demand Map Lanes</a> cookbook.
 * <p>
 * In this cookbook, an agent is created with a Demand Map Lane. This Demand Lane will only transform data when there is a downlink
 * that is subscribed to it.
 * <p>
 */
public class BasicPlane extends AbstractPlane {

    @SwimRoute("/unit")
    AgentRoute<UnitAgent> unitAgentType;

    public static void main(String[] args) throws InterruptedException {
        final Kernel kernel = ServerLoader.loadServer();
        final ActorSpace space = (ActorSpace) kernel.getSpace("basic");

        System.out.println("Starting server...");
        kernel.start();
        kernel.run();

        final MapDownlink<Integer, String> rawDownlink =
                space.downlinkMap()
                        .keyForm(Form.forInteger()).valueForm(Form.forString())
                        .nodeUri("/unit").laneUri("raw")
                        .didUpdate((key, newValue, oldValue) -> {
                            if(!oldValue.equals(newValue)) System.out.println("raw updated entry " + key + " : '" + newValue + "'");
                        }).open();
        Thread.sleep(1000); //Sleeps are used to ensure logging is in correct order for clarity

        rawDownlink.put(1, "bar");
        rawDownlink.put(2, "bar");
        Thread.sleep(1000);

        System.out.println("Creating downlink to data - raw will start being transformed.");

        final MapDownlink<Integer, String> dataDownlink =
                space.downlinkMap()
                        .keyForm(Form.forInteger()).valueForm(Form.forString())
                        .nodeUri("/unit").laneUri("data")
                        .didUpdate((key, newValue, oldValue) -> {
                            System.out.println("data updated entry " + key + " : '" + newValue + "'");
                        }).open();
        //Upon opening the downlink, the current values (bar) in raw will be transformed
        Thread.sleep(1000);

        rawDownlink.put(1, "baz");
        Thread.sleep(1000);

        System.out.println("Closing downlink to data - raw will stop being transformed.");
        dataDownlink.close();
        Thread.sleep(1000);

        //The following will not be transformed by the agent as there is no downlink subscribed to data lane
        rawDownlink.put(2, "baz");

        Thread.sleep(1000);
        kernel.stop();
    }

    @Override
    public void didStart() {
        super.didStart();
        //Immediately wake up the Unit Agent on plane load
        command("/unit", "wakeup", Value.absent());
    }

}
