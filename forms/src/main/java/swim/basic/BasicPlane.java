package swim.basic;

import swim.actor.ActorSpace;
import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;

public class BasicPlane extends AbstractPlane {

  @SwimRoute("/unit/:id")
  AgentRoute<UnitAgent> unitAgentType;

  public static void main(String[] args) {
    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("basic");

    kernel.start();
    System.out.println("Running Basic server...");
    kernel.run();

//    space.command("/unit/foo", "wakeup", Value.absent());
    // TODO: add project-relevant commands
  }
}
