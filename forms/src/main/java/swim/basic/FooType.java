/**
 * TODO: write a javadoc
 */

@Tag("fooType")
public class FooType {
	private int numberVal = 0;
	private String stringType = "";
	
	public FooType() {
	}
	
	public FooType(int numberVal, String stringType) {
		this.numberVal = numberVal;
		this.stringType = stringType;
	}
	
	public int getNumber() {
		return numberVal;
	}
	
	public String getString() {
		return stringVal;
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