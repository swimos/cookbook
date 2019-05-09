package swim.grade;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.api.lane.ValueLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;

public class StudentAgent extends AbstractAgent {

  private int id;

  @SwimLane("currentGrade")
  ValueLane<Value> grade = this.<Value>valueLane()
      .didSet((n, o) -> {
        logMessage("changed grade to " + Recon.toString(n));
        CustomDriver.updateGrade(id, n.get("earned").intValue(), n.get("possible").intValue());
      });

  @SwimLane("addAssignment")
  CommandLane<Value> publish = this.<Value>commandLane()
      .onCommand(msg -> {
        final Value current = grade.get();
        this.grade.set(Record.create(2)
            .slot("earned", current.get("earned").intValue() + msg.get("earned").intValue())
            .slot("possible", current.get("possible").intValue() + msg.get("possible").intValue()));
      });

  private void logMessage(Object msg) {
    System.out.println(nodeUri() + ": " + msg);
  }

  @Override
  public void didStart() {
    try {
      this.id = getProp("id").intValue();
    } catch (UnsupportedOperationException e) {
      logMessage("startup error");
      e.printStackTrace();
      close();
    }
    this.grade.set(Record.create(2).slot("earned", 0).slot("possible", 0));
  }
}
