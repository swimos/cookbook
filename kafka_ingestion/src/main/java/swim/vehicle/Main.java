package swim.vehicle;

import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import swim.kernel.Kernel;
import swim.server.ServerLoader;

public final class Main {

  private Main() {
  }

  private static KafkaConsumer<String, String> kafkaConsumer0;

  public static KafkaConsumer<String, String> kafkaConsumer0() {
    return Main.kafkaConsumer0;
  }

  private static KafkaConsumer<String, String> loadKafkaConsumer() {
    // Use code (demonstrated here) or a .properties file
    final Properties props = new Properties();
    props.setProperty("bootstrap.servers", "your-bootstrap-host:9092");
    props.setProperty("group.id", "your-group");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

    return new KafkaConsumer<>(props);
  }

  private static void startServer() {
    final Kernel kernel = ServerLoader.loadServer();
    kernel.start();
    System.out.println("Running Kafka-ingesting server...");
    kernel.run();
  }

  public static void main(String[] args) {
    Main.kafkaConsumer0 = loadKafkaConsumer();
    startServer();
  }

}
