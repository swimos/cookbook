package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.concurrent.TimerRef;
import swim.structure.Form;
import swim.structure.Text;

import java.util.Objects;

public class CustomerAgent extends AbstractAgent {

    private TimerRef timer;

    @SwimLane("register")
    public CommandLane<String> register = this.<String>commandLane()
            .onCommand(location -> {
                addStockNotificationDownlink(location);
                createTakeItemTimer(location);
            });

    private void addStockNotificationDownlink(final String location) {
        final String warehouseNodeUri = "/warehouse/" + location;

        //Create a value downlink to a different Swim server from an agent
        this.downlinkValue().valueForm(Form.forInteger())
                .hostUri(SupplierPlane.WAREHOUSE_HOST_URI)
                .nodeUri(warehouseNodeUri).laneUri("lastResupplyId")
                .didSet((newValue, oldValue) -> {
                    if(!Objects.equals(newValue, oldValue)) System.out.println("customer received new stock notification, resupply: " + newValue);
                }).open();
    }

    private void createTakeItemTimer(final String location){
        cancelTimer();
        this.timer = setTimer(2000, () -> {
            command(SupplierPlane.WAREHOUSE_HOST_URI, "/warehouse/" + location, "takeItem", Text.from("foo"));
            this.timer.reschedule(2000);
        });
    }

    public void cancelTimer() {
        if (this.timer != null){
            this.timer.cancel();
        }
        this.timer = null;
    }

    @Override
    public void willStop() {
        cancelTimer();
    }

}
