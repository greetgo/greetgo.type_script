package kz.greetgo.ts_java_convert;

import kz.greetgo.ts_java_convert.test_ConvertModel.ConvertModelDir;
import org.testng.annotations.Test;

import java.io.File;

import static java.util.Arrays.asList;

public class ConvertModel_exec_Test {
  @Test
  public void test1() throws Exception {

    ConvertModelDir dir = new ConvertModelDir(true);
    File anotherClass = dir.read("sub3/AnotherClass.ts");
    File mainClass = dir.read("sub3/MainClass.ts");
    File someEnumClass = dir.read("sub3/SomeEnum.ts");

    TsFileReference another = new TsFileReference(anotherClass, "sub3", "AnotherClass");
    TsFileReference main = new TsFileReference(mainClass, "sub3", "MainClass");
    TsFileReference someEnum = new TsFileReference(someEnumClass, "sub3", "SomeEnum");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");
    convertModel.defineStructure(asList(main, another, someEnum));

    convertModel.generate(another.classStructure, dir.destinationDir());
    convertModel.generate(main.classStructure, dir.destinationDir());
    convertModel.generate(someEnum.classStructure, dir.destinationDir());

  }

  @Test
  public void test2() throws Exception {

    ConvertModelDir dir = new ConvertModelDir(true);
    dir.read("sub3/AnotherClass.ts");
    dir.read("sub3/MainClass.ts");
    dir.read("sub3/SomeEnum.ts");

    new ConvertModelBuilder()
      .sourceDir(dir.sourceDir())
      .destinationDir(dir.destinationDir())
      .destinationPackage("kz.greetgo.generations")
      .create().execute()
    ;

  }
}
