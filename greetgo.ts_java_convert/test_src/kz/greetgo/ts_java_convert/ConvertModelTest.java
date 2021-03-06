package kz.greetgo.ts_java_convert;

import kz.greetgo.ts_java_convert.errors.NoDefinitionInTsFile;
import kz.greetgo.ts_java_convert.test_ConvertModel.ConvertModelDir;
import kz.greetgo.ts_java_convert.test_arrays.ModelsForTestArray;
import kz.greetgo.ts_java_convert.test_bpm_manager.PathWithBpmManager;
import kz.greetgo.util.RND;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.fest.assertions.api.Assertions.assertThat;

public class ConvertModelTest {

  private static void killEmptyLinesInEnd(List<String> lines) {
    while (!lines.isEmpty() && lines.get(lines.size() - 1).length() == 0) {
      lines.remove(lines.size() - 1);
    }
  }

  @Test
  public void readLeaveFurther() {
    String content = "package kz.greetgo.wow.sub3;\n" +
      "\n" +
      "\n" +
      "public class AnotherClass {\n" +
      "  public int intField;\n" +
      "\n" +
      "  //The following code would be not removed after regenerating\n" +
      "  ///LEAVE_FURTHER\n" +
      "\n" +
      "  public void asd() {}\n" +
      "\n" +
      "}\n";

    //
    //
    List<String> actualLeaveFurther = ConvertModel.readLeaveFurther(
      Arrays.stream(content.split("\n")).collect(toList())
    );
    //
    //

    List<String> expectedLeaveFurther = new ArrayList<>();
    expectedLeaveFurther.add("");
    expectedLeaveFurther.add("  public void asd() {}");
    expectedLeaveFurther.add("");
    expectedLeaveFurther.add("}");

    assertThat(actualLeaveFurther).isEqualTo(expectedLeaveFurther);
  }

