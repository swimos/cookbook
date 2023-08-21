package swim.tower;

import swim.api.space.Space;
import swim.kernel.Kernel;
import swim.server.ServerLoader;

public final class Main {

  public static void main(String[] args) {
    final Space space = startServer();
    // Occupies main thread until process termination
    Simulation.run(space);
  }

  private static Space startServer() {
    final Kernel kernel = ServerLoader.loadServer();
    final Space space = kernel.getSpace("tower");
    kernel.start();
    System.out.println("Running summary statistics guide...");
    kernel.run();
    return space;
  }

}
