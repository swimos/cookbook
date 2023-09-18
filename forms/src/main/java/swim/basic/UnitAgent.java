package swim.basic;

import swim.api.SwimLane;
import swim.api.agent.AbstractAgent;
import swim.api.lane.CommandLane;
import swim.api.lane.ValueLane;
import swim.structure.Value;

public class UnitAgent extends AbstractAgent {
  public UnitAgent() {
  }

  // ValueLane<FooType> foo, whose didSet(newValue, oldValue) callback prints newValue
  @SwimLane("foo")
  ValueLane<FooType> foo = this.<FooType>valueLane()
      .didSet((newValue, oldValue) -> {
        System.out.println("foo: " + newValue);
      });

  // CommandLane<FooType> addFoo, whose onCommand(value) callback invokes foo.set(value)
  @SwimLane("addFoo")
  CommandLane<FooType> addFoo = this.<FooType>commandLane()
      .onCommand((FooType value) -> {
        this.foo.set(value);
      });

  // ValueLane<BarType> bar, whose didSet(newValue, oldValue) callback prints newValue
  @SwimLane("bar")
  ValueLane<BarType> bar = this.<BarType>valueLane()
      .didSet((newValue, oldValue) -> {
        System.out.println("bar: " + newValue);
      });

  // CommandLane<BarType> addBar, whose onCommand(value) callback invokes bar.set(value)
  @SwimLane("addBar")
  CommandLane<BarType> addBar = this.<BarType>commandLane()
      .onCommand((BarType value) -> {
        this.bar.set(value);
      });

  // CommandLane<Value> addValue, whose onCommand(value) callback prints value
  // Just included to exercise that the internal lane type is always fundamentally a Value, so this type is compatible with anything.
  @SwimLane("addValue")
  CommandLane<Value> addValue = this.<Value>commandLane()
      .onCommand((Value value) -> {
        System.out.println("some value: " + value);
      });

}
