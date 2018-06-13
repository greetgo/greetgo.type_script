package kz.greetgo.ts_java_convert.stru;


public class TypeDate extends TypeStructure {
  private enum Wrapper {
    ELEMENT;

    private final TypeDate instance = new TypeDate();
  }

  public static TypeDate get() {
    return Wrapper.ELEMENT.instance;
  }

  private TypeDate() {}

  @Override
  public String toString() {
    return "Date";
  }
}
