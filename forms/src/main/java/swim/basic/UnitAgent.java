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

// TODO:
//Has ValueLane<FooType> foo, whose didSet(newValue, oldValue) callback prints newValue.
//Has CommandLane<FooType> addFoo, whose onCommand(value) callback invokes foo.set(value).
//Has ValueLane<BarType> bar, whose didSet(newValue, oldValue) callback prints newValue.
//Has CommandLane<BarType> addBar, whose onCommand(value) callback invokes bar.set(value).
//OPTIONAL: Has CommandLane<Value> addValue, whose onCommand(value) callback prints value. Just to exercise that the internal lane type is always fundamentally a Value, so this type is compatible with anything. Though it's up to you to decide if this actually isn't that useful.