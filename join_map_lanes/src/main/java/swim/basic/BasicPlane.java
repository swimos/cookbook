package swim.basic;

import swim.api.SwimLane;
import swim.api.SwimRoute;
import swim.api.agent.AbstractAgent;
import swim.api.agent.AgentRoute;
import swim.api.downlink.MapDownlink;
import swim.api.lane.JoinMapLane;
import swim.api.lane.MapLane;
import swim.api.plane.AbstractPlane;
import swim.fabric.FabricDef;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.service.web.WebServiceDef;

public class BasicPlane {

    private static final String HOST_URI = "warp://localhost:53556";
    private static final int THRESHOLD = 1000;

    static class StateMapLaneAgent extends AbstractAgent {
        @SwimLane("state")
        MapLane<String, String> stateMap = this.mapLane();
    }

    static class StateJoinMapLaneAgent extends AbstractAgent {
        @SwimLane("join")
        JoinMapLane<String, String, String> stateJoinMap = this.joinMapLane();

        @Override
        public void didStart() {
            stateJoinMap.downlink("california").hostUri(HOST_URI).nodeUri("/state/california").laneUri("state").open();
            stateJoinMap.downlink("texas").hostUri(HOST_URI).nodeUri("/state/texas").laneUri("state").open();
            stateJoinMap.downlink("florida").hostUri(HOST_URI).nodeUri("/state/florida").laneUri("state").open();
        }
    }

    static class StateJoinMapPlane extends AbstractPlane {
        @SwimRoute("/state/:name")
        AgentRoute<StateMapLaneAgent> mapRoute;

        @SwimRoute("/join/state/:name")
        AgentRoute<StateJoinMapLaneAgent> joinMapRoute;
    }

    private static MapDownlink<String, Integer> initDownlink(StateJoinMapPlane plane, String uri) {
        return plane.downlinkMap()
                .keyClass(String.class)
                .valueClass(Integer.class)
                .hostUri(HOST_URI)
                .nodeUri(uri)
                .laneUri("state")
                .open();
    }

    public static void main(String[] args) throws InterruptedException {
        final Kernel kernel = ServerLoader.loadServerStack();
        final StateJoinMapPlane plane = kernel.openSpace(FabricDef.fromName("test"))
                .openPlane("test", StateJoinMapPlane.class);

        try {
            kernel.openService(WebServiceDef.standard().port(53556).spaceName("test"));
            kernel.start();

            final MapDownlink<String, Integer> californiaDownlink = initDownlink(plane, "/state/california");
            final MapDownlink<String, Integer> texasDownlink = initDownlink(plane, "/state/texas");
            final MapDownlink<String, Integer> floridaDownlink = initDownlink(plane, "/state/florida");

            final MapDownlink<String, Integer> joinDownlink = plane.downlinkMap()
                    .keyClass(String.class)
                    .valueClass(Integer.class)
                    .hostUri(HOST_URI)
                    .nodeUri("/join/state/all")
                    .laneUri("join")
                    .didUpdate((key, newValue, oldValue) -> {
                        if (newValue > THRESHOLD) {
                            logStreet(key, newValue);
                        }
                    })
                    .open();

            // A preferred approach is to use countdown latches but for brevity a sleep suffices
            Thread.sleep(1000);

            californiaDownlink.put("cal_st_george", 500);
            californiaDownlink.put("cal_centre_st", 1000);
            texasDownlink.put("tx_crockett", 100);
            texasDownlink.put("tx_houston", 200);
            floridaDownlink.put("fl_nene", 3000);
            floridaDownlink.put("fl_worth", 4000);

            Thread.sleep(1000);
        } finally {
            kernel.stop();
        }
    }

    private static void logStreet(String streetName, Integer population) {
        System.out.println(streetName + " has " + population + " residents");
    }

}

