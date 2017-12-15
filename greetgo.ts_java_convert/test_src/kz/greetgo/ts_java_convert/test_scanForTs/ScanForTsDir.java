package kz.greetgo.ts_java_convert.test_scanForTs;

import kz.greetgo.ts_java_convert.ConvertModelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ScanForTsDir {

  public File copy(String resourceName, File destinationDir) {
    InputStream inputStream = getClass().getResourceAsStream(resourceName);
    if (inputStream == null) throw new IllegalArgumentException("No resource " + resourceName);
    File destinationFile = destinationDir.toPath().resolve(resourceName).toFile();
    destinationFile.getParentFile().mkdirs();
    try {
      try (FileOutputStream outputStream = new FileOutputStream(destinationFile)) {
        ConvertModelUtil.copyStreams(inputStream, outputStream);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return destinationFile;
  }

}
