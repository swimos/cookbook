package swim.basic;

import swim.client.ClientRuntime;
import swim.structure.Record;
import swim.structure.Value;

class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();
    final String hostUri = "warp://localhost:9001";
    final String fooNodeUri = "/unit/foo";
    final String barNodeUri = "/unit/bar";

    Value fooVal = Record.create(3).attr("foo").slot("i", 5).slot("s", "potato");

    BarType barObj = new BarType(1, "two", 3);
    Value barVal = (Value) BarType.form().mold(barObj);

    for (int i = 0; i < 10; i++) {
      // Commands the addFoo lane of UnitAgent with some Record that is compatible with FooType.
      swimClient.command(hostUri, fooNodeUri, "addFoo", fooVal);

      // Commands the addBar lane of UnitAgent with BarType molded into type Value
      swimClient.command(hostUri, barNodeUri, "addBar", barVal);

      // Commands the addValue lane of UnitAgent with both of the aforementioned messages.
      swimClient.command(hostUri, fooNodeUri, "addValue", fooVal);
      swimClient.command(hostUri, barNodeUri, "addValue", barVal);

      Thread.sleep(5000 * i);
    }
    System.out.println("Will shut down client");
    swimClient.stop();
  }
}