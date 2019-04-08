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

    final MapDownlink<String, Integer> link = swimClient.downlinkMap()
        .keyForm(Form.forString()).valueForm(Form.forInteger())
        .hostUri(hostUri).nodeUri(nodeUriPrefix+"0").laneUri("shoppingCart")
        .open();

    // Each `shoppingCart` can be populated either via proxy command lane (see UnitAgent)...
    swimClient.command(hostUri, nodeUriPrefix+"0", "addItem", Text.from("FromClientCommand"));
    // ...or a direct downlink put().
    link.put("FromClientLink", 25);

    Thread.sleep(2000);

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
