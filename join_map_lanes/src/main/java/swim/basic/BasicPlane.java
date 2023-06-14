package swim.basic;

import swim.actor.ActorSpace;
import swim.api.downlink.MapDownlink;
import swim.api.plane.AbstractPlane;
import swim.api.space.Space;
import swim.kernel.Kernel;
import swim.server.ServerLoader;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/join-map-lanes/">Join
 * Map Lane</a> cookbook.
 *
 * <p>In this cookbook, three map lanes are created to hold information pertaining to state
 * statistics and is aggregated into a Join Map Lane. This join map lane checks entries that are
 * added to see if they exceed a threshold. If it is, then the data is logged.
 *
 * <p>See {@link CustomClient}
 */
public class BasicPlane extends AbstractPlane {

  static final String HOST_URI = "warp://localhost:9001";

  /** Returns a downlink map for a given plane and node URI using the 'state' lane URI */
  private static MapDownlink<String, Integer> initDownlink(Space space, String uri) {
    return space
        .downlinkMap()
        .keyClass(String.class)
        .valueClass(Integer.class)
        .hostUri(HOST_URI)
        .nodeUri(uri)
        .laneUri("state")
        .open();
  }

  public static void main(String[] args) throws InterruptedException {
    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("basic");
    kernel.start();

    final MapDownlink<String, Integer> californiaDownlink =
        initDownlink(space, "/state/california");
    final MapDownlink<String, Integer> texasDownlink = initDownlink(space, "/state/texas");
    final MapDownlink<String, Integer> floridaDownlink = initDownlink(space, "/state/florida");

    // A preferred approach is to use countdown latches but for brevity a sleep suffices
    Thread.sleep(1000);

    californiaDownlink.put("cal_st_george", 500);
    californiaDownlink.put("cal_centre_st", 1000);
    texasDownlink.put("tx_crockett", 100);
    texasDownlink.put("tx_houston", 200);
    floridaDownlink.put("fl_nene", 3000);
    floridaDownlink.put("fl_worth", 4000);

    System.out.println("Started plane...");
  }
}
