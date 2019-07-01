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
import swim.recon.Recon;
import swim.structure.Value;

public class BasicPlane extends AbstractPlane {

  @SwimRoute("/listener")
  final AgentType<ListenerAgent> listenerAgentType = agentClass(ListenerAgent.class);

  @SwimRoute("/unit/:id")
  final AgentType<UnitAgent> unitAgentType = agentClass(UnitAgent.class);

  @Override
  public void didStart() {
    super.didStart();
    // Immediately wake up ListenerAgent upon plane load
    context.command("/listener", "wakeup", Value.absent());
  }

  public static void main(String[] args) throws IOException, InterruptedException {
    final ServerContext server = ServerLoader.load(BasicPlane.class.getModule()).serverContext();
    server.start();
    final PlaneContext plane = server.getPlane("basic").planeContext();
    server.run();

    // Event downlink issued against plane context
    plane.downlink()
        .nodeUri("/unit/0")
        .laneUri("addItem")
        .onEvent(v -> {
          System.out.println("event downlink saw " + Recon.toString(v));
        })
        .open();
  }
}
