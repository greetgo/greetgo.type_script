package kz.greetgo.ts_java_convert;


import kz.greetgo.ts_java_convert.test_scanForTs.ScanForTsDir;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static org.fest.assertions.api.Assertions.assertThat;

public class TsFileReferenceTest {
  @Test
  public void scanForTs() {

    ScanForTsDir sfd = new ScanForTsDir();

    File class1 = sfd.read("Class1.ts");
    File class2 = sfd.read("sub1/Class2.ts");

    //
    //
    List<TsFileReference> list = TsFileReference.scanForTs(sfd.sourceDir());
    //
    //

    Map<String, TsFileReference> map = list.stream().collect(toMap(r -> r.className, Function.identity()));

    assertThat(map).containsKey("Class1");
    assertThat(map).containsKey("Class2");
    assertThat(map.get("Class1").tsFile).hasContentEqualTo(class1);
    assertThat(map.get("Class1").subPackage).isNull();

    assertThat(map.get("Class2").tsFile).hasContentEqualTo(class2);
    assertThat(map.get("Class2").subPackage).isEqualTo("sub1");
  }
}