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

package swim.grade;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;

public class StudentAgent extends AbstractAgent {

  @SwimLane("addAssignment")
  CommandLane<Value> publish = this.<Value>commandLane()
      .onCommand(msg -> {
        final Value current = this.grade.get();
        this.grade.set(Record.create(2)
            .slot("earned", current.get("earned").intValue() + msg.get("earned").intValue())
            .slot("possible", current.get("possible").intValue() + msg.get("possible").intValue()));
      });
  private int id;
  @SwimLane("currentGrade")
  ValueLane<Value> grade = this.<Value>valueLane()
      .didSet((n, o) -> {
        logMessage("changed grade to " + Recon.toString(n));
        CustomDriver.updateGrade(id, n.get("earned").intValue(), n.get("possible").intValue());
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

  @Override
  public void didStart() {
    try {
      this.id = getProp("id").intValue();
    } catch (UnsupportedOperationException e) {
      logMessage("startup error");
      e.printStackTrace();
      close();
    }
    this.grade.set(Record.create(2).slot("earned", 0).slot("possible", 0));
  }
}
