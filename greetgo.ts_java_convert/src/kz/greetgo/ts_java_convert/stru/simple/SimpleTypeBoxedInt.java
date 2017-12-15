package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

public class SimpleTypeBoxedInt extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return Integer.class.getSimpleName();
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeBoxedInt instance = new SimpleTypeBoxedInt();
  }

  public static SimpleTypeBoxedInt get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeBoxedInt() {
  }

  @Override
  public String toString() {
    return "BOXED_INT";
  }
}
