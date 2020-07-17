/**
 * TODO: write a javadoc
 */

@Tag("barType")
public class BarType {
	
	// TODO: the final vars were not specified in the git issue, but were included in the example Token class
	// they also come up in the auxiliary class, are they necessary?
	
	// the empty obj seems helpful for readabilty purposes
	public static final BarType EMPTY = new BarType("","","");

	// however, FORM below seems covered by the "if(form==null)" logic already
	//	public static final BarTypeForm = FORM = new BarTypeForm();
	
	private int i = 0;
	private String s = "";
	private int j = 0;
	
	public BarType() {
	}
	
	public BarType(int i, String s) {
		this.i = i;
		this.s = s;
		this.j = j;
	}
	
	public int getNumber1() {
		return i;
	}
	
	public String getString() {
		return s;
	}
	
	public int getNumber2() {
		return j;
	}
	
	@Kind
	private static Form<BarType> form;
	
	public static Form<BarType> form() {
		if(form == null) {
			form = Form.forClass(BarType.class);
		}
		return form;
	}
}


class BarTypeForm extends Form<BarType> {
	
	@Override 
	public String tag() {
		return "barType";
	}
	
	@Override
	public Value mold(BarType barType) {
		return Record.create(3)
				.attr(tag())
				.slot("i", barType.getNumber1())
				.slot("s", barType.getString())
				.slot("j", barType.getNumber2())
	}
	
	@Override
	public BarType cast(Item value) {
		try {
			final Attr attr = (Attr) value.head();
			final String barType = attr.getKey().stringValue("");
			if (!tag().equals(barType)) {
				return BarType.EMPTY;
			}
			BarType b = new BarType(value.get("i").intValue(0),
					value.get("s").stringValue(""),
					value.get("j").intValue(0));
		} catch (Exception e) {
			return BarType.EMPTY;
		}
	}
}