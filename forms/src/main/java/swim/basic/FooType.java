package swim.basic;

import swim.recon.Recon;
import swim.structure.Form;
import swim.structure.Kind;
import swim.structure.Tag;

// Fundamentally a simple Java class, with a small number of fields of any simple type
// with the necessary pieces added to automatically generate a form

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

	//@Kind-annotated static field and static accessor method
	@Kind
	private static Form<FooType> form;
	
	public static Form<FooType> form() {
		if(form == null) {
			form = Form.forClass(FooType.class);
		}
		return form;
	}

	@Override
	public String toString() {
		return Recon.toString(form().mold(this));
	}
}