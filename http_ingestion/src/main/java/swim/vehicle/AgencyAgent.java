package swim.vehicle;

import java.util.ArrayList;
import java.util.List;
import swim.api.agent.AbstractAgent;
import swim.concurrent.AbstractTask;
import swim.concurrent.TaskRef;
import swim.concurrent.TimerRef;
import swim.structure.Attr;
import swim.structure.Item;
import swim.structure.Value;

public class AgencyAgent extends AbstractAgent {

  private TimerRef timer;
  private final TaskRef agencyPollTask = asyncStage().task(new AbstractTask() {

    @Override
    public void runTask() {
      final String aid = agencyId();
      // Make API call
      final Value payload = NextBusApi.getVehiclesForAgency(Main.httpClient(), aid);
      // Extract information for all vehicles and the API-provided timestamp
      final List<Value> vehicleInfos = new ArrayList<>(payload.length());
      long lastTime = -1L;
      for (Item i : payload) {
        if (i.head() instanceof Attr) {
          final String label = i.head().key().stringValue(null);
          if ("vehicle".equals(label)) {
            vehicleInfos.add(i.head().toValue());
          } else if ("lastTime".equals(label)) {
            lastTime = i.head().toValue().get("time").longValue();
          }
        }
      }
      // Relay each (vehicleInfo UNION timestamp) to the appropriate VehicleAgent
      for (Value vehicleInfo : vehicleInfos) {
        command("/vehicle/" + aid + "/" + vehicleInfo.get("id").stringValue(),
            "addMessage",
            vehicleInfo.updatedSlot("timestamp", lastTime));
      }
    }

    @Override
    public boolean taskWillBlock() {
      return true;
    }

  });

  private String agencyId() {
    final String nodeUri = nodeUri().toString();
    return nodeUri.substring(nodeUri.lastIndexOf("/") + 1);
  }

  private void initPoll() {
    this.timer = setTimer((long) (Math.random() * 100), () -> {
      this.agencyPollTask.cue();
      this.timer.reschedule(15000L);
    });
  }

  @Override
  public void didStart() {
    initPoll();
  }

}
