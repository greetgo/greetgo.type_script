package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeLong extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return boxed ? Long.class.getSimpleName() : "long";
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeLong instance = new SimpleTypeLong();
  }

  public static SimpleTypeLong get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeLong() {
  }

  @Override
  public String toString() {
    return "LONG";
  }
}
