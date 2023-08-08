package swim.vehicle;

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
      final Value payload = NextBusApi.getVehiclesForAgency(Main.httpClient(), aid);
      for (Item i : payload) {
        if (i.head() instanceof Attr && "vehicle".equals(i.head().key().stringValue(null))) {
          final Value vehicleInfo = i.head().toValue();
          final String vid = vehicleInfo.get("id").stringValue();
          command("/vehicle/" + aid + "/" + vid, "addMessage", vehicleInfo);
        }
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
