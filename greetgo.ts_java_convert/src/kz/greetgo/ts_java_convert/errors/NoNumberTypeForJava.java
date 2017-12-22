package kz.greetgo.ts_java_convert.errors;

public class NoNumberTypeForJava extends ParseError {
  public NoNumberTypeForJava(String place) {
    super(createMessage(place));
  }

  private static String createMessage(String place) {
    return "No number type for Java at " + place + "\n" +
      "  Examples:\n" +
      "    public numberIntField: number /* int */;\n" +
      "    public numberIntArrayField1: number /* int */ [];\n" +
      "    public numberIntArrayField2: number [] /* int */;\n" +
      "    public numberIntOrNullField1: number /* int */ | null;\n" +
      "    public numberIntOrNullField2: number | /* int */ null;\n" +
      "    public numberIntOrNullField3: number | null /* int */;\n" +
      "    public numberIntOrNullField4: null | number /* int */;\n" +
      "    public numberLongField: number /* long */;\n" +
      "    public numberLongArrayField1: number /* long */ [];\n" +
      "    public numberLongArrayField2: number [] /* long */;\n" +
      "    public numberLongOrNullField1: number /* long */ | null;\n" +
      "    public numberLongOrNullField2: number | /* long */ null;\n" +
      "    public numberLongOrNullField3: number | null /* long */;\n" +
      "    public numberLongOrNullField4: null | number /* long */;\n";
  }
}
