package swim.basic;

import swim.recon.Recon;
import swim.structure.Form;
import swim.structure.Kind;
import swim.structure.Member;
import swim.structure.Tag;

// Fundamentally a simple Java class, with a small number of fields of any simple type
// with the necessary pieces added to automatically generate a form

@Tag("fooType")
public class FooType {

  // @Kind-annotated static field and static accessor method
  @Kind private static Form<FooType> form;
  @Member("int")
  private int i = 0;
  @Member("string")
  private String s = "";

  public FooType() {}

  public FooType(int i, String s) {
    this.i = i;
    this.s = s;
  }

  public static Form<FooType> form() {
    if (form == null) {
      form = Form.forClass(FooType.class);
    }
    return form;
  }

  public int getNumber() {
    return this.i;
  }

  public String getString() {
    return this.s;
  }

  @Override
  public String toString() {
    return Recon.toString(form().mold(this));
  }
}
