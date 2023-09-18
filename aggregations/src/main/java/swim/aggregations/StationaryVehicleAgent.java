package swim.aggregations;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.structure.Value;
import swim.uri.Uri;

/**
 * This agent is not used in the demo.
 * It is included to show the use of the {@link AbstractAgent#didStart()} method.
 */
public class StationaryVehicleAgent extends AbstractAgent {

  public StationaryVehicleAgent() {
  }

  @SwimLane("addEvent")
  private CommandLane<Value> addEvent = this.<Value>commandLane()
      .onCommand(v -> this.status.set(v));

  @SwimLane("status")
  private ValueLane<Value> status = this.<Value>valueLane();

  private void joinState(final String state) {
    command(
        "/state/" + state,
        "addVehicle",
        Uri.form().mold(nodeUri()).toValue()
    );
  }

  @Override
  public void didStart() {
    joinState("California");
  }

}
