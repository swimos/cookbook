package swim.vehicle;

import java.net.http.HttpClient;
import swim.kernel.Kernel;
import swim.server.ServerLoader;

public final class Main {

  private Main() {
  }

  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

  public static HttpClient httpClient() {
    return HTTP_CLIENT;
  }

  private static void startServer() {
    final Kernel kernel = ServerLoader.loadServer();
    kernel.start();
    System.out.println("Running Http-ingesting server...");
    kernel.run();
  }

  public static void main(String[] args) {
    startServer();
  }

}
