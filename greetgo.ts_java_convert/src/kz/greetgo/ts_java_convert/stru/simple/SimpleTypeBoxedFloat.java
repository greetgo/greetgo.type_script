package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeBoxedFloat extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return Float.class.getSimpleName();
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeBoxedFloat instance = new SimpleTypeBoxedFloat();
  }

  public static SimpleTypeBoxedFloat get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeBoxedFloat() {
  }

  @Override
  public String toString() {
    return "BOXED_FLOAT";
  }
}
