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

import java.util.Base64;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/demand-value-lanes/">Demand Value Lanes</a> cookbook.
 * <p>
 * In this cookbook, an agent is created with a Demand Lane. This Demand Lane will only transform data when there is a downlink
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

        final ValueDownlink<String> rawDownlink =
                space.downlinkValue()
                        .valueForm(Form.forString())
                        .nodeUri("/unit").laneUri("raw")
                        .didSet((n, o) -> {
                            if (!n.equals(o)) System.out.println("raw updated from '" + o + "' to '" + n + "'");
                        })
                        .open();
        Thread.sleep(1000); //Sleeps are used to ensure logging is in correct order for clarity

        //This message will never be decoded because the downlink has not been created yet
        rawDownlink.set(encode("MESSAGE_ONE"));
        Thread.sleep(1000);

        //This message will be decoded as it will be the value of the lane when the downlink is created
        rawDownlink.set(encode("MESSAGE_TWO"));
        Thread.sleep(1000);

        System.out.println("Creating downlink to data - raw will start being decoded.");
        final ValueDownlink<String> dataDownlnink =
                space.downlinkValue()
                        .valueForm(Form.forString())
                        .nodeUri("/unit").laneUri("data")
                        .didSet((n, o) -> System.out.println("data updated from '" + o + "' to '" + n + "'"))
                        .open();
        Thread.sleep(1000);

        rawDownlink.set(encode("MESSAGE_THREE"));
        Thread.sleep(1000);

        System.out.println("Closing downlnink to data - raw will no longer be decoded.");
        dataDownlnink.close();
        rawDownlink.set(encode("MESSAGE_FOUR"));
        Thread.sleep(1000);

        kernel.stop();
    }

    private static String encode(final String rawMessage) {
        return Base64.getEncoder().encodeToString(rawMessage.getBytes());
    }

    @Override
    public void didStart() {
        super.didStart();
        //Immediately wake up the Unit Agent on plane load
        command("/unit", "wakeup", Value.absent());
    }

}
