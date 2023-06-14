package swim.basic;

import swim.recon.Recon;
import swim.structure.Attr;
import swim.structure.Form;
import swim.structure.Item;
import swim.structure.Kind;
import swim.structure.Record;
import swim.structure.Value;

// Another simple Java class distinguishable from FooType

public class BarType {

  private static Form<BarType> form = new BarTypeForm();
  private int i = 0;
  private String s = "";
  private int j = 0;

  public BarType() {}

  public BarType(int i, String s, int j) {
    this.i = i;
    this.s = s;
    this.j = j;
  }

  @Kind
  public static Form<BarType> form() {
    return form;
  }

  public int getNumber1() {
    return this.i;
  }

  public String getString() {
    return this.s;
  }

  public int getNumber2() {
    return this.j;
  }

  @Override
  public String toString() {
    return Recon.toString(form().mold(this));
  }
}

// Auxiliary class to BarType with overridden methods
class BarTypeForm extends Form<BarType> {

  @Override
  public String tag() {
    return "barType";
  }

  @Override
  public Class<?> type() {
    return BarType.class;
  }

  // mold method creates a Value from the fields of the BarType object argument
  @Override
  public Value mold(BarType barType) {
    return Record.create(4)
        .attr(tag())
        .slot("i", barType.getNumber1())
        .slot("s", barType.getString())
        .slot("j", barType.getNumber2());
  }

  // the cast method is the inverse of mold, converts Value -> BarType object
  @Override
  public BarType cast(Item value) {
    try {
      final Attr attr = (Attr) value.head();
      final String barType = attr.getKey().stringValue("");
      if (!tag().equals(barType)) {
        return null;
      }
      return new BarType(
          value.get("i").intValue(0), value.get("s").stringValue(""), value.get("j").intValue(0));
    } catch (Exception e) {
      return null;
    }
  }
}
