package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.MapLane;

class StreetStatisticsAgent extends AbstractAgent {

  StreetStatisticsAgent() {
  }
    
  /*
      - Key: Street name
      - Value: Street population
   */
  @SwimLane("state")
  MapLane<String, Integer> streetStatistics = this.mapLane();

}
