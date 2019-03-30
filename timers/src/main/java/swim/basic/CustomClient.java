package swim.basic;

import swim.client.ClientRuntime;
import swim.structure.Value;

class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();
    final String hostUri = "warp://localhost:9001";
    final String nodeUri = "/unit/foo";
    for (int i = 0; i < 10; i++) {
      swimClient.command(hostUri, nodeUri, "publish", Value.absent());
      Thread.sleep(5000 * i);
    }
    System.out.println("Will shut down client");
    swimClient.stop();
  }
}
