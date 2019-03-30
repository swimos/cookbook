package swim.basic;

import java.io.IOException;
import swim.api.SwimRoute;
import swim.api.agent.AgentType;
import swim.api.plane.AbstractPlane;
import swim.api.plane.PlaneContext;
import swim.api.server.ServerContext;
import swim.loader.ServerLoader;
import swim.structure.Value;

public class BasicPlane extends AbstractPlane {

  @SwimRoute("/unit/:id")
  final AgentType<UnitAgent> unitAgentType = agentClass(UnitAgent.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    final ServerContext server = ServerLoader.load(BasicPlane.class.getModule()).serverContext();
    server.start();
    final PlaneContext plane = server.getPlane("basic").planeContext();
    server.run();
    
    plane.command("/unit/1", "unusedForNow", Value.absent());
    plane.command("/unit/foo", "unusedForNow", Value.absent());
    plane.command("/unit/foo_1", "unusedForNow", Value.absent());
    Thread.sleep(1000);

    System.out.println("Server will shut down in 3 seconds");
    Thread.sleep(3000);
    System.out.println("Sent shutdown signal to server");
    server.stop();
  }
}
