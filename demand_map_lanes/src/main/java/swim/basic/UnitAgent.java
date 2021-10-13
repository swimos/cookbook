package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.DemandMapLane;
import swim.api.lane.MapLane;

import java.util.Base64;
import java.util.Collections;

public class UnitAgent extends AbstractAgent {

    @SwimLane("raw")
    protected MapLane<String, String> raw = this.<String, String>mapLane()
            .didUpdate((key, newValue, oldValue) -> this.data.cue(key));

    @SwimLane("data")
    protected DemandMapLane<String, String> data = this.<String, String>demandMapLane()
            .onCue((key, uplink) -> {
                final String name = uplink.laneUri().query().get("name");
                //Decode if no parameter was passed by the uplink or if the key matches the parameter
                return (name == null || name.equals(key)) ? decodeRaw(key) : null;
            })
            .onSync(uplink -> {
                final String name = uplink.laneUri().query().get("name");
                return (this.raw.containsKey(name)) ? Collections.singletonList(name).iterator() : this.raw.keyIterator();
            });

    private String decodeRaw(String key) {
        final String encoded = this.raw.get(key);
        if (encoded == null) return "";
        final String decoded = new String(Base64.getDecoder().decode(encoded.getBytes()));
        System.out.println(nodeUri() + ": Decoded raw data to: "+ decoded);
        return decoded;
    }

    @Override
    public void didStart() {
        System.out.println(nodeUri() + " did start");
    }

}
