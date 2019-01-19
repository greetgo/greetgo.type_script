package kz.greetgo.ts_java_convert.definitions;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ClassDefinitionTest {

  @Test
  public void simpleMatch() {

    ClassDefinition classDefinition = ClassDefinition.match("export class HelloWorld {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("HelloWorld");
    assertThat(classDefinition.extend).isNull();
    assertThat(classDefinition.impls).isEmpty();

  }

  @Test
  public void matchWithoutSpaceAtTheEnd() {

    ClassDefinition classDefinition = ClassDefinition.match("export class HelloWorld{");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("HelloWorld");
    assertThat(classDefinition.extend).isNull();
    assertThat(classDefinition.impls).isEmpty();

  }

  @Test
  public void matchWithExtend() {

    ClassDefinition classDefinition = ClassDefinition.match("export class HelloWorld extends ClassWow {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("HelloWorld");
    assertThat(classDefinition.extend).isEqualTo("ClassWow");
    assertThat(classDefinition.impls).isEmpty();

  }

  @Test
  public void matchWithOneImpl() {

    ClassDefinition classDefinition = ClassDefinition.match("export class HelloWorld implements Inter1 {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("HelloWorld");
    assertThat(classDefinition.extend).isNull();
    assertThat(classDefinition.impls).containsExactly("Inter1");

  }

  @Test
  public void matchWithTwoImpls() {

    ClassDefinition classDefinition = ClassDefinition.match("export class GroupStatus implements Moon, Inter2 {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("GroupStatus");
    assertThat(classDefinition.extend).isNull();
    assertThat(classDefinition.impls).containsExactly("Moon", "Inter2");

  }

  @Test
  public void matchWithThreeImpls() {

    ClassDefinition classDefinition = ClassDefinition.match("export class Paco implements Sum54325, Zoom, Tan {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("Paco");
    assertThat(classDefinition.extend).isNull();
    assertThat(classDefinition.impls).containsExactly("Sum54325", "Zoom", "Tan");
  }


  @Test
  public void matchWithExtendAndOneImpl() {

    ClassDefinition classDefinition = ClassDefinition.match("export class Sinus extends PageSinus implements Family {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("Sinus");
    assertThat(classDefinition.extend).isEqualTo("PageSinus");
    assertThat(classDefinition.impls).containsExactly("Family");
  }

  @Test
  public void matchWithExtendAndTwoImpls() {

    ClassDefinition classDefinition = ClassDefinition.match(
      "export class Class4351565 extends RightSide implements Stack, Intro {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("Class4351565");
    assertThat(classDefinition.extend).isEqualTo("RightSide");
    assertThat(classDefinition.impls).containsExactly("Stack", "Intro");
  }

  @Test
  public void matchWithExtendAndThreeImpls() {

    ClassDefinition classDefinition = ClassDefinition.match(
      "export class HelloWorld extends PoolSta4325 implements Stake, Cake, Tomato {");

    assertThat(classDefinition).isNotNull();
    assert classDefinition != null;
    assertThat(classDefinition.lineClassName).isEqualTo("HelloWorld");
    assertThat(classDefinition.extend).isEqualTo("PoolSta4325");
    assertThat(classDefinition.impls).containsExactly("Stake", "Cake", "Tomato");
  }

  @DataProvider
  public Object[][] noMachDataProvider() {
    return new Object[][]{
      {"4u3h2i4h3 3uih5i234 23u42 34i"},
      {"class Asd {"},
      {"Export Class Asd {"},
      {"export interface Asd {"},
      {"export class {"},
    };
  }

  @Test(dataProvider = "noMachDataProvider")
  public void noMach(String line) {

    ClassDefinition classDefinition = ClassDefinition.match(line);

    assertThat(classDefinition).isNull();

  }

}