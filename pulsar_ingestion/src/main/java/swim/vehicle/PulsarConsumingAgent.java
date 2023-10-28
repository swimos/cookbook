package swim.vehicle;

import java.util.Map;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import swim.api.agent.AbstractAgent;

public class PulsarConsumingAgent extends AbstractAgent {

  private Consumer<String> pulsarConsumer; // or other type parameter

  private Consumer<String> loadPulsarConsumer(PulsarClient client) {
    final Map<String, Object> config = Map.ofEntries(
        Map.entry("topicNames", "myTopic"),
        Map.entry("subscriptionName", "mySubscription")
    );
    try {
      return client.newConsumer(Schema.STRING)
          .loadConf(config)
          .messageListener((c, m) -> {
            asyncStage().execute(() -> {
              // TODO: take an action on m
            });
          })
          .subscribe();
    } catch (Exception e) {
      throw new RuntimeException("Failed to load Pulsar consumer", e);
    }
  }

  @Override
  public void didStart() {
    this.pulsarConsumer = loadPulsarConsumer(Assets.pulsarClient());
  }

}
