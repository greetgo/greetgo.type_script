package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeBoolean extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return boxed ? Boolean.class.getSimpleName() : "boolean";
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeBoolean instance = new SimpleTypeBoolean();
  }

  public static SimpleTypeBoolean get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeBoolean() {
  }

  @Override
  public String toString() {
    return "BOOL";
  }
}
