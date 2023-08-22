package swim.tower;

import swim.api.space.Space;
import swim.structure.Record;
import swim.structure.Value;

public final class Simulation {

  private Simulation() {
  }

  private static void simulateOnce(Space space, String id, double center, int spread,
                                   long now) {
    final double val = center + (Math.random() * spread) - (spread / 2.0);
    // 9% chance for 2, 21% for 1, 70% for 0
    final int failures = Math.random() < 0.3 ? Math.random() < 0.3 ? 2 : 1 : 0;
    final String[] nodeUris = {"/tower/" + id, "/towerB/" + id, "/towerW/" + id};
    final Value payload = Record.create(3).slot("s_n_ratio", val)
        .slot("disconnects", failures)
        .slot("timestamp", now);
    for (String nodeUri : nodeUris) {
      space.command(nodeUri, "addMessage", payload);
    }
  }

  public static void run(Space space) {
    // infinite loop
    while (true) {
      final long now = System.currentTimeMillis();
      simulateOnce(space, "2350", 18.0, 15, now);
      simulateOnce(space, "2171", 27, 6, now);
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        // impossible
      }
    }
  }

}
