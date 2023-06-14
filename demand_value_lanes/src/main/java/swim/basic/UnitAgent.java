package swim.basic;

import java.util.Base64;
import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.DemandLane;
import swim.api.lane.ValueLane;

public class UnitAgent extends AbstractAgent {

  @SwimLane("raw")
  ValueLane<String> raw = this.<String>valueLane().didSet((n, o) -> this.data.cue());

  @SwimLane("data")
  DemandLane<String> data = this.<String>demandLane().onCue(uplink -> decodeRaw());

  // Transform raw data to the desired format
  private String decodeRaw() {
    final String encoded = this.raw.get();
    if (encoded == null) {
      return "";
    }
    final String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
    System.out.println(nodeUri() + ": Decoded raw data to: " + decoded);
    return decoded;
  }

  @Override
  public void didStart() {
    System.out.println(nodeUri() + " did start");
  }
}
