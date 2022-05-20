// Copyright 2015-2022 SWIM.AI inc.
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
import swim.api.lane.MapLane;
import swim.api.lane.ValueLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;

public class PostAgent extends AbstractAgent {

  @SwimLane("info")
  ValueLane<Value> info = this.valueLane();

  @SwimLane("history")
  MapLane<Long, Value> history = this.<Long, Value>mapLane()
      .didUpdate((k, n, o) -> {
        System.out.println(nodeUri() + ": added comment at " + k + ": " + Recon.toString(n));
      });

  @SwimLane("addComment")
  CommandLane<Value> addComment = this.<Value>commandLane()
      .onCommand(v -> {
        if (!this.info.get().isDistinct()) {
          this.info.set(Record.create(3)
              .slot("link_id", v.get("link_id"))
              .slot("link_permalink", v.get("link_permalink"))
              .slot("link_author", v.get("link_author")));
        }
        this.history.put(v.get("created_utc").longValue() * 1000L,
            Record.create(4)
                .slot("id", v.get("id"))
                .slot("author", v.get("author"))
                .slot("body", v.get("body"))
                .slot("permalink", v.get("permalink")));
      });
}