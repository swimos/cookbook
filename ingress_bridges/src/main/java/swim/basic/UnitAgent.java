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
import swim.api.downlink.ValueDownlink;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.structure.Form;

public class UnitAgent extends AbstractAgent {

  private ValueDownlink<String> toDataSource;

  @SwimLane("history")
  MapLane<Long, String> history = this.<Long, String>mapLane()
      .didUpdate((k, n, o) -> {
        logMessage(String.format("history update: <%d, %s>", k, n));
      });

  @SwimLane("publish")
  CommandLane<String> publish = this.<String>commandLane()
      .onCommand(msg -> {
        this.history.put(System.currentTimeMillis(), msg);
      });

  @Override
  public void didStart() {
    // If your data source is a separate Swim server running on
    //   `localhost:9002`
    // and for each `UnitAgent` with `nodeUri`
    //   `/unit/:id`
    // in the current server, you have a corresponding Web Agent with `nodeUri`
    //   `/source/:id`
    // that contains a `ValueLane<String>` addressable via
    //   `val`
    // then the following line pulls data from the remote server
    subscribe("warp://localhost:9002", "/source/" + getProp("id").stringValue(), "val");
    // Feel free to comment it out when running the MQTT demo; it is only used
    // in the WARP demo.
  }

  private void subscribe(String host, String node, String lane) {
    if (this.toDataSource != null) {
      this.toDataSource.close();
    }
    this.toDataSource = downlinkValue()
        .valueForm(Form.forString())
        .hostUri(host).nodeUri(node).laneUri(lane)
        .keepSynced(true)
        .didSet((n, o) -> {
          this.history.put(System.currentTimeMillis(), n);
        })
        .open();
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
