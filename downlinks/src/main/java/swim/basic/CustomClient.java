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
    final String nodeUriPrefix = "/unit/";

    // Write-only downlink; note keepLinked is false
    final MapDownlink<String, Integer> link = swimClient.downlinkMap()
        .keyForm(Form.forString()).valueForm(Form.forInteger())
        .hostUri(hostUri).nodeUri(nodeUriPrefix+"0").laneUri("shoppingCart")
        .keepLinked(false)
        .open();
    link.put("FromClientLink", 25);

    Thread.sleep(1000);
    // Henceforth, we will strictly use commands; let's close our link
    link.close();

    final String[] items = {"bat", "cat", "rat"};
    for (int i = 0; i < 50; i++) {
      // randomly add an item from `items` to the shopping carts of /unit/0, /unit/1, /unit/2 via commands
      swimClient.command(hostUri, nodeUriPrefix+(i%3), "addItem", Text.from(items[(int) (Math.random() * 3)]));
    }

    System.out.println("Will shut down client in 2 seconds");
    Thread.sleep(2000);
    swimClient.stop();
  }
}
