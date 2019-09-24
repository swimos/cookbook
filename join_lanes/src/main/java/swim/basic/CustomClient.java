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

import swim.api.downlink.ValueDownlink;
import swim.client.ClientRuntime;
import swim.structure.*;

class CustomClient {

  public static void main(String[] args) throws InterruptedException {
    
    
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();
    final String hostUri = "warp://localhost:9001";
    final String fooNodeUri = "/unit/foo";
    final String barNodeUri = "/unit/bar";
    
    swimClient.command(hostUri, fooNodeUri, "WAKEUP", Value.absent());
    swimClient.command(hostUri, barNodeUri, "WAKEUP", Value.absent());
  
    
    final ValueDownlink<Value> fooCountLink = swimClient.downlinkValue()
                                                .hostUri(hostUri)
                                                .nodeUri(fooNodeUri)
                                                .laneUri("count")
                                                .open();
    
    fooCountLink.set(Num.from(13));
  
  
    final ValueDownlink<Value> barCountLink = swimClient.downlinkValue()
                                                        .hostUri(hostUri)
                                                        .nodeUri(barNodeUri)
                                                        .laneUri("count")
                                                        .open();
    
    barCountLink.set(Num.from(56));
  
    
    swimClient.command(hostUri, barNodeUri, "syncCounts", Text.from("/unit/foo"));
    swimClient.command(hostUri, fooNodeUri, "syncCounts", Text.from("/unit/bar"));
  
    swimClient.command(hostUri, fooNodeUri, "displayStatus", Text.from("Foo"));
    Thread.sleep(2000);
    swimClient.command(hostUri, barNodeUri, "displayStatus", Text.from("Bar"));
    
    System.out.println("Will shut down client in 2 seconds");
    Thread.sleep(2000);
    
    swimClient.stop();
  }
}
