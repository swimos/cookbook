package swim.vehicle;

import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public final class Assets {

  private Assets() {
  }

  private static KafkaConsumer<String, String> kafkaConsumer;

  public static KafkaConsumer<String, String> kafkaConsumer() {
    return Assets.kafkaConsumer;
  }

  private static KafkaConsumer<String, String> loadKafkaConsumer() {
    final Properties props = new Properties();
    props.setProperty("bootstrap.servers", "your-bootstrap-host:9092");
    props.setProperty("group.id", "your-group");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    // Alternatively, load above from a .properties file
    return new KafkaConsumer<>(props);
  }

  public static void init() {
    Assets.kafkaConsumer = loadKafkaConsumer();
  }

}
