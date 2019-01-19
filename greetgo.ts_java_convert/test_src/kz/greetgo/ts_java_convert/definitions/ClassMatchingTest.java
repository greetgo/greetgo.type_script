package kz.greetgo.ts_java_convert.definitions;

import kz.greetgo.ts_java_convert.matchings.ClassMatching;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ClassMatchingTest {

  @Test
  public void simpleMatch() {

    ClassMatching classMatching = ClassMatching.match("export class HelloWorld {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("HelloWorld");
    assertThat(classMatching.extend).isNull();
    assertThat(classMatching.impls).isEmpty();

  }

  @Test
  public void matchWithoutSpaceAtTheEnd() {

    ClassMatching classMatching = ClassMatching.match("export class HelloWorld{");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("HelloWorld");
    assertThat(classMatching.extend).isNull();
    assertThat(classMatching.impls).isEmpty();

  }

  @Test
  public void matchWithExtend() {

    ClassMatching classMatching = ClassMatching.match("export class HelloWorld extends ClassWow {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("HelloWorld");
    assertThat(classMatching.extend).isEqualTo("ClassWow");
    assertThat(classMatching.impls).isEmpty();

  }

  @Test
  public void matchWithOneImpl() {

    ClassMatching classMatching = ClassMatching.match("export class HelloWorld implements Inter1 {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("HelloWorld");
    assertThat(classMatching.extend).isNull();
    assertThat(classMatching.impls).containsExactly("Inter1");

  }

  @Test
  public void matchWithTwoImpls() {

    ClassMatching classMatching = ClassMatching.match("export class GroupStatus implements Moon, Inter2 {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("GroupStatus");
    assertThat(classMatching.extend).isNull();
    assertThat(classMatching.impls).containsExactly("Moon", "Inter2");

  }

  @Test
  public void matchWithThreeImpls() {

    ClassMatching classMatching = ClassMatching.match("export class Paco implements Sum54325, Zoom, Tan {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("Paco");
    assertThat(classMatching.extend).isNull();
    assertThat(classMatching.impls).containsExactly("Sum54325", "Zoom", "Tan");
  }


  @Test
  public void matchWithExtendAndOneImpl() {

    ClassMatching classMatching = ClassMatching.match("export class Sinus extends PageSinus implements Family {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("Sinus");
    assertThat(classMatching.extend).isEqualTo("PageSinus");
    assertThat(classMatching.impls).containsExactly("Family");
  }

  @Test
  public void matchWithExtendAndTwoImpls() {

    ClassMatching classMatching = ClassMatching.match(
      "export class Class4351565 extends RightSide implements Stack, Intro {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("Class4351565");
    assertThat(classMatching.extend).isEqualTo("RightSide");
    assertThat(classMatching.impls).containsExactly("Stack", "Intro");
  }

  @Test
  public void matchWithExtendAndThreeImpls() {

    ClassMatching classMatching = ClassMatching.match(
      "export class HelloWorld extends PoolSta4325 implements Stake, Cake, Tomato {");

    assertThat(classMatching).isNotNull();
    assert classMatching != null;
    assertThat(classMatching.lineClassName).isEqualTo("HelloWorld");
    assertThat(classMatching.extend).isEqualTo("PoolSta4325");
    assertThat(classMatching.impls).containsExactly("Stake", "Cake", "Tomato");
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

    ClassMatching classMatching = ClassMatching.match(line);

    assertThat(classMatching).isNull();

  }

}