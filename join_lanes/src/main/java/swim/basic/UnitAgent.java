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
import swim.api.lane.JoinValueLane;
import swim.api.lane.ValueLane;

import java.util.Map;

public class UnitAgent extends AbstractAgent
{
    @SwimLane ("count")
    ValueLane<Integer> count;
    
    @SwimLane ("externalCounts")
    JoinValueLane<String, Integer> externalCounts = this.joinValueLane();
    
    @SwimLane ("syncCounts")
    CommandLane<String> syncCounts = this.<String>commandLane().onCommand(node -> {
        this.externalCounts.downlink(node).nodeUri(node).laneUri("count").open();
    });
    
    @SwimLane ("displayStatus")
    CommandLane<String> displayStatus = this.<String>commandLane().onCommand(msg -> {
        
        System.out.println("---Node " + msg + " Count: " + this.count.get() + "---");
        for (Map.Entry<String, Integer> entry : this.externalCounts)
        {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
        System.out.println();
    });
}
