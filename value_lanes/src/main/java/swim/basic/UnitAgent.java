package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Text;
import swim.structure.Value;
import swim.uri.Uri;

public class UnitAgent extends AbstractAgent {

  @SwimLane("info")
  ValueLane<String> info = this.<String>valueLane()
      .didSet((newValue, oldValue) -> {
        logMessage("`info` set to {" + newValue + "} from {" + oldValue + "}");
      });

  @SwimLane("publishInfo")
  CommandLane<String> publishInfo = this.<String>commandLane()
      .onCommand(msg -> {
        this.info.set("from publishInfo: " + msg);
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
