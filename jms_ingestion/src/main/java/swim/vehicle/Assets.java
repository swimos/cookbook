package swim.vehicle;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import org.apache.activemq.ActiveMQConnectionFactory;

public final class Assets {

  private Assets() {
  }

  private static ConnectionFactory connectionFactory;
  private static Connection connection;

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

  public static Connection getOrCreateConnection() throws JMSException {
    if (Assets.connection == null) {
      Assets.connection = Assets.connectionFactory.createConnection();
    }
    return Assets.connection;
  }

}
