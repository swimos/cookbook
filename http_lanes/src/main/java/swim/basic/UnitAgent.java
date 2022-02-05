package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.http.HttpLane;
import swim.api.lane.ValueLane;
import swim.http.HttpMethod;
import swim.http.HttpResponse;
import swim.http.HttpStatus;
import swim.http.MediaType;
import swim.json.Json;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;

public class UnitAgent extends AbstractAgent {

    @SwimLane("state")
    ValueLane<Value> state = this.<Value>valueLane()
            .didSet((newValue, oldValue) -> {
                logMessage("State changed from " + Recon.toString(oldValue) + " to " + Recon.toString(newValue));
            });

    @SwimLane("http")
    HttpLane<Value> http = this.<Value>httpLane()
            .doRespond(request -> {
                if (HttpMethod.POST.equals(request.method())){
                    state.set(request.entity().get());
                }
                return HttpResponse.from(HttpStatus.OK).body(Recon.toString(state.get()), MediaType.applicationXRecon());
            });

    @SwimLane("httpJson")
    HttpLane<Value> httpJson = this.<Value>httpLane()
            .doRespond(request ->
                 HttpResponse.from(HttpStatus.OK).body(Json.toString(state.get()), MediaType.applicationJson()));

    @Override
    public void didStart() {
        logMessage("did start");
        //Insert some dummy values into the state of the web agent
        state.set(Record.create(2)
                .slot("foo", 1)
                .slot("bar", 2));
    }

    private void logMessage(final String message) {
        System.out.println(nodeUri() + ": " + message);
    }

}