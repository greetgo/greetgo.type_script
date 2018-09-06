package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeBoxedDouble extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return Double.class.getSimpleName();
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeBoxedDouble instance = new SimpleTypeBoxedDouble();
  }

  public static SimpleTypeBoxedDouble get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeBoxedDouble() {}

  @Override
  public String toString() {
    return "BOXED_DOUBLE";
  }
}
