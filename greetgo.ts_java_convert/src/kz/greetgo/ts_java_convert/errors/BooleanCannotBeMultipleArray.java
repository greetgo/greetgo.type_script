package kz.greetgo.ts_java_convert.errors;

public class BooleanCannotBeMultipleArray extends ParseError {
  public BooleanCannotBeMultipleArray(String place) {
    super("Boolean cannot be multiple array at " + place);
  }
}
