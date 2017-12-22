package kz.greetgo.ts_java_convert.errors;

import java.io.File;

public class NoFileInImport extends ParseError {
  public NoFileInImport(File importedFile, String place) {
    super("No file " + importedFile + " in import at " + place);
  }
}
