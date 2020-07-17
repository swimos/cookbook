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