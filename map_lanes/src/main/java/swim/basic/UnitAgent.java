package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Text;
import swim.structure.Value;
import swim.uri.Uri;

public class UnitAgent extends AbstractAgent {

  @SwimLane("shoppingCart")
  MapLane<String, Integer> shoppingCart = this.<String, Integer>mapLane()
      .didUpdate((key, newValue, oldValue) -> {
        logMessage(key + " count changed to " + newValue + " from " + oldValue);
      })
      .didRemove((key, oldValue) -> {
        logMessage("removed <" + key  + "," + oldValue + ">");
      });

  @SwimLane("addItem")
  CommandLane<String> publish = this.<String>commandLane()
      .onCommand(msg -> {
        final int n = this.shoppingCart.getOrDefault(msg, 0) + 1;
        this.shoppingCart.put(msg, n);
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }
}
