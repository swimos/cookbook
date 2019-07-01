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
