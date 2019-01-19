package kz.greetgo.ts_java_convert.errors;

import java.io.File;

public class NoDefinitionInTsFile extends RuntimeException {
  public NoDefinitionInTsFile(File tsFile) {
    super("No class or enum definition in " + tsFile
      + "\n\t\tAdd line `///ignore file` at this file to ignore this file");
  }
}
