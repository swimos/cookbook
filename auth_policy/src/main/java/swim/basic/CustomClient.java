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
        final String nodeUri = "/unit";

        //Accessing the setInfo lane requires a token
        swimClient.command(hostUri, nodeUri, "setInfo", Text.from("Command without a token"));
        swimClient.command(hostUri + "?token=aaa", nodeUri, "setInfo", Text.from("Command with invalid token"));
        swimClient.command(hostUri + "?token=abc", nodeUri, "setInfo", Text.from("Command with valid token"));

        Thread.sleep(500); //Clearer logging

        //The adminInfo lane requires specifically token 'abc123'
        //The first link will fail as no token is supplied
        final ValueDownlink<Value> adminInfoDownlink = swimClient.downlinkValue()
                .valueForm(Form.forValue())
                .hostUri(hostUri)
                .nodeUri(nodeUri)
                .laneUri("adminInfo")
                .didLink(() -> System.out.println("link to adminInfo successful with no token")) //This will not get printed as a token is required
                .open();
        adminInfoDownlink.set(Text.from("Setting adminInfo using link with no token"));

        Thread.sleep(500);

        //This link will be successful as an admin token has been used
        final ValueDownlink<Value> adminInfoDownlinkWithToken = swimClient.downlinkValue()
                .valueForm(Form.forValue())
                .hostUri(hostUri + "?token=abc123")
                .nodeUri(nodeUri)
                .laneUri("adminInfo")
                .didLink(() -> System.out.println("link to adminInfo successful with token 'abc123'"))
                .open();
        adminInfoDownlinkWithToken.set(Text.from("Setting adminInfo using link with token abc123"));

        Thread.sleep(2000);
        swimClient.stop();
    }
}
