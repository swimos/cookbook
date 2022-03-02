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

import swim.client.ClientRuntime;
import swim.structure.Record;

final class Sim {

  private Sim() {
  }

  public static void main(String[] args) throws InterruptedException {
    final ClientRuntime client = new ClientRuntime();
    client.start();
    while (true) {
      client.command("warp://localhost:9001",
              String.format("/student/%d", ((int) (Math.random() * 5)) + 1),
              "addAssignment",
              Record.create(3)
                      .attr("assignment")
                      .slot("earned", ((int) (Math.random() * 5)) + 15)
                      .slot("possible", 20));
      Thread.sleep(500);
    }
  }

}
