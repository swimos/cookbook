package swim.vehicle;

import swim.kernel.Kernel;
import swim.server.ServerLoader;

public final class Main {

  private Main() {
  }

  public static void main(String[] args) {
    Assets.init();
    startServer();
  }

  private static void startServer() {
    final Kernel kernel = ServerLoader.loadServer();
    kernel.start();
    System.out.println("Running MongoDB-ingesting server...");
    kernel.run();
  }

}
