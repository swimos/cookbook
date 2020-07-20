package swim.basic;

import swim.structure.Form;
import swim.structure.Kind;
import swim.structure.Tag;

/**
 * TODO: write a javadoc
 */

@Tag("fooType")
public class FooType {
	private int i = 0;
	private String s = "";
	
	public FooType() {
	}
	
	public FooType(int i, String s) {
		this.i = i;
		this.s = s;
	}
	
	public int getNumber() {
		return i;
	}
	
	public String getString() {
		return s;
	}
	
	@Kind
	private static Form<FooType> form;
	
	public static Form<FooType> form() {
		if(form == null) {
			form = Form.forClass(FooType.class);
		}
		return form;
	}
}

// TODO:
//Fundamentally a simple Java class, with a small number of fields of any simple type you'd like (e.g. primitives, Strings). Something like class FooType { int i; String s; }
//Add the necessary pieces to automatically generated a form, like here
//@Tag annotation
//Default values and (hugely important, easily forgettable) a no-argument constructor
//@Kind-annotated static field and static accessor method.