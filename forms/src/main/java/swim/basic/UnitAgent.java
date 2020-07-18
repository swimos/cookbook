public class UnitAgent {
	
	@SwimLane("foo")
	public ValueLane<FooType> foo = this.<FooType>valueLane()
			.didSet(newValue, oldValue) -> {
				System.out.println(newValue);
			});
	
	@SwimLane("addFoo")
  public CommandLane<FooType> addFoo = this.<FooType>commandLane()
  	.onCommand((FooType value) -> {
  		foo.set(value);
  	});

  @SwimLane("bar")
  public ValueLane<BarType> bar = this.<BarType>valueLane()
  		.didSet(newValue, oldValue) -> {
  			System.out.println(newValue);
  		});
  
  @SwimLane("addBar")
  public CommandLane<FooType> addBar = this.<BarType>commandLane()
  	.onCommand((BarType value) -> {
  		bar.set(value);
  	});
	
  @SwimLane("addValue")
  public CommandLane<Value> addValue = this.<Value>commandLane()
  	.onCommand((Value value) -> {
  		System.out.println(value);
  	});
}