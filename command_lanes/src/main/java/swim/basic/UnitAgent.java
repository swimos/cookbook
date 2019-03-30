package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;
import swim.uri.Uri;

public class UnitAgent extends AbstractAgent {

  @SwimLane("publish")
  CommandLane<Integer> publish = this.<Integer>commandLane()
      .onCommand((Integer msg) -> {
        logMessage("`publish` commanded with " + msg);
        final Value updatedMsg = Record.create(1).slot("fromServer", msg);
        // command() `updatedMsg` TO
        // the "publishValue" lane OF
        // the agent addressable by `nodeUri()` RUNNING ON
        // this plane (indicated by no hostUri argument)
        command(nodeUri(), Uri.parse("publishValue"), updatedMsg);
      });

  @SwimLane("publishValue")
  CommandLane<Value> publishV = this.<Value>commandLane()
      .onCommand((Value msg) -> {
        logMessage("`publishValue` commanded with " + Recon.toString(msg));
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
