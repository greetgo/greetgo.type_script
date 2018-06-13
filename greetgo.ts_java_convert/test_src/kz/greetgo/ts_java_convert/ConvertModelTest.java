package kz.greetgo.ts_java_convert;

import kz.greetgo.ts_java_convert.test_ConvertModel.ConvertModelDir;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
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

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.test001");
    convertModel.defineStructure(singletonList(class1));

    String destinationDir = "testLeaveFurther_destination";

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

      lines.remove(lines.size() - 1);

      lines.add("  public void helloWorldTestMethod() {}");
      lines.add("  public void byWorldTestMethod() {}");
      lines.add("}");
      lines.add("");

      Files.write(javaFile.toPath(), lines.stream().collect(joining("\n")).getBytes(UTF_8), CREATE);
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

      String content = lines.stream().collect(joining("\n"));

      assertThat(content).contains("public void helloWorldTestMethod() {}");
      assertThat(content).contains("public void byWorldTestMethod() {}");

      assertThat(lines.stream().filter(s -> s.contains("///LEAVE_FURTHER")).count()).isEqualTo(1);
    }
  }

  private void assertBracketPairs(List<String> lines) {
    int openCount = 0, closeCount = 0;
    for (char c : lines.stream().collect(joining()).toCharArray()) {
      if (c == '{') openCount++;
      if (c == '}') closeCount++;
    }

    assertThat(openCount).isEqualTo(closeCount);
  }

}