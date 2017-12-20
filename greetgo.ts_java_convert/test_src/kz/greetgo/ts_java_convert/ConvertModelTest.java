package kz.greetgo.ts_java_convert;

import kz.greetgo.ts_java_convert.stru.ClassAttr;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeStr;
import kz.greetgo.ts_java_convert.test_ConvertModel.ConvertModelDir;
import org.testng.annotations.Test;

import java.io.File;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.fest.assertions.api.Assertions.assertThat;

@SuppressWarnings("RedundantThrows")
public class ConvertModelTest {
  @Test
  public void defineStructure() throws Exception {

    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub1/Class1.ts");

    TsFileReference fr = new TsFileReference(class1, "sub1", "Class1");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    convertModel.defineStructure(singletonList(fr));

    assertThat(fr.classStructure).isNotNull();
    assertThat(fr.sourceDir).isEqualTo(dir.sourceDir());

    assertThat(fr.classStructure.aPackage).isEqualTo("kz.greetgo.wow.sub1");
    assertThat(fr.classStructure.name).isEqualTo("Class1");
    assertThat(fr.classStructure.classComment).isEqualTo(asList("/**", " * Hello world", " * By world", " */"));
    assertThat(fr.classStructure.hasPackage()).isTrue();
    assertThat(fr.classStructure.fullName()).isEqualTo("kz.greetgo.wow.sub1.Class1");
    assertThat(fr.classStructure.attrList).hasSize(1);
    assertThat(fr.classStructure.attrList.get(0).isArray).isFalse();
    assertThat(fr.classStructure.attrList.get(0).name).isEqualTo("field");
    assertThat(fr.classStructure.attrList.get(0).type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
  }

  @Test
  public void defineStructure_stringField() throws Exception {

    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/ClassWithStringField.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "ClassWithStringField");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    convertModel.defineStructure(singletonList(fr));

    assertThat(fr.classStructure).isNotNull();


    assertThat(fr.classStructure.attrList).hasSize(1);

    ClassAttr attr = fr.classStructure.attrList.get(0);

    assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
    assertThat(attr.isArray).isFalse();
    assertThat(attr.name).isEqualTo("strField");
    assertThat(attr.comment).isEqualTo(asList("  /**", "   * string field", "   * line 2 of comment", "   */"));
  }
}