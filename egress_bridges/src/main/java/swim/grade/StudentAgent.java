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

package swim.grade;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.concurrent.AbstractTask;
import swim.grade.db.BlockingStudentsDriver;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;

public class StudentAgent extends AbstractAgent {

  private int id() {
    return getProp("id").intValue();
  }

  @SwimLane("addAssignment")
  CommandLane<Value> publish = this.<Value>commandLane()
      .onCommand(msg -> {
        final Value current = this.grade.get();
        this.grade.set(Record.create(2)
            .slot("earned", current.get("earned").intValue() + msg.get("earned").intValue())
            .slot("possible", current.get("possible").intValue() + msg.get("possible").intValue()));
      });

  @SwimLane("currentGrade")
  ValueLane<Value> grade = this.<Value>valueLane()
      .didSet((n, o) -> {
        logMessage("changed grade to " + Recon.toString(n));
        // Don't naively make a database call. Delegate it to asyncStage() to
        // avoid potentially-blocking functions within the agent context.
        asyncStage().task(new AbstractTask() {
            @Override
            public void runTask() {
              BlockingStudentsDriver.updateGrade(id(), n.get("earned").intValue(), n.get("possible").intValue());
            }

            @Override
            public boolean taskWillBlock() {
              return true;
            }
          })
          .cue();
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

  @Override
  public void didStart() {
    this.grade.set(Record.create(2).slot("earned", 0).slot("possible", 0));
  }

}
