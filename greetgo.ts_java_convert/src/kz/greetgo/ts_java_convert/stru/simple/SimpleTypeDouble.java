package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeDouble extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return boxed ? Double.class.getSimpleName() : "double";
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeDouble instance = new SimpleTypeDouble();
  }

  public static SimpleTypeDouble get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeDouble() {}

  @Override
  public String toString() {
    return "DOUBLE";
  }
}
