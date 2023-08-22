// Copyright 2015-2022 SWIM.AI inc.
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

package swim.aggregations;

import swim.actor.ActorSpace;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;

public class BasicPlane extends AbstractPlane {

  public static void main(String[] args) throws InterruptedException {
    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("aggregations");

    kernel.start();
    System.out.println("Running Basic server...");
    kernel.run();

    // Start 10 vehicles
    for (int i = 0; i < 10; i++) {
      space.command("/vehicle/" + i, "startVehicle", Value.absent());
      Thread.sleep(1000);
    }

    Thread.sleep(60000);

    kernel.stop();
  }

}