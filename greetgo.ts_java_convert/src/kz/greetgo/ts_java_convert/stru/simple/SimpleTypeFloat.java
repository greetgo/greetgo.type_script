package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeFloat extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return boxed ? Float.class.getSimpleName() : "float";
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeFloat instance = new SimpleTypeFloat();
  }

  public static SimpleTypeFloat get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeFloat() {}

  @Override
  public String toString() {
    return "FLOAT";
  }
}
