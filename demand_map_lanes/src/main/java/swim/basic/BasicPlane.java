package swim.basic;

import java.util.Base64;
import swim.actor.ActorSpace;
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
 * that is subscribed to it, for given key.
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

    final MapDownlink<String, String> rawDownlink = space.downlinkMap()
        .keyForm(Form.forString()).valueForm(Form.forString())
        .nodeUri("/unit").laneUri("raw")
        .didUpdate((key, newValue, oldValue) -> {
          if (!oldValue.equals(newValue)) {
            System.out.println("raw updated entry " + key + " : '" + newValue + "'");
          }
        })
        .open();

    //Incrementally update raw lane so downlink can observe changes
    int messageNumber = 0;
    while (true) {
      Thread.sleep(2000);
      rawDownlink.put("foo", encode("foo_message_" + messageNumber));
      rawDownlink.put("bar", encode("bar_message_" + messageNumber++));
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
