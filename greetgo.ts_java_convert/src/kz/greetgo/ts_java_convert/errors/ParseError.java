package kz.greetgo.ts_java_convert.errors;

public class ParseError extends RuntimeException {
  public ParseError(String message) {
    super(message);
  }
}
