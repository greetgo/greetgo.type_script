package kz.greetgo.ts_java_convert;

import kz.greetgo.ts_java_convert.errors.CommaAtLastEnumElement;
import kz.greetgo.ts_java_convert.errors.NoFileInImport;
import kz.greetgo.ts_java_convert.errors.NoNumberTypeForJava;
import kz.greetgo.ts_java_convert.errors.NumberCannotBeMultipleArray;
import kz.greetgo.ts_java_convert.stru.ClassAttr;
import kz.greetgo.ts_java_convert.stru.ClassStructure;
import kz.greetgo.ts_java_convert.stru.TypeDate;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoolean;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedBoolean;
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
public class ConvertModel_defineStructure_Test {
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

  @DataProvider
  public Object[][] defineStructure_stringField_DP() {
    return new Object[][]{
      {"strField", false},
      {"strOrNullField", false},
      {"strOrNullField2", false},
      {"strArrayField", true},
      {"strArrayOrNullField", true},
      {"strArrayOrNullField2", true},
      // with init
      {"strField_init", false},
      {"strOrNullField_init", false},
      {"strOrNullField2_init", false},
      {"strArrayField_init", true},
      {"strArrayOrNullField_init", true},
      {"strArrayOrNullField2_init", true},
    };
  }

  @Test(dataProvider = "defineStructure_stringField_DP")
  public void defineStructure_stringField(String fieldName, boolean isArray) throws Exception {

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
      ClassAttr attr = attrMap.get(fieldName);
      assertThat(attr).describedAs("Поле оказалось вообще не определённым").isNotNull();

      assertThat(attr.type.getClass().getName()).isEqualTo(SimpleTypeStr.class.getName());
      assertThat(attr.isArray).isEqualTo(isArray);
      assertThat(attr.comment).isEmpty();
    }

  }

  @Test
  public void defineStructure_checkAllFieldsIn_ClassWithStringField() throws Exception {

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

    List<String> attrNames = fr.classStructure.attrList.stream().map(a -> a.name).collect(toList());
    assertThat(attrNames).containsExactly(
      "strField",
      "strOrNullField",
      "strOrNullField2",
      "strArrayField",
      "strArrayOrNullField",
      "strArrayOrNullField2",
      //with init
      "strField_init",
      "strOrNullField_init",
      "strOrNullField2_init",
      "strArrayField_init",
      "strArrayOrNullField_init",
      "strArrayOrNullField2_init"
    );
  }

  @DataProvider
  public Object[][] defineStructure_numberField_DP() {
    return new Object[][]{
      {"numberIntField", SimpleTypeInt.class, false},
      {"numberIntArrayField1", SimpleTypeInt.class, true},
      {"numberIntArrayField2", SimpleTypeInt.class, true},
      {"numberIntOrNullField1", SimpleTypeBoxedInt.class, false},
      {"numberIntOrNullField2", SimpleTypeBoxedInt.class, false},
      {"numberIntOrNullField3", SimpleTypeBoxedInt.class, false},
      {"numberIntOrNullField4", SimpleTypeBoxedInt.class, false},
      {"numberLongField", SimpleTypeLong.class, false},
      {"numberLongArrayField1", SimpleTypeLong.class, true},
      {"numberLongArrayField2", SimpleTypeLong.class, true},
      {"numberLongOrNullField1", SimpleTypeBoxedLong.class, false},
      {"numberLongOrNullField2", SimpleTypeBoxedLong.class, false},
      {"numberLongOrNullField3", SimpleTypeBoxedLong.class, false},
      // with init
      {"numberIntField_init", SimpleTypeInt.class, false},
      {"numberIntArrayField1_init", SimpleTypeInt.class, true},
      {"numberIntArrayField2_init", SimpleTypeInt.class, true},
      {"numberIntOrNullField1_init", SimpleTypeBoxedInt.class, false},
      {"numberIntOrNullField2_init", SimpleTypeBoxedInt.class, false},
      {"numberIntOrNullField3_init", SimpleTypeBoxedInt.class, false},
      {"numberIntOrNullField4_init", SimpleTypeBoxedInt.class, false},
      {"numberLongField_init", SimpleTypeLong.class, false},
      {"numberLongArrayField1_init", SimpleTypeLong.class, true},
      {"numberLongArrayField2_init", SimpleTypeLong.class, true},
      {"numberLongOrNullField1_init", SimpleTypeBoxedLong.class, false},
      {"numberLongOrNullField2_init", SimpleTypeBoxedLong.class, false},
      {"numberLongOrNullField3_init", SimpleTypeBoxedLong.class, false},
    };
  }

  @Test(dataProvider = "defineStructure_numberField_DP")
  public void defineStructure_numberField(String fieldName,
                                          Class<?> fieldClass,
                                          boolean isArray) throws Exception {

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

    //noinspection Duplicates
    {
      assertThat(attrMap).containsKey(fieldName);
      ClassAttr attr = attrMap.get(fieldName);

      assertThat(attr.type.getClass().getName()).isEqualTo(fieldClass.getName());
      assertThat(attr.isArray).isEqualTo(isArray);
      assertThat(attr.comment).isEmpty();
    }
  }

  @Test(expectedExceptions = NumberCannotBeMultipleArray.class)
  public void multipleNumberArray() throws Exception {
    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/MultipleNumberArray.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "MultipleNumberArray");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //
  }

  @Test(expectedExceptions = NumberCannotBeMultipleArray.class)
  public void multipleNumberArray_withInit() throws Exception {
    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/MultipleNumberArrayWithInit.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "MultipleNumberArrayWithInit");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //
  }

  @Test(expectedExceptions = NoNumberTypeForJava.class)
  public void noNumberTypeForJava() throws Exception {
    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/NoNumberTypeForJava.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "NoNumberTypeForJava");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //
  }

  @Test(expectedExceptions = NoNumberTypeForJava.class)
  public void noNumberTypeForJava_withInit() throws Exception {
    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/NoNumberTypeForJavaWithInit.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "NoNumberTypeForJavaWithInit");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //
  }

  @Test
  public void defineStructure_checkAllFieldsIn_ClassWithNumberField() throws Exception {

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

    List<String> attrNames = fr.classStructure.attrList.stream().map(a -> a.name).collect(toList());
    assertThat(attrNames).containsExactly(
      "numberIntField",
      "numberIntArrayField1",
      "numberIntArrayField2",
      "numberIntOrNullField1",
      "numberIntOrNullField2",
      "numberIntOrNullField3",
      "numberIntOrNullField4",
      "numberLongField",
      "numberLongArrayField1",
      "numberLongArrayField2",
      "numberLongOrNullField1",
      "numberLongOrNullField2",
      "numberLongOrNullField3",
      "numberLongOrNullField4",
      // with init
      "numberIntField_init",
      "numberIntArrayField1_init",
      "numberIntArrayField2_init",
      "numberIntOrNullField1_init",
      "numberIntOrNullField2_init",
      "numberIntOrNullField3_init",
      "numberIntOrNullField4_init",
      "numberLongField_init",
      "numberLongArrayField1_init",
      "numberLongArrayField2_init",
      "numberLongOrNullField1_init",
      "numberLongOrNullField2_init",
      "numberLongOrNullField3_init",
      "numberLongOrNullField4_init"
    );
  }


  @DataProvider
  public Object[][] defineStructure_boolField_DP() {
    return new Object[][]{
      {"boolField", SimpleTypeBoolean.class, false},
      {"boolOrNullField", SimpleTypeBoxedBoolean.class, false},
      {"boolArrayField", SimpleTypeBoxedBoolean.class, true},
      {"boolArrayOrNullField", SimpleTypeBoxedBoolean.class, true},
      {"boolField_null", SimpleTypeBoxedBoolean.class, false},
      {"boolField_null_array", SimpleTypeBoxedBoolean.class, true},
      // with init
      {"boolField_init", SimpleTypeBoolean.class, false},
      {"boolOrNullField_init", SimpleTypeBoxedBoolean.class, false},
      {"boolArrayField_init", SimpleTypeBoxedBoolean.class, true},
      {"boolArrayOrNullField_init", SimpleTypeBoxedBoolean.class, true},
      {"boolField_null_init", SimpleTypeBoxedBoolean.class, false},
      {"boolField_null_array_init", SimpleTypeBoxedBoolean.class, true},
    };
  }

  @Test(dataProvider = "defineStructure_boolField_DP")
  public void defineStructure_boolField(String fieldName,
                                        Class<?> fieldClass,
                                        boolean isArray) throws Exception {

    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/ClassWithBooleanField.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "ClassWithBooleanField");

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

    //noinspection Duplicates
    {
      assertThat(attrMap).containsKey(fieldName);
      ClassAttr attr = attrMap.get(fieldName);

      assertThat(attr.type.getClass().getName()).isEqualTo(fieldClass.getName());
      assertThat(attr.isArray).isEqualTo(isArray);
      assertThat(attr.comment).isEmpty();
    }
  }

  @Test(expectedExceptions = NoFileInImport.class)
  public void defineStructure_classField_NoFileInImport() throws Exception {

    ConvertModelDir dir = new ConvertModelDir();
    File class1 = dir.read("sub2/ClassWithClassField.ts");

    TsFileReference fr = new TsFileReference(class1, "sub2", "ClassWithClassField");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    //
    //
    convertModel.defineStructure(singletonList(fr));
    //
    //
  }

  @DataProvider
  public Object[][] defineStructure_classField_DP() {
    return new Object[][]{
      {"field", false},
      {"fieldArray", true},
      {"fieldNull", false},
      {"fieldArrayNull1", true},
      {"fieldArrayNull2", true},
      {"nullField", false},
      {"nullFieldArray", true},
      //with init
      {"field_init", false},
      {"fieldArray_init", true},
      {"fieldNull_init", false},
      {"fieldArrayNull1_init", true},
      {"fieldArrayNull2_init", true},
      {"nullField_init", false},
      {"nullFieldArray_init", true},
    };
  }

  @Test(dataProvider = "defineStructure_classField_DP")
  public void defineStructure_classField(String fieldName,
                                         boolean isArray) throws Exception {

    ConvertModelDir dir = new ConvertModelDir();
    File anotherClass = dir.read("sub2/AnotherClass.ts");
    File class1 = dir.read("sub2/ClassWithClassField.ts");

    TsFileReference another = new TsFileReference(anotherClass, "sub2", "AnotherClass");
    TsFileReference fr = new TsFileReference(class1, "sub2", "ClassWithClassField");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    assertThat(fr.classStructure).isNull();

    //
    //
    convertModel.defineStructure(asList(fr, another));
    //
    //

    assertThat(fr.classStructure).isNotNull();

    Map<String, ClassAttr> attrMap = fr.classStructure
      .attrList.stream().collect(toMap(f -> f.name, Function.identity()));

    //noinspection Duplicates
    {
      assertThat(attrMap).containsKey(fieldName);
      ClassAttr attr = attrMap.get(fieldName);

      assertThat(attr.type.getClass().getName()).isEqualTo(ClassStructure.class.getName());
      assertThat(attr.isArray).isEqualTo(isArray);
      assertThat(attr.comment).isEmpty();
    }
  }

  @Test
  public void defineStructure_someEnum() throws Exception {
    ConvertModelDir dir = new ConvertModelDir();
    File someTestEnumClass = dir.read("sub2/SomeTestEnum.ts");

    TsFileReference someTestEnum = new TsFileReference(someTestEnumClass, "sub2", "SomeTestEnum");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    //
    //
    convertModel.defineStructure(singletonList(someTestEnum));
    //
    //

    assertThat(someTestEnum.enumElementList.get(0).name).isEqualTo("ELEMENT1");
    assertThat(someTestEnum.enumElementList.get(0).value).isEqualTo("VALUE1");

    assertThat(someTestEnum.enumElementList.get(1).name).isEqualTo("ELEMENT2");
    assertThat(someTestEnum.enumElementList.get(1).value).isEqualTo("VALUE2");

    assertThat(someTestEnum.enumElementList.get(2).name).isEqualTo("ELEMENT3");
    assertThat(someTestEnum.enumElementList.get(2).value).isEqualTo("VALUE3");

    assertThat(someTestEnum.enumElementList).hasSize(3);
  }

  @DataProvider
  public Object[][] defineStructure_someEnum_lastElementComma_DP() {
    return new Object[][]{
      {"EnumWithoutCommaAtLastElement1"}, {"EnumWithoutCommaAtLastElement2"},
    };
  }

  @Test(
    expectedExceptions = CommaAtLastEnumElement.class,
    dataProvider = "defineStructure_someEnum_lastElementComma_DP"
  )
  public void defineStructure_someEnum_lastElementComma(String name) throws Exception {
    ConvertModelDir dir = new ConvertModelDir();
    File someTestEnumClass = dir.read("sub2/" + name + ".ts");

    TsFileReference someTestEnum = new TsFileReference(someTestEnumClass, "sub2", name);

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    //
    //
    convertModel.defineStructure(singletonList(someTestEnum));
    //
    //
  }

  @Test
  public void defineStructure_dateField() throws Exception {
    ConvertModelDir dir = new ConvertModelDir();
    File tsClassFile = dir.read("sub2/ClassWithDateField.ts");

    TsFileReference tsFile = new TsFileReference(tsClassFile, "sub2", "ClassWithDateField");

    ConvertModel convertModel = new ConvertModel(dir.sourceDir(), dir.destinationDir(), "kz.greetgo.wow");

    //
    //
    convertModel.defineStructure(singletonList(tsFile));
    //
    //

    assertThat(tsFile.classStructure).isNotNull();
    assertThat(tsFile.classStructure.attrList).hasSize(2);
    assertThat(tsFile.classStructure.attrList.get(0).name).isEqualTo("dateField1");
    assertThat(tsFile.classStructure.attrList.get(1).name).isEqualTo("dateField2");

    assertThat(tsFile.classStructure.attrList.get(0).type).isInstanceOf(TypeDate.class);
    assertThat(tsFile.classStructure.attrList.get(1).type).isInstanceOf(TypeDate.class);
  }
}
