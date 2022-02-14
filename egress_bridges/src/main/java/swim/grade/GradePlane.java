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

import swim.actor.ActorSpace;
import swim.api.plane.AbstractPlane;
import swim.grade.db.BlockingStudentsDriver;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;

import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class GradePlane extends AbstractPlane {

  public static void main(String[] args)
      throws ClassNotFoundException, SQLException {

    // Attempt to start driver
    BlockingStudentsDriver.start("tcp://localhost:9002", "~/test", "sa", "");

    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("grade");
    kernel.start();
    System.out.println("Running Grade server...");
    kernel.run();

    // Immediately wake up EgressAgent
    space.command("/egress", "wakeup", Value.absent());

    Runtime.getRuntime().addShutdownHook(
        new Thread(() -> {
          System.out.println("Database sees:");
          BlockingStudentsDriver.logGrade(1);
          BlockingStudentsDriver.logGrade(2);
          BlockingStudentsDriver.logGrade(3);
          BlockingStudentsDriver.logGrade(4);
          BlockingStudentsDriver.logGrade(5);
          ForkJoinPool.commonPool().awaitQuiescence(1, TimeUnit.MINUTES);
        })

    );
  }
}
