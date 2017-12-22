package kz.greetgo.ts_java_convert.errors;

public class CommaAtLastEnumElement extends ParseError {
  public CommaAtLastEnumElement(String place) {
    super("Comma at last element of an enum MUST BE: " + place);
  }
}
