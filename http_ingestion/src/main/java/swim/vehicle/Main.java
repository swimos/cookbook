package swim.vehicle;

import swim.kernel.Kernel;
import swim.server.ServerLoader;

public final class Main {

  private Main() {
  }

  public static void main(String[] args) {
    startServer();
  }

  private static void startServer() {
    final Kernel kernel = ServerLoader.loadServer();
    kernel.start();
    System.out.println("Running Http-ingesting server...");
    kernel.run();
  }

}
