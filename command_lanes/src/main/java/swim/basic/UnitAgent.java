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

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;
import swim.uri.Uri;

public class UnitAgent extends AbstractAgent {

  @SwimLane("publish")
  CommandLane<Integer> publish = this.<Integer>commandLane()
      .onCommand((Integer msg) -> {
        logMessage("`publish` commanded with " + msg);
        final Value updatedMsg = Record.create(1).slot("fromServer", msg);
        // command() `updatedMsg` TO
        // the "publishValue" lane OF
        // the agent addressable by `nodeUri()` RUNNING ON
        // this plane (indicated by no hostUri argument)
        command(nodeUri(), Uri.parse("publishValue"), updatedMsg);
      });

  @SwimLane("publishValue")
  CommandLane<Value> publishV = this.<Value>commandLane()
      .onCommand((Value msg) -> {
        logMessage("`publishValue` commanded with " + Recon.toString(msg));
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
