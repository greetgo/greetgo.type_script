package kz.greetgo.ts_java_convert.stru;


import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBd;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoolean;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedBoolean;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedDouble;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedFloat;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedInt;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedLong;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeDouble;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeFloat;
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

      case "double":
        return boxed ? SimpleTypeBoxedDouble.get() : SimpleTypeDouble.get();

      case "float":
        return boxed ? SimpleTypeBoxedFloat.get() : SimpleTypeFloat.get();

      case "bd":
        return SimpleTypeBd.get();

      default:
        throw new IllegalArgumentException("Unknown type for Java: " + strType + " at " + place);
    }
  }

  public abstract String javaName(boolean boxed);
}
