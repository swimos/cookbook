package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.DemandLane;
import swim.api.lane.ValueLane;

public class UnitAgent extends AbstractAgent {

    @SwimLane("raw")
    ValueLane<Integer> raw = this.<Integer>valueLane().didSet((n, o) -> this.data.cue());

    @SwimLane("data")
    DemandLane<String> data = this.<String>demandLane().onCue(uplink -> transformRaw());

    // Transform raw data to the desired format
    private String transformRaw() {
        final Integer v = this.raw.get();
        System.out.println(nodeUri() + ": Transforming raw data: "+ v);
        if (v != null) {
            return  "Num:" + v; //Arbitrary transformation for display purposes
        } else {
            return "";
        }
    }

    @Override
    public void didStart() {
        System.out.println(nodeUri() + " did start");
    }

}
