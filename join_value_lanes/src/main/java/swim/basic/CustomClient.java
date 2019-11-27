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

import swim.api.downlink.MapDownlink;
import swim.client.ClientRuntime;
import swim.structure.Form;
import swim.structure.Value;

class CustomClient {

    public static void main(String[] args) throws InterruptedException {
        ClientRuntime swimClient = new ClientRuntime();
        swimClient.start();

        final String hostUri = "warp://localhost:9001";
        final String buildingUri = "/building/swim";
        final String firstRoomUri = "/swim/1";
        final String secondRoomUri = "/swim/2";
        final String thirdRoomUri = "/swim/3";

        final MapDownlink<Integer, Boolean> link = swimClient.downlinkMap()
                .keyForm(Form.forInteger()).valueForm(Form.forBoolean())
                .hostUri(hostUri).nodeUri(buildingUri).laneUri("lights")
                .didUpdate((key, newValue, oldValue) -> {
                    System.out.println("The lights in room " + key + " are " + (newValue ? "on" : "off"));
                })
                .open();

        Thread.sleep(2000);

        swimClient.command(hostUri, firstRoomUri, "toggleLights", Value.absent());
        swimClient.command(hostUri, secondRoomUri, "toggleLights", Value.absent());
        swimClient.command(hostUri, thirdRoomUri, "toggleLights", Value.absent());
        swimClient.command(hostUri, secondRoomUri, "toggleLights", Value.absent());
        swimClient.command(hostUri, secondRoomUri, "toggleLights", Value.absent());
        swimClient.command(hostUri, thirdRoomUri, "toggleLights", Value.absent());

        Thread.sleep(2000);
        System.out.println("Will shut down client in 2 seconds");
        Thread.sleep(2000);

        swimClient.stop();
    }

}
