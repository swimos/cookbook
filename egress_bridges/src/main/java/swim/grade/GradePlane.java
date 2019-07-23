// Copyright 2015-2019 SWIM.AI inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package swim.grade;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.plane.AbstractPlane;
import swim.fabric.Fabric;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;

public class GradePlane extends AbstractPlane {

  @SwimRoute("/student/:id")
  AgentRoute<StudentAgent> unitAgentType;

  @SwimRoute("/egress")
  AgentRoute<EgressAgent> egressAgentType;

  public static void main(String[] args) {

    // Attempt to start CustomDriver
    CustomDriver.start("tcp://localhost:9002", "~/test", "sa", "");

    final Kernel kernel = ServerLoader.loadServer();
    final Fabric fabric = (Fabric) kernel.getSpace("grade");

    kernel.start();
    System.out.println("Running Basic server...");
    kernel.run();

    // Immediately wake up EgressAgent
    fabric.command("/egress", "wakeup", Value.absent());

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
