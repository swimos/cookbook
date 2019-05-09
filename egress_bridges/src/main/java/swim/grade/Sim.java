package swim.grade;

import swim.client.ClientRuntime;
import swim.structure.Record;

class Sim {

  public static void main(String[] args) throws InterruptedException {
    final ClientRuntime client = new ClientRuntime();
    client.start();
    while (true) {
      client.command("warp://localhost:9001",
          String.format("/student/%d", ((int) (Math.random() * 5)) + 1),
          "addAssignment",
          Record.create(3)
              .attr("assignment")
              .slot("earned", ((int) (Math.random() * 5)) + 15)
              .slot("possible", 20));
      Thread.sleep(500);
    }
  }
}
