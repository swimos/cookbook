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

  public static void main(String[] args) throws IOException {
    final ServerContext server = ServerLoader.load(BasicPlane.class.getModule()).serverContext();
    server.start();
    final PlaneContext plane = server.getPlane("basic").planeContext();
    server.run();

    // A Web Agent won't run unless its URI is invoked for the first time.
    // In the MQTT demo, an external process does this, so the upcoming command
    // is unnecessary. In the WARP demo, the data flow from `SourcePlane` to
    // `BasicPlane` is strictly pull-based, and it happens in a Web Agent; we
    // solve this chicken-and-egg problem by jump-starting one `UnitAgent`.
    plane.command("/unit/0", "wakeup", Value.absent());
  }
}
