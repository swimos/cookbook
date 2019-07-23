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

package swim.basic.warp;

import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.plane.AbstractPlane;
import swim.fabric.Fabric;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Text;

public class SourcePlane extends AbstractPlane {

  @SwimRoute("/source/:id")
  AgentRoute<SourceAgent> sourceAgentType;

  public static void main(String[] args) throws InterruptedException {
    System.setProperty("swim.config", "source.recon");

    final Kernel kernel = ServerLoader.loadServer();
    final Fabric fabric = (Fabric) kernel.getSpace("source");

    kernel.start();
    System.out.println("Running Source server...");
    kernel.run();

    int count = 0;
    while (true) {
      for (int i = 0; i < 10; i++) {
        fabric.command("/source/"+i, "val",
            Text.from("FromOtherSwimServer"+(count++)));
        Thread.sleep(100);
      }
    }
  }
}
