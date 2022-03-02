package swim.basic;

import swim.recon.Recon;
import swim.structure.Form;
import swim.structure.Kind;
import swim.structure.Member;
import swim.structure.Tag;

@Tag("bazType")
public class BazType {

  @Member("fooType")
  private FooType f = new FooType();
  @Member("barType")
  private BarType b = new BarType();

  public BazType() {
  }

  public BazType(FooType f, BarType b) {
    this.f = f;
    this.b = b;
  }

  public FooType getFooType() {
    return this.f;
  }

  public BarType getBarType() {
    return this.b;
  }

  //@Kind-annotated static field and static accessor method
  @Kind
  private static Form<BazType> form;

  public static Form<BazType> form() {
    if (form == null) {
      form = Form.forClass(BazType.class);
    }
    return form;
  }

  @Override
  public String toString() {
    return Recon.toString(form().mold(this));
  }

}
