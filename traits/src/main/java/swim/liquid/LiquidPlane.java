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

package swim.liquid;

import java.util.Random;
import swim.actor.ActorSpace;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Value;
import swim.uri.Uri;

public class LiquidPlane extends AbstractPlane {

  public static void main(String[] args) throws InterruptedException {
    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("liquid");

    kernel.start();
    System.out.println("Running Basic server...");
    kernel.run();

    // Dynamic Agent
    int n = 0;
    Random rand = new Random();
    while (n < 4) {
      int rand_int = rand.nextInt(1000);
      String node = "/liquid/" + rand_int;
      space.command(node, "sharedDynInfo", Value.absent());
      n++;
      Thread.sleep(5000);
    }

    System.out.println("Server will shut down in 3 seconds");
    Thread.sleep(3000);
    System.out.println("Sent shutdown signal to server");
    kernel.stop();
  }
}
