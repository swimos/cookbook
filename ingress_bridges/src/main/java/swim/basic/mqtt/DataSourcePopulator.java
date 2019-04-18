package swim.basic.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class DataSourcePopulator {

  public final MqttClient mqtt;

  public DataSourcePopulator(String broker) throws MqttException {
    this.mqtt = new MqttClient(broker, "Writer");
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);
    System.out.println("Populator connecting to " + broker);
    mqtt.connect(connOpts);
    System.out.println("Populator connected!");
  }

  public void populate() {
    final int qos = 1;
    final String topic = "swimSensors/all";
    int count = 0;
    while (true) {
      for (int i = 0; i < 10; i++) {
        final String content = String.format("@msg{id:%d,val:FromPopulator%d}", i, count++);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        try {
          mqtt.publish(topic, message);
          // Don't lower this value unless you use a personal MQTT broker, or
          // you will get rate-limited!!
          Thread.sleep(10000);
        } catch (MqttException | InterruptedException e) {
          System.out.println("Publication error");
          e.printStackTrace();
          return;
        }
      }
    }
  }

  public static void main(String[] args) throws MqttException {
    final DataSourcePopulator pop = new DataSourcePopulator("tcp://iot.eclipse.org:1883");
    pop.populate();
  }
}
