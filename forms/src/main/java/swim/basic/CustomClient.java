class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();
    final String hostUri = "warp://localhost:9001";
    final String nodeUri = "/unit/foo";
    for (int i = 0; i < 10; i++) {
      swimClient.command(hostUri, nodeUri, "publish", Value.absent());
      Thread.sleep(5000 * i);
    }
    System.out.println("Will shut down client");
    swimClient.stop();
  }
}

// TODO: replace the above boilerplate code with relevant code that is cookbook specific

// TODO: from git issue
//Commands the addFoo lane of UnitAgent with some Record that is compatible with FooType. With the example, and assuming the class was annotated with @Tag("foo"), such a Record might look like Record.create(3).attr("foo").slot("i", 5).slot("s", "potato").
//Commands the addBar lane of UnitAgent with the result of (Value) BarType.form().mold(someBarTypeInstance).
//OPTIONAL: commands the addValue lane of UnitAgent with both of the aforementioned messages.