package kz.greetgo.ts_java_convert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConvertModelUtil {
  public static List<TsFileReference> deleteIt(TsFileReference file, List<TsFileReference> files) {
    List<TsFileReference> ret = new ArrayList<>();
    for (TsFileReference another : files) {
      if (another != file) ret.add(another);
    }
    return Collections.unmodifiableList(ret);
  }

  public static String streamToStr(InputStream inputStream) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    copyStreams(inputStream, out);
    try {
      return out.toString("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static void copyStreams(InputStream inputStream, OutputStream outputStream) {
    copyStreams(inputStream, outputStream, new byte[4 * 1024]);
  }

  public static void copyStreams(InputStream inputStream, OutputStream outputStream, byte[] buffer) {
    try {

      while (true) {
        int count = inputStream.read(buffer);
        if (count < 0) return;
        outputStream.write(buffer, 0, count);
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static boolean isParent(File parentDir, File childFile) {

    URI parentUri = parentDir.toURI();

    URI childUri = childFile.toURI();

    return !parentUri.relativize(childUri).isAbsolute();
  }
}
