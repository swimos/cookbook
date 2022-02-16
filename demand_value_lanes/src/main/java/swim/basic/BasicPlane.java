package swim.basic;

import swim.actor.ActorSpace;
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
 * See {@link CustomClient}
 */
public class BasicPlane extends AbstractPlane {

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

    //Incrementally change the raw lane so the client can observe changes
    int messageNumber = 0;
    while (true) {
      Thread.sleep(1000);
      rawDownlink.set(encode("MESSAGE_" + messageNumber++));
    }
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
