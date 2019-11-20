package swim.basic;

import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.plane.AbstractPlane;

class StateJoinMapPlane extends AbstractPlane {

  @SwimRoute("/state/:name")
  AgentRoute<StreetStatisticsAgent> mapRoute;

  @SwimRoute("/join/state/:name")
  AgentRoute<AggregatedStatisticsAgent> joinMapRoute;

}
