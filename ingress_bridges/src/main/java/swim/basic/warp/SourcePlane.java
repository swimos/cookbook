package swim.basic.warp;

import java.io.IOException;
import swim.api.SwimRoute;
import swim.api.agent.AgentType;
import swim.api.plane.AbstractPlane;
import swim.api.plane.PlaneContext;
import swim.api.server.ServerContext;
import swim.loader.ServerLoader;
import swim.structure.Text;

public class SourcePlane extends AbstractPlane {

  @SwimRoute("/source/:id")
  final AgentType<SourceAgent> sourceAgentType = agentClass(SourceAgent.class);

  public static void main(String[] args) throws IOException, InterruptedException {
    System.setProperty("swim.config", "/source.recon");
    final ServerContext server = ServerLoader.load(SourcePlane.class.getModule()).serverContext();
    server.start();
    final PlaneContext plane = server.getPlane("source").planeContext();
    server.run();
    int count = 0;
    while (true) {
      for (int i = 0; i < 10; i++) {
        plane.command("/source/"+i, "val",
            Text.from("FromOtherSwimServer"+(count++)));
        Thread.sleep(100);
      }
    }
  }
}
