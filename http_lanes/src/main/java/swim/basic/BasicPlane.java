package swim.basic;

import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;

public class BasicPlane extends AbstractPlane {

  public BasicPlane() {
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
    //Immediately wake up the Unit Agent on plane load
    command("/unit", "wakeup", Value.absent());
  }

}
