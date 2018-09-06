package kz.greetgo.ts_java_convert.stru.simple;


import kz.greetgo.ts_java_convert.stru.SimpleType;

import java.math.BigDecimal;

public class SimpleTypeBd extends SimpleType {
  @Override
  public String javaName(boolean boxed) {
    return BigDecimal.class.getSimpleName();
  }

  private enum Wrapper {
    ELEMENT;

    private final SimpleTypeBd instance = new SimpleTypeBd();
  }

  public static SimpleTypeBd get() {
    return Wrapper.ELEMENT.instance;
  }

  private SimpleTypeBd() {}

  @Override
  public String toString() {
    return "BIG_DECIMAL";
  }
}
