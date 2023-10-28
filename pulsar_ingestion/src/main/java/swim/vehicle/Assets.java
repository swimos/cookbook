package swim.vehicle;

import java.util.Map;
import org.apache.pulsar.client.api.PulsarClient;

public final class Assets {

  private Assets() {
  }

  private static PulsarClient client;

  public static PulsarClient pulsarClient() {
    return Assets.client;
  }

  private static PulsarClient loadPulsarClient() {
    final Map<String, Object> config = Map.ofEntries(
        Map.entry("serviceUrl", "pulsar://localhost:6650"),
        Map.entry("numListenerThreads", 1)
    );
    try {
      return PulsarClient.builder()
          .loadConf(config)
          .build();
    } catch (Exception e) {
      throw new RuntimeException("Failed to load Pulsar client", e);
    }
  }

  public static void init() {
    Assets.client = loadPulsarClient();
  }

}
