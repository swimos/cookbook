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

import swim.api.agent.AbstractAgent;
import swim.api.SwimLane;
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
    final String stringVal = getProp("propString").stringValue(null);
    if (stringVal != null) {
      System.out.println("Property " + stringVal + " belongs to " +
              nodeUri());
    }
    else {
      System.out.println("Property 'propString' does not exist for " +
              nodeUri());
    }

    // 2. Property type: Integer
    try {
      Value intVal = null;
      intVal = getProp("propInt");
      String type = intVal.getClass().getSimpleName().toString();
      if (intVal == Value.absent()) {
        System.out.println("Property 'propInt' does not exist for " +
                nodeUri());
      }
      else if (!type.equals("NumI32")) {
        System.out.println("Property 'propInt' of " + nodeUri() +
                " has incompatible value assigned.");
      }
      else {
        System.out.println("Property 'propInt' with value " +
                intVal.intValue() + " belongs to " + nodeUri());
      }
    }
    catch (Exception e) {
      logMessage(e);
    }

    // 3. Property type: Float
    try {
      Value floatVal = null;
      floatVal = getProp("propFloat");
      String type = floatVal.getClass().getSimpleName().toString();
      if (floatVal == Value.absent()) {
        System.out.println("Property 'propFloat' does not exist for " +
                nodeUri());
      }
      else if (!type.equals("NumF64")) {
        System.out.println("Property 'propFloat' of " + nodeUri() +
                " has incompatible value assigned.");
      }
      else {
        System.out.println("Property 'propFloat' with value " +
                floatVal.floatValue() + " belongs to " + nodeUri());
      }
    }
    catch (Exception e) {
      logMessage(e);
    }

    // 4. Property type: Boolean
    try {
      Value boolVal = null;
      boolVal = getProp("propBool");
      String type = boolVal.getClass().getSimpleName().toString();
      if (boolVal == Value.absent()) {
        System.out.println("Property 'propBool' does not exist for " +
                nodeUri());
      }
      else if (!type.equals("Bool")) {
        System.out.println("Property 'propBool' of " + nodeUri() +
                " has incompatible value assigned.");
      }
      else {
        System.out.println("Property 'propBool' with value " +
                boolVal.booleanValue() + " belongs to " + nodeUri());
      }
    }
    catch (Exception e) {
      logMessage(e);
    }
  }

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
