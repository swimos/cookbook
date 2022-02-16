package swim.basic;

import swim.api.downlink.ValueDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;
import swim.structure.Text;
import swim.structure.Value;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/auth-policy/">Auth Policy</a> cookbook.
 * <p>
 * In this cookbook, a plane is created with an auth policy. Requests to the server will only be accepted if the token
 * provided has sufficient permissions.
 * <p>
 * See {@link BasicPlane}
 */
public class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    final ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();

    final String hostUri = "warp://localhost:9001";

    String nodeUri = "/control";
    String laneUri = "command";
    //Accessing the /control agent requires an admin token
    swimClient.command(hostUri, nodeUri, laneUri, Text.from("Command without a token"));
    swimClient.command(hostUri + "?token=aaa", nodeUri, laneUri, Text.from("Command with invalid token"));
    swimClient.command(hostUri + "?token=abc", nodeUri, laneUri, Text.from("Command with user token"));
    swimClient.command(hostUri + "?token=abc123", nodeUri, laneUri, Text.from("Command with admin token"));

    Thread.sleep(500); //Clearer logging

    nodeUri = "/unit";
    laneUri = "setInfo";
    //Accessing the setInfo lane requires a user or admin token
    swimClient.command(hostUri, nodeUri, laneUri, Text.from("Command without a token"));
    swimClient.command(hostUri + "?token=aaa", nodeUri, laneUri, Text.from("Command with invalid token"));
    swimClient.command(hostUri + "?token=abc", nodeUri, laneUri, Text.from("Command with user token"));
    swimClient.command(hostUri + "?token=abc123", nodeUri, laneUri, Text.from("Command with admin token"));

    Thread.sleep(500);

    laneUri = "adminInfo";
    //The adminInfo lane requires an admin token
    //The first link will fail as no token is supplied
    final ValueDownlink<Value> adminInfoDownlink = swimClient.downlinkValue()
            .valueForm(Form.forValue())
            .hostUri(hostUri)
            .nodeUri(nodeUri)
            .laneUri(laneUri)
            .didLink(() -> System.out.println("link to adminInfo successful with no token")) //This will not get printed as a token is required
            .open();
    adminInfoDownlink.set(Text.from("Setting adminInfo using link with no token"));

    Thread.sleep(500);

    //This link will be successful as an admin token has been used
    final ValueDownlink<Value> adminInfoDownlinkWithToken = swimClient.downlinkValue()
            .valueForm(Form.forValue())
            .hostUri(hostUri + "?token=abc123")
            .nodeUri(nodeUri)
            .laneUri(laneUri)
            .didLink(() -> System.out.println("link to adminInfo successful with token 'abc123'"))
            .open();
    adminInfoDownlinkWithToken.set(Text.from("Setting adminInfo using link with token abc123"));

    Thread.sleep(2000);
    swimClient.stop();
  }
}
