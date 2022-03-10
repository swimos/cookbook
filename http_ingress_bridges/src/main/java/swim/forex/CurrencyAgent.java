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

package swim.forex;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.structure.Value;

public class CurrencyAgent extends AbstractAgent {

  @SwimLane("rateFromUSD")
  MapLane<Long, Double> rateFromUSD = this.<Long, Double>mapLane()
      .didUpdate((k, n, o) -> {
        logMessage("added entry <" + k + ", " + n + ">");
      });

  @SwimLane("addEntry")
  CommandLane<Value> addEntry = this.<Value>commandLane()
      .onCommand(v -> {
        this.rateFromUSD.put(v.get("timestamp").longValue(), v.get("rate").doubleValue());
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

}
