package kz.greetgo.ts_java_convert.util;

import kz.greetgo.ts_java_convert.ConvertModelUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractResourceDir {
  public File read(String resourceName) {
    InputStream inputStream = getClass().getResourceAsStream(resourceName);
    if (inputStream == null) {
      throw new IllegalArgumentException("No resource " + resourceName);
    }
    File destinationFile = sourceDir.toPath().resolve(resourceName).toFile();
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


  private final File sourceDir;
  private final File destinationDir;

  public AbstractResourceDir() {
    this(false);
  }

  public AbstractResourceDir(boolean fixedDestination) {
    String build = "build";
    String yyyyMMdd_hHmmssSSS = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
    sourceDir = new File(build
        + "/test_data/"
        + getClass().getSimpleName()
        + "/"
        + yyyyMMdd_hHmmssSSS
        + "/source"
    );
    destinationDir = new File(build
        + "/test_data/"
        + getClass().getSimpleName()
        + (fixedDestination ? "" : "/" + yyyyMMdd_hHmmssSSS)
        + "/destination"
    );
  }

  public File sourceDir() {
    return sourceDir;
  }

  public File destinationDir() {
    return destinationDir;
  }

  public File destinationDir(String dirName) {
    return destinationDir.getParentFile().toPath().resolve(dirName).toFile();
  }
}
