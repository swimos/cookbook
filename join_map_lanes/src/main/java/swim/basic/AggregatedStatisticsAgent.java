package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.JoinMapLane;

class AggregatedStatisticsAgent extends AbstractAgent {

  // Aggregated statistics of US states
  @SwimLane("join")
  JoinMapLane<String, String, Integer> stateStreetStats = this.joinMapLane();

  @Override
  public void didStart() {
    this.stateStreetStats
        .downlink("california")
        .hostUri(BasicPlane.HOST_URI)
        .nodeUri("/state/california")
        .laneUri("state")
        .open();
    this.stateStreetStats
        .downlink("texas")
        .hostUri(BasicPlane.HOST_URI)
        .nodeUri("/state/texas")
        .laneUri("state")
        .open();
    this.stateStreetStats
        .downlink("florida")
        .hostUri(BasicPlane.HOST_URI)
        .nodeUri("/state/florida")
        .laneUri("state")
        .open();
  }
}
