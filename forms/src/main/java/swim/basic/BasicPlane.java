package swim.basic;

import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.plane.AbstractPlane;
import swim.api.space.Space;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;

public class BasicPlane extends AbstractPlane {

  @SwimRoute("/unit/:id")
  AgentRoute<UnitAgent> unitAgentType;

  public static void pojoTransformations() {
    // FooType instance fooType
    FooType fooType = new FooType(7, "cat");

    // BarType instance barType
    BarType barType = new BarType(4, "dog", 2);

    Value fooVal = (Value) FooType.form().mold(fooType);
    Value barVal = (Value) BarType.form().mold(barType);

    System.out.println(FooType.form().cast(fooVal));
    System.out.println(BarType.form().cast(barVal));

    // The following two statements return null. Why do you think that is?
    System.out.println(FooType.form().cast(barVal));
    System.out.println(BarType.form().cast(fooVal));

    // Consider why the following would fail to compile:
    // FooType.form().mold(barType);
  }

  public static void runServer() {
    final Kernel kernel = ServerLoader.loadServer();
    final Space space = kernel.getSpace("basic");

    kernel.start();
    System.out.println("Running Basic server...");
    space.command("/unit/foo", "wakeup", Value.absent());
    kernel.run();
  }

  public static void main(String[] args) {
    // Switch the flag to tue to run the server only once you understand what happens in pojoTransformations()
    boolean willRunServer = false;

    pojoTransformations();

    if (willRunServer) {
      runServer();
    }
  }
}
