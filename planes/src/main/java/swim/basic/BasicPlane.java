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
import swim.api.agent.AgentRoute;
import swim.api.auth.Identity;
import swim.api.plane.AbstractPlane;
import swim.api.policy.AbstractPolicy;
import swim.api.policy.PolicyDirective;
import swim.fabric.Fabric;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
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
  AgentRoute<UnitAgent> unitAgentType;

  // Inject policy. Swim internally calls the no-argument constructor, which retains
  // its implicit call to super() in Java
  public BasicPlane() {
    context.setPolicy(new BasicPolicy());
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
    final Kernel kernel = ServerLoader.loadServer();
    final Fabric fabric = (Fabric) kernel.getSpace("basic");

    kernel.start();
    System.out.println("Running Basic server...");
    kernel.run();

    // observe the effects of our commands
    fabric.downlinkValue()
      .nodeUri("/unit/master")
      .laneUri("info")
      .didSet((newValue, oldValue) -> {
        System.out.println("observed info change to " + newValue + " from " + oldValue);
      })
      .open();
    // Swim handles don't reject their own messages, regardless of policy
    fabric.command("/unit/master", "publishInfo", Text.from("Without network"));
    // Network events without tokens get rejected
    fabric.command("warp://localhost:9001", "/unit/master", "publishInfo", Text.from("With network, no token"));
    // Network events with the right token are accepted
    fabric.command("warp://localhost:9001?token=abcd", "/unit/master", "publishInfo", Text.from("With network, token"));
  }
}
