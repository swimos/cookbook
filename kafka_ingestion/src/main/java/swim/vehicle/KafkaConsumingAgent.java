package swim.vehicle;

import java.time.Duration;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import swim.api.agent.AbstractAgent;
import swim.concurrent.AbstractTask;
import swim.concurrent.TaskRef;
import swim.json.Json;
import swim.structure.Value;

public class KafkaConsumingAgent extends AbstractAgent {

  private final TaskRef endlessConsumingTask = asyncStage().task(new AbstractTask() {

        @Override
        public void runTask() {
          while (true) {
            final ConsumerRecords<String, String> records = Main.kafkaConsumer0()
                .poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
              final String nodeUri = "/vehicle/" + record.key();
              final Value payload = Json.parse(record.value());
              command(nodeUri, "addMessage", payload);
            }
          }
        }

        @Override
        public boolean taskWillBlock() {
          return true;
        }

      });

  @Override
  public void didStart() {
    this.endlessConsumingTask.cue();
  }

}
