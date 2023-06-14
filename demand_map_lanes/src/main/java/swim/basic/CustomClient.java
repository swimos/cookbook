package swim.basic;

import swim.api.downlink.MapDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;

/**
 * The complimentary code as part of the <a
 * href="https://swimos.org/tutorials/demand-map-lanes/">Demand Map Lanes</a> cookbook.
 *
 * <p>In this cookbook, an agent is created with a Demand Map Lane. This Demand Lane will only
 * transform data when there is a downlink that is subscribed to it, for given key.
 *
 * <p>See {@link BasicPlane}
 */
public final class CustomClient {

  private CustomClient() {}

  public static void main(String[] args) throws InterruptedException {
    final ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();

    final String hostUri = "warp://localhost:9001";

    System.out.println("Opening downlink to data with no parameter. Raw will start to be decoded.");
    final MapDownlink<String, String> dataDownlink =
        swimClient
            .downlinkMap()
            .keyForm(Form.forString())
            .valueForm(Form.forString())
            .hostUri(hostUri)
            .nodeUri("/unit")
            .laneUri("data")
            .didUpdate(
                (key, newValue, oldValue) -> {
                  System.out.println("data updated entry " + key + " : '" + newValue + "'");
                })
            .open();

    Thread.sleep(10000);
    dataDownlink.close();
    System.out.println("Closed downlink to data with no parameter. Raw will stop being decoded.");

    System.out.println("Opening downlink to data for key 'bar'. Raw will start to be decoded.");
    final MapDownlink<String, String> dataDownlinkWithQueryParameter =
        swimClient
            .downlinkMap()
            .keyForm(Form.forString())
            .valueForm(Form.forString())
            .hostUri(hostUri)
            .nodeUri("/unit")
            .laneUri("data?name=bar")
            .didUpdate(
                (key, newValue, oldValue) -> {
                  System.out.println("data updated entry " + key + " : '" + newValue + "'");
                })
            .open();

    Thread.sleep(10000);
    dataDownlinkWithQueryParameter.close();
    swimClient.stop();
    System.out.println("Closed downlink to data for key 'bar'. Raw will stop being decoded.");
  }
}