  @Test
  public void testLeaveFurther() throws Exception {

    ConvertModelDir dir = new ConvertModelDir(true);
    File class1File = dir.read("sub1/Class1.ts");

    TsFileReference class1 = new TsFileReference(class1File, "sub1", "Class1");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), null, dir.destinationDir(), "kz.greetgo.test001");
    convertModel.defineStructure(singletonList(class1));

    String destinationDir = "testLeaveFurther_destination_" + RND.intStr(10);

    //
    //
    File javaFile = convertModel.generate(class1.classStructure, dir.destinationDir(destinationDir));
    //
    //

    {
      List<String> lines = Files.readAllLines(javaFile.toPath(), UTF_8);
      killEmptyLinesInEnd(lines);
      assertThat(lines.get(lines.size() - 1)).isEqualTo("}");

      assertBracketPairs(lines);

      assertThat(lines.get(lines.size() - 1)).isEqualTo("}");

      assertThat(lines.stream().filter(s -> s.contains("///LEAVE_FURTHER")).count()).isEqualTo(1);

      lines.remove(lines.size() - 1);

      lines.add("  public void helloWorldTestMethod() {}");
      lines.add("  public void byWorldTestMethod() {}");
      lines.add("}");
      lines.add("");

      Files.write(javaFile.toPath(), String.join("\n", lines).getBytes(UTF_8), CREATE);
    }

    //
    //
    File javaFile2 = convertModel.generate(class1.classStructure, dir.destinationDir(destinationDir));
    //
    //

    assertThat(javaFile2).isEqualTo(javaFile);

    {
      List<String> lines = Files.readAllLines(javaFile.toPath(), UTF_8);
      killEmptyLinesInEnd(lines);
      assertThat(lines.get(lines.size() - 1)).isEqualTo("}");
      assertBracketPairs(lines);

      String content = String.join("\n", lines);

      assertThat(content).contains("public void helloWorldTestMethod() {}");
      assertThat(content).contains("public void byWorldTestMethod() {}");

      assertThat(lines.stream().filter(s -> s.contains("///LEAVE_FURTHER")).count()).isEqualTo(1);
    }
  }

  private void assertBracketPairs(List<String> lines) {
    int openCount = 0, closeCount = 0;
    for (char c : String.join("", lines).toCharArray()) {
      if (c == '{') {
        openCount++;
      }
      if (c == '}') {
        closeCount++;
      }
    }

    assertThat(openCount).isEqualTo(closeCount);
  }

  @Test
  public void generate_date() throws Exception {

    ConvertModelDir dir = new ConvertModelDir(true);
    File class1File = dir.read("sub2/ClassWithDateField.ts");

    TsFileReference class1 = new TsFileReference(class1File, "sub2", "ClassWithDateField");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), null, dir.destinationDir(), "kz.greetgo.test001");
    convertModel.defineStructure(singletonList(class1));

    String destinationDir = "generate_date_destination_" + RND.intStr(10);

    //
    //
    File javaFile = convertModel.generate(class1.classStructure, dir.destinationDir(destinationDir));
    //
    //

    String content = new String(Files.readAllBytes(javaFile.toPath()), UTF_8);

    assertThat(content).contains("import " + Date.class.getName() + ";");
    assertThat(content).contains("public Date dateField1;");
    assertThat(content).contains("public Date dateField2;");
  }

  @Test
  public void test_ConvertModelBuilder_with_BpmManager() throws Exception {
    PathWithBpmManager dir = new PathWithBpmManager();
    dir.read("model/BpmListRow.ts");
    dir.read("model/bpm/manager/BpmManagerContent.ts");
    dir.read("model/bpm/manager/BpmRecord.ts");

    File sourceDir = dir.sourceDir();
    File destinationDir = dir.destinationDir();
    String destinationPackage = "kz.greetgo.bpm.models";

    ConvertModel convertModel = new ConvertModelBuilder()
      .sourceDir(sourceDir, "model")
      .destinationDir(destinationDir)
      .destinationPackage(destinationPackage)
      .create();

    convertModel.execute();

    Path javaFile = dir.destinationDir().toPath().resolve("kz/greetgo/bpm/models/BpmListRow.java");

    List<String> lines = Files.readAllLines(javaFile);

    lines.set(2, "import asd.asd.Wow;");

    Files.write(javaFile, lines);

    assertThat(lines.get(2)).isEqualTo("import asd.asd.Wow;");

    convertModel.execute();

    lines = Files.readAllLines(javaFile);

    assertThat(lines.get(2)).isEqualTo("import asd.asd.Wow;");
  }

  @Test(expectedExceptions = NoDefinitionInTsFile.class)
  public void test_ConvertModelBuilder_NoDefinitionInTsFile() throws Exception {
    PathWithBpmManager dir = new PathWithBpmManager();
    dir.read("model/BpmListRow.ts");
    dir.read("model/createEmptyBpmListRow.ts");
    dir.read("model/bpm/manager/BpmManagerContent.ts");
    dir.read("model/bpm/manager/BpmRecord.ts");

    File sourceDir = dir.sourceDir();
    File destinationDir = dir.destinationDir();
    String destinationPackage = "kz.greetgo.bpm.models";

    ConvertModel convertModel = new ConvertModelBuilder()
      .sourceDir(sourceDir, "model")
      .destinationDir(destinationDir)
      .destinationPackage(destinationPackage)
      .create();

    convertModel.execute();
  }

  @Test
  public void test_ConvertModelBuilder_fileWithoutDefinition() throws Exception {
    PathWithBpmManager dir = new PathWithBpmManager();
    dir.read("model/BpmListRow.ts");
    dir.read("model/bpm/manager/BpmManagerContent.ts");
    dir.read("model/bpm/manager/BpmRecord.ts");
    dir.read("model/bpm/manager/createBpmRecord.ts");

    File sourceDir = dir.sourceDir();
    File destinationDir = dir.destinationDir();
    String destinationPackage = "kz.greetgo.bpm.models";

    ConvertModel convertModel = new ConvertModelBuilder()
      .sourceDir(sourceDir, "model")
      .destinationDir(destinationDir)
      .destinationPackage(destinationPackage)
      .create();

    convertModel.execute();
  }

  @Test
  public void test_ConvertModelBuilder_extends() throws Exception {
    PathWithBpmManager dir = new PathWithBpmManager();
    dir.read("model2/base/BaseClass.ts");
    dir.read("model2/ChildClass0.ts");
    dir.read("model2/ChildClass1.ts");
    dir.read("model2/ChildClass2.ts");
    dir.read("model2/OnlyInterfaceClass1.ts");
    dir.read("model2/OnlyInterfaceClass2.ts");
    dir.read("out_of_model/SuperTestInterface1.ts");
    dir.read("out_of_model/SuperTestInterface2.ts");

    File sourceDir = dir.sourceDir();
    File destinationDir = dir.destinationDir();
    String destinationPackage = "kz.greetgo.bpm.models2";

    ConvertModel convertModel = new ConvertModelBuilder()
      .sourceDir(sourceDir, "model2")
      .destinationDir(destinationDir)
      .destinationPackage(destinationPackage)
      .create();

    convertModel.execute();

    Path packageDir = dir.destinationDir().toPath().resolve("kz/greetgo/bpm/models2");

    {
      Path javaFile = packageDir.resolve("ChildClass0.java");

      String javaContent = String.join("\n", Files.readAllLines(javaFile));

      assertThat(javaContent).contains("class ChildClass0 extends BaseClass");
      assertThat(javaContent).contains("import kz.greetgo.bpm.models2.base.BaseClass;");
    }
    {
      Path javaFile = packageDir.resolve("ChildClass1.java");

      String javaContent = String.join("\n", Files.readAllLines(javaFile));

      assertThat(javaContent).contains("class ChildClass1 extends BaseClass");
      assertThat(javaContent).contains("import kz.greetgo.bpm.models2.base.BaseClass;");
    }
    {
      Path javaFile = packageDir.resolve("ChildClass2.java");

      String javaContent = String.join("\n", Files.readAllLines(javaFile));

      assertThat(javaContent).contains("class ChildClass2 extends BaseClass");
      assertThat(javaContent).contains("import kz.greetgo.bpm.models2.base.BaseClass;");
    }

    assertThat(packageDir.resolve("OnlyInterfaceClass1.java").toFile()).exists();
    assertThat(packageDir.resolve("OnlyInterfaceClass2.java").toFile()).exists();
  }

  @Test
  public void test_ConvertModelBuilder_importInSamePackageDoNotPrint() throws Exception {
    PathWithBpmManager dir = new PathWithBpmManager();
    dir.read("model3/working_package/MainClass.ts");
    dir.read("model3/working_package/RefInSamePackage.ts");
    dir.read("model3/working_package/sub_package/RefInSubPackage.ts");
    dir.read("model3/RefInParentPackage.ts");
    dir.read("model3/parallel_package/RefInParallelPackage.ts");

    File sourceDir = dir.sourceDir();
    File destinationDir = dir.destinationDir();
    String destinationPackage = "kz.greetgo.bpm3";

    ConvertModel convertModel = new ConvertModelBuilder()
      .sourceDir(sourceDir, "model3")
      .destinationDir(destinationDir)
      .destinationPackage(destinationPackage)
      .create();

    convertModel.execute();

    Path packageDir = dir.destinationDir().toPath().resolve("kz/greetgo/bpm3/working_package/");

    Path javaFile = packageDir.resolve("MainClass.java");

    String javaContent = String.join("\n", Files.readAllLines(javaFile));

    assertThat(javaContent).contains("import kz.greetgo.bpm3.RefInParentPackage;");
    assertThat(javaContent).contains("import kz.greetgo.bpm3.parallel_package.RefInParallelPackage;");
    assertThat(javaContent).contains("import kz.greetgo.bpm3.working_package.sub_package.RefInSubPackage;");

    assertThat(javaContent).doesNotContain("import kz.greetgo.bpm3.working_package.RefInSamePackage;");
  }

  @Test
  public void testName() throws Exception {
    ModelsForTestArray dir = new ModelsForTestArray();
    dir.read("model/ArrayElement.ts");
    dir.read("model/EntityWithArray.ts");

    File sourceDir = dir.sourceDir();
    File destinationDir = dir.destinationDir();
    String destinationPackage = "pack.one";

    ConvertModel convertModel = new ConvertModelBuilder()
      .sourceDir(sourceDir, "model")
      .destinationDir(destinationDir)
      .destinationPackage(destinationPackage)
      .create();

    convertModel.execute();

    Path javaFilePath = dir.destinationDir().toPath().resolve("pack/one/EntityWithArray.java");

    String javaContent = String.join("\n", Files.readAllLines(javaFilePath));

    assertThat(javaContent).contains("public List<ArrayElement> arrayAtr = new ArrayList<>();");
  }
}