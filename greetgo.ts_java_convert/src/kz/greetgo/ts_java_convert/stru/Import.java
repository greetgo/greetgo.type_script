package kz.greetgo.ts_java_convert.stru;


import kz.greetgo.ts_java_convert.TsFileReference;

import java.util.Objects;

public class Import {
  public final String className;
  public final TsFileReference tsFileReference;
  public final int lineNo;

  public Import(String className, TsFileReference tsFileReference, int lineNo) {
    this.className = className;
    this.tsFileReference = tsFileReference;
    this.lineNo = lineNo;
  }

  public ClassStructure toClassStru() {
    return Objects.requireNonNull(tsFileReference.classStructure);
  }
}
