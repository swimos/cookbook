package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.downlink.MapDownlink;
import swim.api.lane.CommandLane;
import swim.api.lane.JoinMapLane;
import swim.structure.Form;
import swim.structure.Text;

public class SupplierAgent extends AbstractAgent {

    private final static String WAREHOUSE_HOST_URI = "warp://localhost:9002";

    private final static int RESUPPLY_THRESHOLD = 3;

    @SwimLane("register")
    public CommandLane<String> register = this.<String>commandLane().
            onCommand(location -> {
                addStockDownlink(location);
                addResupplyDownlink(location);
            });

    /**
     * Creates a downlink to a given location that will resupply stock when it falls below the threshold
     */
    private void addResupplyDownlink(final String location){
        final String warehouseNodeUri = "/warehouse/" + location;

        final MapDownlink<String, Integer> resupplyDownlink = this.downlinkMap()
                .keyForm(Form.forString()).valueForm(Form.forInteger())
                .hostUri(WAREHOUSE_HOST_URI)
                .nodeUri(warehouseNodeUri)
                .laneUri("stock")
                .didUpdate(((item, newValue, oldValue) -> {
                    if(newValue < RESUPPLY_THRESHOLD){ //If the stock is too low then resupply it
                        logResupply(item, newValue, location);
                        this.command(WAREHOUSE_HOST_URI, warehouseNodeUri, "resupply", Text.from(item));
                    }
                }))
                .open();
    }

    @SwimLane("stock")
    public JoinMapLane<String, String, Integer> stock = this.<String, String, Integer>joinMapLane();

    private void addStockDownlink(final String location){
        final String warehouseNodeUri = "/warehouse/" + location;
        stock.downlink(location).hostUri(WAREHOUSE_HOST_URI).nodeUri(warehouseNodeUri).laneUri("stock").open();
    }

    private void logResupply(final String item, final Integer stock, final String location){
        System.out.println("current stock of (" + item + ":" + stock + ") too low at " + location + " warehouse, resupplying");
    }

    @Override
    public void didStart() {
        logEvent("opened");
    }

    @Override
    public void willStop() {
        logEvent("stopped");
    }

    private void logEvent(Object msg) {
        System.out.println("supplier " + msg);
    }
}
