public class UnitAgent {
	
	@SwimLane("foo")
	public ValueLane<FooType> foo;
	// TODO: where in a swim agent class do you put methods such as didSet() ?
	// TODO: didSet(newValue, oldValue) callback prints newValue
	
	@SwimLane("addFoo")
  public CommandLane<FooType> addFoo = this.<FooType>commandLane().onCommand((FooType value) -> {
    foo.set(value);
  }

  @SwimLane("bar")
  public ValueLane<BarType> bar;
  // TODO: (newValue, oldValue) callback prints newValue
  
  @SwimLane("addBar")
  public CommandLane<FooType> addBar = this.<BarType>commandLane().onCommand((BarType value) -> {
    bar.set(value);
  }
	
  @SwimLane("addValue")
  public CommandLane<Value> addValue = this<Value>commandLane().onCommand((Value value)) -> {
  	console.log(value);
  }
}