package swim.vehicle;

import java.net.http.HttpClient;

public final class Assets {

  private Assets() {
  }

  private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

  public static HttpClient httpClient() {
    return Assets.HTTP_CLIENT;
  }

}
