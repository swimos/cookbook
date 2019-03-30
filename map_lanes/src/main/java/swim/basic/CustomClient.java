package swim.basic;

import swim.api.downlink.MapDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;
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
    final MapDownlink<String, Integer> link = swimClient.downlinkMap()
        .keyForm(Form.forString()).valueForm(Form.forInteger())
        .hostUri(hostUri).nodeUri(nodeUri).laneUri("shoppingCart")
        .didUpdate((key, newValue, oldValue) -> {
          System.out.println("link watched " + key + " change to " + newValue + " from " + oldValue);
        })
        .open();    
    // Send using either the proxy command lane...
    swimClient.command(hostUri, nodeUri, "addItem", Text.from("FromClientCommand"));
    // ...or a downlink put()
    link.put("FromClientLink", 25);
    Thread.sleep(2000);
    link.remove("FromClientLink");

    System.out.println("Will shut down client in 2 seconds");
    Thread.sleep(2000);
    swimClient.stop();
  }
}
