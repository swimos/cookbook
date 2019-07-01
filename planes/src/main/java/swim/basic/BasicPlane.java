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
import swim.api.auth.Identity;
import swim.api.plane.AbstractPlane;
import swim.api.plane.PlaneContext;
import swim.api.policy.AbstractPolicy;
import swim.api.policy.PolicyDirective;
import swim.api.server.ServerContext;
import swim.loader.ServerLoader;
import swim.structure.Text;
import swim.structure.Value;
import swim.warp.Envelope;

public class BasicPlane extends AbstractPlane {
  // Define policy; doesn't have to be an inner class
  class BasicPolicy extends AbstractPolicy {
    @Override
    protected <T> PolicyDirective<T> authorize(Envelope envelope, Identity identity) {
      if (identity != null) {
        final String token = identity.requestUri().query().get("token");
        if ("abcd".equals(token)) {
          return allow();
        }
      }
      return forbid();
    }
  }

  @SwimRoute("/unit/:id")
  final AgentType<UnitAgent> unitAgentType = agentClass(UnitAgent.class);

  // Inject policy. Swim internally calls the no-argument constructor, which retains
  // its implicit call to super() in Java
  public BasicPlane() {
    context.setPlanePolicy(new BasicPolicy());
  }

  @Override
  public void didStart() {
    context.command("/unit/master", "WAKEUP", Value.absent());
  }

  @Override
  public void willStop() {
    System.out.println("Shutdown in progress...");
  }

  public static void main(String[] args) throws IOException {
    final ServerContext server = ServerLoader.load(BasicPlane.class.getModule()).serverContext();
    server.start();
    final PlaneContext plane = server.getPlane("basic").planeContext();
    server.run();
    // observe the effects of our commands
    plane.downlinkValue()
      .nodeUri("/unit/master")
      .laneUri("info")
      .didSet((newValue, oldValue) -> {
        System.out.println("observed info change to " + newValue + " from " + oldValue);
      })
      .open();
    // Swim handles don't reject their own messages, regardless of policy
    plane.command("/unit/master", "publishInfo", Text.from("Without network"));
    // Network events without tokens get rejected
    plane.command("warp://localhost:9001", "/unit/master", "publishInfo", Text.from("With network, no token"));
    // Network events with the right token are accepted
    plane.command("warp://localhost:9001?token=abcd", "/unit/master", "publishInfo", Text.from("With network, token"));
  }
}
