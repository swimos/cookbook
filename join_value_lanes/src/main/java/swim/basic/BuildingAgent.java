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
import swim.structure.Value;

public class BuildingAgent extends AbstractAgent {

    @SwimLane("lights")
    JoinValueLane<String, Boolean> lights = this.<String, Boolean>joinValueLane().didUpdate((String key, Boolean newValue, Boolean oldValue) -> {
        System.out.println("The lights in room " + key + " are " + (newValue ? "on." : "off."));
    });

    @SwimLane("registerRoom")
    CommandLane<Value> registerRoom = this.<Value>commandLane().onCommand(room -> {
        String roomUri = "/" + this.getProp("name").stringValue() + "/" + room.stringValue();
        this.lights.downlink(room.stringValue()).nodeUri(roomUri).laneUri("lights").open();
    });

}
