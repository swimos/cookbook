package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.MapLane;

public class WarehouseAgent extends AbstractAgent {

    private final static String SUPPLIER_HOST_URI = "warp://localhost:9001";

    @SwimLane("stock")
    public MapLane<String, Integer> stock = this.<String, Integer>mapLane();

    @SwimLane("addItem")
    public CommandLane<String> addItem = this.<String>commandLane()
            .onCommand(item -> {
                this.stock.put(item, 0);
            });

    @SwimLane("takeItem")
    public CommandLane<String> takeItem = this.<String>commandLane()
            .onCommand(item -> {
                final int newValue = this.stock.getOrDefault(item, 1) - 1;
                this.stock.put(item, newValue);
            });

    @SwimLane("resupply")
    public CommandLane<String> resupply = this.<String>commandLane()
            .onCommand(item -> {
                final int newValue = this.stock.getOrDefault(item, 0) + 5;
                this.stock.put(item, newValue);
                logResupply(item, newValue);
            });

    private void logResupply(final String item, final Integer stock){
        System.out.println("resupplied " + item + " in " + getProp("location").stringValue() + " warehouse to " + stock + " units");
    }

    @Override
    public void didStart() {
        command(SUPPLIER_HOST_URI, "/supplier", "register", getProp("location")); //Register this warehouse to the supplier
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
