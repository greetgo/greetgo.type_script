package kz.greetgo.ts_java_convert.stru;


import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoolean;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedBoolean;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedInt;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedLong;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeInt;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeLong;

public abstract class SimpleType extends TypeStructure {
  public static SimpleType fromStr(String strType, boolean boxed, String place) {
    switch (strType) {
      case "int":
        return boxed ? SimpleTypeBoxedInt.get() : SimpleTypeInt.get();

      case "long":
        return boxed ? SimpleTypeBoxedLong.get() : SimpleTypeLong.get();

      case "boolean":
        return boxed ? SimpleTypeBoxedBoolean.get() : SimpleTypeBoolean.get();

      default:
        throw new IllegalArgumentException("Unknown type for Java: " + strType + " at " + place);
    }
  }

  public abstract String javaName(boolean boxed);
}
