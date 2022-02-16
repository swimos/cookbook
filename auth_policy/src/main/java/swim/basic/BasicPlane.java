package swim.basic;

import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;

/**
 * The complimentary code as part of the <a href="https://swimos.org/tutorials/auth-policy/">Auth Policy</a> cookbook.
 * <p>
 * In this cookbook, a plane is created with an auth policy. Requests to the server will only be accepted if the token
 * provided has sufficient permissions.
 * <p>
 * See {@link CustomClient}
 */
public class BasicPlane extends AbstractPlane {

  public BasicPlane() {
    context.setPolicy(new BasicPolicy());
  }

  public static void main(String[] args) {
    final Kernel kernel = ServerLoader.loadServer();

    System.out.println("Starting server...");
    kernel.start();
    kernel.run();
  }

  @Override
  public void didStart() {
    super.didStart();
    command("/unit", "WAKEUP", Value.absent());
    command("/control", "WAKEUP", Value.absent());
  }

}
