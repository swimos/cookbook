// Copyright 2015-2023 Nstream, inc.
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
import swim.actor.ActorSpace;
import swim.api.plane.AbstractPlane;
import swim.kernel.Kernel;
import swim.recon.Recon;
import swim.server.ServerLoader;
import swim.structure.Value;

public class BasicPlane extends AbstractPlane {

  public static void main(String[] args) throws IOException, InterruptedException {
    final Kernel kernel = ServerLoader.loadServer();
    final ActorSpace space = (ActorSpace) kernel.getSpace("basic");
    kernel.start();
    System.out.println("Running Basic server...");
    kernel.run();

    // Event downlink issued against plane context
    space.downlink()
        .nodeUri("/unit/0")
        .laneUri("addItem")
        .onEvent(v -> {
          System.out.println("event downlink saw " + Recon.toString(v));
        })
        .open();
  }

  @Override
  public void didStart() {
    super.didStart();
    // Immediately wake up ListenerAgent upon plane load
    context.command("/listener", "wakeup", Value.absent());
  }

}
