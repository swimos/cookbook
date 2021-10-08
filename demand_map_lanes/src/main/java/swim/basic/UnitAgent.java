package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.DemandMapLane;
import swim.api.lane.MapLane;

public class UnitAgent extends AbstractAgent {

    @SwimLane("raw")
    protected MapLane<Integer, String> raw = this.<Integer, String>mapLane()
            .didUpdate((key, newValue, oldValue) -> this.data.cue(key));

    @SwimLane("data")
    protected DemandMapLane<Integer, String> data = this.<Integer, String>demandMapLane()
            .onCue((key, uplink) -> transformRaw(key))
            .onSync(uplink -> this.raw.keyIterator());

    private String transformRaw(Integer key) {
        final String rawValue = this.raw.get(key);
        if (rawValue == null) return null;
        System.out.println(nodeUri() + ": Transforming raw value: " + rawValue);
        return rawValue.toUpperCase(); //Arbitrary transformation for display purposes
    }

    @Override
    public void didStart() {
        System.out.println(nodeUri() + " did start");
        raw.put(1, "foo");
        raw.put(2, "foo");
    }

}
