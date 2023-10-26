package swim.vehicle;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

public final class Assets {

  private Assets() {
  }

  private static ConnectionFactory connectionFactory;

  public static ConnectionFactory connectionFactory() {
    return Assets.connectionFactory;
  }

  private static ConnectionFactory loadConnectionFactory() {
    // Here we can configure the ConnectionFactory with additional settings - perhaps loaded from a properties file
    return new ActiveMQConnectionFactory("tcp://activemq:61616");
  }

  public static void init() {
    Assets.connectionFactory = loadConnectionFactory();
  }

}
