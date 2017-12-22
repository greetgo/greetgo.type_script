package kz.greetgo.ts_java_convert.stru;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnumElement {
  public final String name, value;
  public final List<String> comment = new ArrayList<>();

  public EnumElement(String name, String value, Collection<String> comment) {
    this.name = name;
    this.value = value;
    this.comment.addAll(comment);
  }
}
