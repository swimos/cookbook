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

    private long lastTime = 0L;

    @Override
    public void runTask() {
      final String aid = agencyId();
      // Make API call
      final Value payload = NextBusApi.getVehiclesForAgency(Main.httpClient(), aid, this.lastTime);
      // Extract information for all vehicles and the payload's timestamp
      final List<Value> vehicleInfos = new ArrayList<>(payload.length());
      for (Item i : payload) {
        if (i.head() instanceof Attr) {
          final String label = i.head().key().stringValue(null);
          if ("vehicle".equals(label)) {
            vehicleInfos.add(i.head().toValue());
          } else if ("lastTime".equals(label)) {
            this.lastTime = i.head().toValue().get("time").longValue();
          }
        }
      }
      // Relay each vehicleInfo to the appropriate VehicleAgent
      int i = 0;
      for (Value vehicleInfo : vehicleInfos) {
        command("/vehicle/" + aid + "/" + vehicleInfo.get("id").stringValue(),
            "addMessage",
            // lastTime came separately, manually add it to each vehicleInfo
            vehicleInfo.updatedSlot("timestamp", lastTime));
        i++;
      }
      System.out.println(nodeUri() + ": relayed info for " + i + " vehicles");
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
    this.timer = setTimer((long) (Math.random() * 1000), () -> {
      this.agencyPollTask.cue();
      // Placing reschedule() here is like ScheduledExecutorService#scheduleAtFixedRate.
      // Moving it to the end of agencyPollTask#runTask is like #scheduleWithFixedDelay.
      this.timer.reschedule(15000L);
    });
  }

  @Override
  public void didStart() {
    initPoll();
  }

}
