package swim.vehicle;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import swim.api.agent.AbstractAgent;
import swim.json.Json;
import swim.structure.Value;

public class JmsAgent extends AbstractAgent {

  private void subscribe() {
    try {
      // Create a connection
      final Connection connection = Assets.getOrCreateConnection();
      connection.start();

      // Create a session
      final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

      // Create a message consumer and process messages
      final MessageConsumer consumer = session.createConsumer(session.createTopic("myTopic"));
      consumer.setMessageListener(this::processMessage);
    } catch (JMSException jmsException) {
      jmsException.printStackTrace();
    }
  }

  private void processMessage(final Message message) {
    try {
      final Value body = Json.parse(((TextMessage) message).getText());
      final String nodeUri = "/vehicle/" + body.get("id").longValue();
      command(nodeUri, "addMessage", body);
    } catch (JMSException jmsException) {
      jmsException.printStackTrace();
    }
  }

  @Override
  public void didStart() {
    asyncStage().task(this::subscribe).cue();
  }

}

