package swim.basic;

import java.util.Base64;
import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.DemandLane;
import swim.api.lane.ValueLane;
import swim.api.warp.WarpUplink;

public class UnitAgent extends AbstractAgent {

  @SwimLane("raw")
  ValueLane<String> raw = this.<String>valueLane().didSet((n, o) -> this.data.cue());

  @SwimLane("data")
  DemandLane<String> data = this.<String>demandLane()
      .willUplink(uplink -> {
        System.out.println("willUplink start: " + uplink);
        uplink.onUnlink(plink -> {
          System.out.println("onUnlink: " + uplink);
        });
        uplink.onUnlinked(plink -> {
          System.out.println("onUnlinked: " + uplink);
        });
        System.out.println("willUplink finish: " + uplink);
      })
      .didUplink(uplink -> {
        System.out.println("didUplink start: " + uplink);
        uplink.onUnlink(plink -> {
          System.out.println("onUnlink: " + uplink);
        });
        uplink.onUnlink(plink -> {
          System.out.println("onUnlinked: " + uplink);
        });
        System.out.println("didUplink finish: " + uplink);
      })
      .onCue(this::decodeRaw);

  // Transform raw data to the desired format
  private String decodeRaw(WarpUplink uplink) {
    System.out.println("onCue: " + uplink);
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
