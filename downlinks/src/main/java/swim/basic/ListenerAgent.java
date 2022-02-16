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
import swim.api.downlink.MapDownlink;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.collections.HashTrieMap;
import swim.recon.Recon;
import swim.structure.Form;
import swim.structure.Record;
import swim.structure.Value;

public class ListenerAgent extends AbstractAgent {

  // Shopping cart data for *all* UnitAgents, aggregated into a single ListenerAgent
  @SwimLane("shoppingCarts")
  public MapLane<String, Value> shoppingCarts = this.<String, Value>mapLane()
          .didUpdate((k, n, o) -> {
            logMessage("shoppingCarts updated " + k + ": " + Recon.toString(n));
          });
  // Immutable java.util.Map containing all downlink subscriptions
  private HashTrieMap<String, MapDownlink<String, Integer>> shoppingCartSubscribers;
  // Opens a subscription to the `UnitAgent` indicated by `v`
  @SwimLane("triggerListen")
  public CommandLane<String> triggerListen = this.<String>commandLane()
          .onCommand(v -> {
            logMessage("will listen to " + v);
            addSubscription(v);
          });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

  private void addSubscription(final String targetNode) {
    final MapDownlink<String, Integer> downlink = downlinkMap()
            .keyForm(Form.forString()).valueForm(Form.forInteger())
            .nodeUri(targetNode).laneUri("shoppingCart")
            .didUpdate((k, n, o) -> {
              // Update the correct entry in shoppingCarts on every downlink didUpdate()
              final Value shoppingCart = this.shoppingCarts.get(targetNode);
              final Record record = shoppingCart.isDefined() ? ((Record) shoppingCart).branch() : Record.create();
              record.put(k, n);
              this.shoppingCarts.put(targetNode, record);
            })
            .open();
    // Make this downlink accessible by adding it to `shoppingCartSubscribers`
    if (this.shoppingCartSubscribers == null) {
      this.shoppingCartSubscribers = HashTrieMap.empty();
    }
    this.shoppingCartSubscribers = this.shoppingCartSubscribers.updated(targetNode, downlink);
  }
}
