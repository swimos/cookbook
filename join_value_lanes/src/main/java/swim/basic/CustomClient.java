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

import swim.client.ClientRuntime;
import swim.structure.Value;

class CustomClient {

  public static void main(String[] args) throws InterruptedException
  {
    
    
    ClientRuntime swimClient = new ClientRuntime();
    swimClient.start();
    final String hostUri = "warp://localhost:9001";
  
    final String firstRoomUri = "/swim/1";
    final String secondRoomUri = "/swim/2";
    final String thirdRoomUri = "/swim/3";
  
  
    swimClient.command(hostUri, firstRoomUri, "toggleLights", Value.absent());
    swimClient.command(hostUri, secondRoomUri, "toggleLights", Value.absent());
    swimClient.command(hostUri, thirdRoomUri, "toggleLights", Value.absent());
    swimClient.command(hostUri, secondRoomUri, "toggleLights", Value.absent());
    swimClient.command(hostUri, secondRoomUri, "toggleLights", Value.absent());
    swimClient.command(hostUri, thirdRoomUri, "toggleLights", Value.absent());
    
    System.out.println("Will shut down client in 2 seconds");
    Thread.sleep(2000);
    
    swimClient.stop();
  }
}
