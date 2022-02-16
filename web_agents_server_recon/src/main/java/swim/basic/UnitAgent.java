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
import swim.api.lane.ValueLane;
import swim.structure.Value;

public class UnitAgent extends AbstractAgent {

  @SwimLane("info")
  ValueLane<Value> info;

  @Override
  public void didStart() {
    System.out.println(nodeUri() + " didStart region");
    prop();
    close();
  }

  @Override
  public void willStop() {
    logMessage("willStop");
  }

  // Fetch values of various property types belonging to the agents.
  void prop() {
    // 1. Property type: String
    final String stringVal = getProp("propString").stringValue("");
    logMessage("String Property '" + stringVal + "'");

    // 2. Property type: Integer
    final int intVal = getProp("propInt").intValue(0);
    logMessage("Integer Property 'propInt' with value " + intVal);

    // 3. Property type: Float
    final float floatVal = getProp("propFloat").floatValue(0.0f);
    logMessage("Float Property 'propFloat' with value " + floatVal);

    // 4. Property type: Boolean
    final boolean boolVal = getProp("propBool").booleanValue(false);
    logMessage("Boolean Property 'propBool' with value " + boolVal);

    // 5. Dynamic Agent Uri demonstration:
    if (nodeUri().toString() == "/unit/dynamic") {
      // n.b., "id" is dynamically declared.
      final String defaultID = getProp("id").stringValue(null);
      logMessage("The dynamically declared ID is : " + defaultID);
    }
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
