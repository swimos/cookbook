package swim.basic;

import swim.api.downlink.ValueDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/demand-value-lanes/">Demand Value Lanes</a> cookbook.
 * <p>
 * In this cookbook, an agent is created with a Demand Lane. This Demand Lane will only transform data when there is a downlink
 * that is subscribed to it.
 * <p>
 * See {@link BasicPlane}
 */
public class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();

    final String hostUri = "warp://localhost:9001";

    System.out.println("Opening downlink to data, raw will start being decoded");
    final ValueDownlink<String> dataDownlnink =
            swimClient.downlinkValue()
                    .valueForm(Form.forString())
                    .hostUri(hostUri)
                    .nodeUri("/unit").laneUri("data")
                    .didSet((n, o) -> System.out.println("data updated from '" + o + "' to '" + n + "'"))
                    .open();

    Thread.sleep(10000);
    System.out.println("Closing downlink to data, raw will stop being decoded");
    dataDownlnink.close();
    swimClient.stop();
  }

}
