package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;
import swim.api.lane.ValueLane;

public class WarehouseAgent extends AbstractAgent {

    @SwimLane("stock")
    public MapLane<String, Integer> stock = this.<String, Integer>mapLane();

    @SwimLane("takeItem")
    public CommandLane<String> takeItem = this.<String>commandLane()
            .onCommand(item -> {
                int newValue = this.stock.getOrDefault(item, 1) - 1;
                if(newValue < 0) newValue = 0;
                this.stock.put(item, newValue);
            });

    @SwimLane("resupply")
    public CommandLane<String> resupply = this.<String>commandLane()
            .onCommand(item -> {
                final int newValue = this.stock.getOrDefault(item, 0) + 5;
                this.stock.put(item, newValue);
                this.lastResupplyId.set(this.lastResupplyId.get() + 1);
                logResupply(item, newValue);
            });

    @SwimLane("lastResupplyId")
    public ValueLane<Integer> lastResupplyId = this.<Integer>valueLane();

    private void logResupply(final String item, final Integer stock){
        System.out.println("resupplied " + item + " in " + getProp("location").stringValue() + " warehouse to " + stock + " units");
    }

    @Override
    public void didStart() {
        this.lastResupplyId.set(0);
        logEvent("opened");
    }

    @Override
    public void willStop() {
        logEvent("stopped");
    }

    private void logEvent(Object msg) {
        System.out.println(getProp("location").stringValue() + " warehouse " + msg);
    }

}
