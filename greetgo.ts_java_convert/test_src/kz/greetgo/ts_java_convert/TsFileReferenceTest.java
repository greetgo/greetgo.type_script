package kz.greetgo.ts_java_convert;


import kz.greetgo.ts_java_convert.test_scanForTs.ScanForTsDir;
import org.testng.annotations.Test;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static org.fest.assertions.api.Assertions.assertThat;

public class TsFileReferenceTest {
  @Test
  public void scanForTs() {

    File testDir = new File("build/scanForTs/"
      + new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date()));

    ScanForTsDir sfd = new ScanForTsDir();
    File class1 = sfd.copy("Class1.ts", testDir);
    File class2 = sfd.copy("sub1/Class2.ts", testDir);

    //
    //
    List<TsFileReference> list = TsFileReference.scanForTs(testDir);
    //
    //

    Map<String, TsFileReference> map = list.stream().collect(toMap(r -> r.className, r -> r));

    assertThat(map).containsKey("Class1");
    assertThat(map).containsKey("Class2");
    assertThat(map.get("Class1").tsFile).hasContentEqualTo(class1);
    assertThat(map.get("Class1").subPackage).isNull();

    assertThat(map.get("Class2").tsFile).hasContentEqualTo(class2);
    assertThat(map.get("Class2").subPackage).isEqualTo("sub1");


  }
}