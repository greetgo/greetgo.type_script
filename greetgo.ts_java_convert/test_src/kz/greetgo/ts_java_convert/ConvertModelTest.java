package kz.greetgo.ts_java_convert;

import kz.greetgo.ts_java_convert.stru.ClassAttr;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedInt;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedLong;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeInt;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeLong;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeStr;
import kz.greetgo.ts_java_convert.test_ConvertModel.ConvertModelDir;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //

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

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //

    assertThat(fr.classStructure).isNotNull();

    Map<String, ClassAttr> attrMap = fr.classStructure
      .attrList.stream().collect(toMap(f -> f.name, Function.identity()));

    {
      ClassAttr attr = attrMap.get("strField");
      assertThat(attr).isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
      assertThat(attr.isArray).isFalse();
      assertThat(attr.comment).isEqualTo(asList("  /**", "   * string field", "   * line 2 of comment", "   */"));
    }
    {
      ClassAttr attr = attrMap.get("strOrNullField");
      assertThat(attr).isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
      assertThat(attr.isArray).isFalse();
      assertThat(attr.comment).isEmpty();
    }
    {
      ClassAttr attr = attrMap.get("strOrNullField2");
      assertThat(attr).isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
      assertThat(attr.isArray).isFalse();
      assertThat(attr.comment).isEmpty();
    }

    {
      ClassAttr attr = attrMap.get("strArrayField");
      assertThat(attr).isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
      assertThat(attr.isArray).isTrue();
      assertThat(attr.comment).isEmpty();
    }
    {
      ClassAttr attr = attrMap.get("strArrayOrNullField");
      assertThat(attr).isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
      assertThat(attr.isArray).isTrue();
      assertThat(attr.comment).isEmpty();
    }
    {
      ClassAttr attr = attrMap.get("strArrayOrNullField2");
      assertThat(attr).isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
      assertThat(attr.isArray).isTrue();
      assertThat(attr.comment).isEmpty();
    }

    List<String> attrNames = fr.classStructure.attrList.stream().map(a -> a.name).collect(toList());
    assertThat(attrNames).containsExactly(
      "strField",
      "strOrNullField",
      "strOrNullField2",
      "strArrayField",
      "strArrayOrNullField",
      "strArrayOrNullField2"
    );
  }

  @DataProvider
  public Object[][] defineStructure_numberField_DP() {
    return new Object[][]{
      {"numberIntField", SimpleTypeInt.class, false, true},
      {"numberIntArrayField1", SimpleTypeInt.class, true, false},
      {"numberIntArrayField2", SimpleTypeInt.class, true, false},
      {"numberIntOrNullField1", SimpleTypeBoxedInt.class, false, false},
      {"numberIntOrNullField2", SimpleTypeBoxedInt.class, false, false},
      {"numberIntOrNullField3", SimpleTypeBoxedInt.class, false, false},
      {"numberLongField", SimpleTypeLong.class, false, false},
      {"numberLongArrayField1", SimpleTypeLong.class, true, false},
      {"numberLongArrayField2", SimpleTypeLong.class, true, false},
      {"numberLongOrNullField1", SimpleTypeBoxedLong.class, false, false},
      {"numberLongOrNullField2", SimpleTypeBoxedLong.class, false, false},
      {"numberLongOrNullField3", SimpleTypeBoxedLong.class, false, false},

    };
  }

  @Test(dataProvider = "defineStructure_numberField_DP")
  public void defineStructure_numberField(String fieldName,
                                          Class<?> fieldClass,
                                          boolean isArray,
                                          boolean checkFieldExists) throws Exception {

    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/ClassWithNumberField.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "ClassWithNumberField");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //

    assertThat(fr.classStructure).isNotNull();

    Map<String, ClassAttr> attrMap = fr.classStructure
      .attrList.stream().collect(toMap(f -> f.name, Function.identity()));

    {
      ClassAttr attr = attrMap.get(fieldName);
      assertThat(attr).isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(fieldClass.getName());
      assertThat(attr.isArray).isEqualTo(isArray);
      assertThat(attr.comment).isEmpty();
    }

    if (!checkFieldExists) return;

    List<String> attrNames = fr.classStructure.attrList.stream().map(a -> a.name).collect(toList());
    assertThat(attrNames).containsExactly(
      "numberIntField",
      "numberIntArrayField1",
      "numberIntArrayField2",
      "numberIntOrNullField1",
      "numberIntOrNullField2",
      "numberIntOrNullField3",
      "numberLongField",
      "numberLongArrayField1",
      "numberLongArrayField2",
      "numberLongOrNullField1",
      "numberLongOrNullField2",
      "numberLongOrNullField3"
    );
  }

}