package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeBoxedBoolean extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return Boolean.class.getSimpleName();
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeBoxedBoolean instance = new SimpleTypeBoxedBoolean();
  }

  public static SimpleTypeBoxedBoolean get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeBoxedBoolean() {
  }

  @Override
  public String toString() {
    return "BOXED_BOOL";
  }
}
