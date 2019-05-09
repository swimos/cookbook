package swim.grade;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import swim.api.SwimRoute;
import swim.api.agent.AgentType;
import swim.api.plane.AbstractPlane;
import swim.api.plane.PlaneContext;
import swim.api.server.ServerContext;
import swim.loader.ServerLoader;
import swim.structure.Value;

public class GradePlane extends AbstractPlane {

  @SwimRoute("/student/:id")
  final AgentType<StudentAgent> unitAgentType = agentClass(StudentAgent.class);

  @SwimRoute("/egress")
  final AgentType<EgressAgent> egressAgentType = agentClass(EgressAgent.class);

  public static void main(String[] args) throws IOException {

    // Attempt to start CustomDriver
    CustomDriver.start("tcp://localhost:9002", "~/test", "sa", "");

    final ServerContext server = ServerLoader.load(GradePlane.class.getModule()).serverContext();
    server.start();
    final PlaneContext plane = server.getPlane("grade").planeContext();
    server.run();

    // Immediately wake up EgressAgent
    plane.command("/egress", "wakeup", Value.absent());

    Runtime.getRuntime().addShutdownHook(
        new Thread(() -> {
          System.out.println("Database sees:");
          CustomDriver.checkGrade(1);
          CustomDriver.checkGrade(2);
          CustomDriver.checkGrade(3);
          CustomDriver.checkGrade(4);
          CustomDriver.checkGrade(5);
          ForkJoinPool.commonPool().awaitQuiescence(1, TimeUnit.MINUTES);
        })

      );
  }
}
