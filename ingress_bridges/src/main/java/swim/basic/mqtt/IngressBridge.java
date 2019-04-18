package swim.basic.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import swim.client.ClientRuntime;
import swim.recon.Recon;
import swim.structure.Value;

public class IngressBridge {

  private final ClientRuntime swim;
  private final MqttAsyncClient mqtt;

  public IngressBridge(final String swimHost, final String broker) throws MqttException {
    this.swim = new ClientRuntime();
    this.swim.start();

    this.mqtt = new MqttAsyncClient(broker, "Listener");
    this.mqtt.setCallback(new MqttCallback() {
      @Override
      public void connectionLost(Throwable cause) {
        System.err.println("connection lost");
      }

      @Override
      public void messageArrived(String topic, MqttMessage message) throws Exception {
        final String msg = new String(message.getPayload());
        System.out.printf("MQTT Ingress Bridge received '%s'\n", msg);
        final Value structure = Recon.parse(msg);
        final String nodeUri = String.format("/unit/%d", structure.get("id").intValue());
        IngressBridge.this.swim.command(
            swimHost, // hostUri
            nodeUri, // nodeUri
            "publish", // laneUri
            structure.get("val") // value
          );
      }

      @Override
      public void deliveryComplete(IMqttDeliveryToken token) { }
    });
  }

  public void listen() throws MqttException {
    MqttConnectOptions connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);
    this.mqtt.connect(connOpts);
    // Spin until connected
    while (!this.mqtt.isConnected()) { }
    System.out.println("Connected!");
    this.mqtt.subscribe("swimSensors/all", 1);
  }

  public static void main(String[] args) throws MqttException {
    final IngressBridge lis = new IngressBridge("warp://localhost:9001", "tcp://iot.eclipse.org:1883");
    lis.listen();
  }
}