package swim.basic;

import swim.api.client.Client;
import swim.api.downlink.MapDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/join-map-lanes/">Join
 * Map Lane</a> cookbook.
 *
 * <p>In this cookbook, three map lanes are created to hold information pertaining to state
 * statistics and is aggregated into a Join Map Lane. This join map lane checks entries that are
 * added to see if they exceed a threshold. If it is, then the data is logged.
 *
 * <p>See {@link BasicPlane}
 */
public final class CustomClient {

  private static final int THRESHOLD = 1000;

  private CustomClient() {}

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Starting client...");

    final Client clientRuntime = new ClientRuntime();
    clientRuntime.start();

    final MapDownlink<String, Integer> mapDownlink =
        clientRuntime
            .downlinkMap()
            .keyForm(Form.forString())
            .valueForm(Form.forInteger())
            .hostUri(BasicPlane.HOST_URI)
            .nodeUri("/join/state/all")
            .laneUri("join")
            .didUpdate(
                (key, newValue, oldValue) -> {
                  if (newValue > THRESHOLD) {
                    logStreet(key, newValue);
                  }
                })
            .open();

    Thread.sleep(1000);

    System.out.println("Shutting down client...");
    clientRuntime.stop();
  }

  private static void logStreet(String streetName, Integer population) {
    System.out.println(streetName + " has " + population + " residents");
  }
}
