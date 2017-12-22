package kz.greetgo.ts_java_convert.errors;

public class NumberCannotBeMultipleArray extends ParseError {
  public NumberCannotBeMultipleArray(String place) {
    super("Number cannot be multiple array at " + place);
  }
}
