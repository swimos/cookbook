package swim.basic;

import swim.api.downlink.ValueDownlink;
import swim.client.ClientRuntime;
import swim.structure.Text;
import swim.structure.Value;

class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();
    final String hostUri = "warp://localhost:9001";
    final String nodeUri = "/unit/foo";
    // Reduce probability of startup race; no need to conflate this example
    // with proper synchronization just yet
    swimClient.command(hostUri, nodeUri, "WAKEUP", Value.absent());
    // Link with a didSet() override
    final ValueDownlink<Value> link = swimClient.downlinkValue()
        .hostUri(hostUri).nodeUri(nodeUri).laneUri("info")
        .didSet((newValue, oldValue) -> {
          System.out.println("link watched info change TO " + newValue + " FROM " + oldValue);
        })
        .open();
    // Send using either the proxy command lane...
    swimClient.command(hostUri, nodeUri, "publishInfo", Text.from("Hello from command, world!"));
    // ...or a downlink set()
    link.set(Text.from("Hello from link, world!"));
    System.out.println("synchronous link get: " + link.get());

    System.out.println("Will shut down client in 2 seconds");
    Thread.sleep(2000);
    swimClient.stop();
  }
}
