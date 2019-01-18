package kz.greetgo.ts_java_convert;


import kz.greetgo.ts_java_convert.stru.ClassStructure;
import kz.greetgo.ts_java_convert.stru.SimpleType;
import kz.greetgo.ts_java_convert.stru.TypeDate;
import kz.greetgo.ts_java_convert.stru.TypeStructure;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Imports {

  final Map<String, String> fullNameMap = new HashMap<>();
  private final ClassStructure owner;

  public Imports(ClassStructure owner) {
    this.owner = owner;
  }

  public String name(String fullName) {
    int index = fullName.lastIndexOf('.');
    if (index < 0) {
      return fullName;
    }

    String name = fullName.substring(index + 1);

    if (fullName.equals(owner.fullName())) return name;

    String anotherFullName = fullNameMap.get(name);

    if (anotherFullName == null) {
      fullNameMap.put(name, fullName);
      return name;
    }

    if (fullName.equals(anotherFullName)) {
      return name;
    }

    return fullName;
  }

  private static final String LIST_NAME = List.class.getName();

  public String asStr() {
    return fullNameMap.entrySet().stream()
      .map(e -> "import " + e.getValue() + ";")
      .sorted()
      .collect(Collectors.joining("\n"))
      ;
  }

  public String typeStr(TypeStructure type, boolean isArray) {
    return isArray
      ? name(LIST_NAME) + '<' + name(typeName(type, true)) + '>'
      : name(typeName(type, false));
  }

  private String typeName(TypeStructure type, boolean boxed) {
    if (type == TypeDate.get()) {
      return Date.class.getName();
    }
    if (type instanceof SimpleType) {
      return ((SimpleType) type).javaName(boxed);
    }
    if (type instanceof ClassStructure) {
      return ((ClassStructure) type).fullName();
    }
    throw new IllegalArgumentException("Cannot get name of " + type.getClass());
  }

  private static final Pattern IMPORT = Pattern.compile("\\s*import\\s+(\\S+)\\s*;.*");

  public void readImportsFromJava(File javaFile) {
    if (!javaFile.exists()) {
      return;
    }

    try {

      List<String> lines = Files.readAllLines(javaFile.toPath());

      boolean inImports = false;

      for (String line : lines) {
        if (line.trim().startsWith("//")) {
          continue;
        }
        if (line.trim().length() == 0) {
          continue;
        }
        Matcher matcher = IMPORT.matcher(line);
        if (matcher.matches()) {
          inImports = true;
          name(matcher.group(1));
        } else if (inImports) {
          break;
        }
      }

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
