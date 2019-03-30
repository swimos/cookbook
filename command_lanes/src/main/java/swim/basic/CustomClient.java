package swim.basic;

import swim.api.downlink.EventDownlink;
import swim.client.ClientRuntime;
import swim.structure.Num;
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

    final EventDownlink<Value> link = swimClient.downlink()
        .hostUri(hostUri).nodeUri(nodeUri).laneUri("publishValue")
        .onEvent((Value event) -> {
          System.out.println("link received event: " + event);
        })
        .open();
    final Value msg = Num.from(9035768);
    // command() `msg` TO
    // the "publish" lane OF
    // the agent addressable by `/unit/foo` RUNNING ON
    // the plane with hostUri "warp://localhost:9001"    
    swimClient.command(hostUri, nodeUri, "publish", msg);
    Thread.sleep(2000);
    swimClient.stop();
  }
}
