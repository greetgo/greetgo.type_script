package kz.greetgo.ts_java_convert.definitions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClassDefinition {
  public final String lineClassName;
  public final String extend;
  public final List<String> impls;

  private ClassDefinition(String lineClassName, String extend, List<String> impls) {
    this.lineClassName = lineClassName;
    this.extend = extend;
    this.impls = impls;
  }


  private static final Pattern CLASS_DEFINITION1
    = Pattern.compile("\\s*export\\s+class\\s+(\\w+)\\s*\\{.*");

  private static final Pattern CLASS_DEFINITION2
    = Pattern.compile("\\s*export\\s+class\\s+(\\w+)\\s+extends\\s+(\\w+)\\s*\\{.*");

  private static final Pattern CLASS_DEFINITION3
    = Pattern.compile("\\s*export\\s+class\\s+(\\w+)\\s+implements\\s+((\\w+)(\\s*,\\s*\\w+)*)\\s*\\{.*");

  private static final Pattern CLASS_DEFINITION4
    = Pattern.compile("\\s*export\\s+class\\s+(\\w+)\\s+extends\\s+(\\w+)\\s+implements\\s+((\\w+)(\\s*,\\s*\\w+)*)\\s*\\{.*");

  public static ClassDefinition match(String line) {
    {
      Matcher matcher = CLASS_DEFINITION1.matcher(line);
      if (matcher.matches()) {
        String lineClassName = matcher.group(1);
        return new ClassDefinition(lineClassName, null, Collections.emptyList());
      }
    }

    {
      Matcher matcher = CLASS_DEFINITION2.matcher(line);
      if (matcher.matches()) {
        String lineClassName = matcher.group(1);
        String extend = matcher.group(2);
        return new ClassDefinition(lineClassName, extend, Collections.emptyList());
      }
    }

    {
      Matcher matcher = CLASS_DEFINITION3.matcher(line);
      if (matcher.matches()) {
        String lineClassName = matcher.group(1);
        String impls = matcher.group(2).trim();

        return new ClassDefinition(lineClassName,
          null,
          Arrays.stream(impls.split(","))
            .map(String::trim)
            .collect(Collectors.toList()));
      }
    }
    {
      Matcher matcher = CLASS_DEFINITION4.matcher(line);
      if (matcher.matches()) {
        String lineClassName = matcher.group(1);
        String extend = matcher.group(2);
        String impls = matcher.group(3).trim();

        return new ClassDefinition(lineClassName,
          extend,
          Arrays.stream(impls.split(","))
            .map(String::trim)
            .collect(Collectors.toList()));
      }
    }


    return null;
  }
}
