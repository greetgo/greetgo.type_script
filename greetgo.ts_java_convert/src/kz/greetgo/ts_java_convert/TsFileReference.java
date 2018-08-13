package kz.greetgo.ts_java_convert;


import kz.greetgo.ts_java_convert.errors.BooleanCannotBeMultipleArray;
import kz.greetgo.ts_java_convert.errors.CannotFindClassInImports;
import kz.greetgo.ts_java_convert.errors.ClassCannotBeMultipleArray;
import kz.greetgo.ts_java_convert.errors.CommaAtLastEnumElement;
import kz.greetgo.ts_java_convert.errors.NoFileInImport;
import kz.greetgo.ts_java_convert.errors.NoNumberTypeForJava;
import kz.greetgo.ts_java_convert.errors.NumberCannotBeMultipleArray;
import kz.greetgo.ts_java_convert.stru.ClassAttr;
import kz.greetgo.ts_java_convert.stru.ClassStructure;
import kz.greetgo.ts_java_convert.stru.EnumElement;
import kz.greetgo.ts_java_convert.stru.Import;
import kz.greetgo.ts_java_convert.stru.SimpleType;
import kz.greetgo.ts_java_convert.stru.TypeDate;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoolean;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeBoxedBoolean;
import kz.greetgo.ts_java_convert.stru.simple.SimpleTypeStr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TsFileReference {
  public final File tsFile;
  public final String subPackage;
  public final String className;

  public List<TsFileReference> anotherFiles;

  private String content = null;
  public ClassStructure classStructure;
  public File sourceDir;

  public void defineRealPackage(String packagePrefix) {
    String realPackage = resolvePackage(packagePrefix, subPackage);
    classStructure = new ClassStructure(realPackage, className, attrList, enumElementList, classComment);
  }

  public String content() {

    if (content == null) try {
      content = ConvertModelUtil.streamToStr(new FileInputStream(tsFile));
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    return content;
  }

  @Override
  public String toString() {
    //noinspection StringBufferReplaceableByString
    final StringBuilder sb = new StringBuilder("TsFileReference{");
    sb.append("tsFile=").append(tsFile);
    sb.append(", subPackage=").append(subPackage == null ? "<NULL>" : subPackage);
    sb.append('}');
    return sb.toString();
  }

  public TsFileReference(File tsFile, String subPackage, String className) {
    this.tsFile = tsFile;
    this.subPackage = subPackage;
    this.className = className;
  }

  public static List<TsFileReference> scanForTs(File dir) {
    List<TsFileReference> ret = new ArrayList<>();
    scanForTsInner(ret, dir, null);
    return ret;
  }

  private static void scanForTsInner(List<TsFileReference> ret,
                                     File dir, String currentSubPackage) {
    File[] files = dir.listFiles();
    if (files == null) throw new NullPointerException("dir.listFiles() == null, dir = " + dir);
    for (File subFile : files) {
      if (subFile.isDirectory()) {
        scanForTsInner(ret, subFile, resolvePackage(currentSubPackage, subFile.getName()));
        continue;
      }
      if (subFile.getName().endsWith(".ts")) {
        ret.add(new TsFileReference(subFile, currentSubPackage,
          subFile.getName().substring(0, subFile.getName().length() - 3)));
        continue;
      }
    }
  }

  public static String resolvePackage(String subPackage1, String subPackage2) {
    if (subPackage1 == null || subPackage1.length() == 0) return subPackage2;
    if (subPackage2 == null || subPackage2.length() == 0) return subPackage1;
    return subPackage1 + '.' + subPackage2;
  }

  final List<String> classComment = new ArrayList<>();
  boolean wasClassDefinition = false;
  boolean wasEnumDefinition = false;

  public void fillAttributes() throws Exception {
    wasClassDefinition = false;
    wasEnumDefinition = false;

    int lineNo = 1;
    for (String line : content().split("\n")) {
      parseLine(lineNo++, line);
    }

    if (!wasClassDefinition && !wasEnumDefinition) {
      throw new RuntimeException("No class or enum definition in " + tsFile);
    }
  }

  private static final Pattern CLASS_DEFINITION
    = Pattern.compile("\\s*export\\s+class\\s+(\\w+)[^{]*\\{\\s*");

  private static final Pattern ENUM_DEFINITION
    = Pattern.compile("\\s*export\\s+enum\\s+(\\w+)[^{]*\\{\\s*");

  //public world: string;
  private static final Pattern STRING_FIELD
    = Pattern.compile("\\s*public\\s+(\\w+)\\s*!?:\\s*string\\s*(\\[\\s*]\\s*)?\\s*(\\|\\s*null)?.*(=.*)?;.*");

  private static final Pattern STRING_FIELD2
    = Pattern.compile("\\s*public\\s+(\\w+)\\s*!?:\\s*null\\s*\\|\\s*string\\s*(\\[\\s*]\\s*)?.*(=.*)?;.*");

  private static final Pattern NUMBER_FIELD_array_hasOrAbsent
    = Pattern.compile("\\s*public\\s+(\\w+)\\s*!?:\\s*number\\s*(\\[\\s*])?\\s*(/\\*\\s*(\\w+)\\s*\\*/)?\\s*(\\[\\s*])?\\s*(=.*)?;.*");

  private static final Pattern NUMBER_FIELD_null
    = Pattern.compile("\\s*public\\s+(\\w+)\\s*!?:\\s*number\\s*(\\|)?\\s*(null)?\\s*(/\\*\\s*(\\w+)\\s*\\*/)\\s*(\\|)?\\s*(null)?\\s*(=.*)?;.*");

  private static final Pattern NUMBER_FIELD_null2
    = Pattern.compile("\\s*public\\s+(\\w+)\\s*!?:\\s*null\\s*\\|\\s*number\\s*(/\\*\\s*(\\w+)\\s*\\*/)\\s*(=.*)?;.*");

  //import {OrgUnitKind} from "./org_unit/OrgUnitKind";
  private static final Pattern IMPORT
    = Pattern.compile("\\s*import\\s+\\{(\\w+)}\\s+from\\s*\"\\./([^\"]*\\w)\"\\s*;.*");

  //import {OrgUnitKind} from "../org_unit/OrgUnitKind";
  private static final Pattern IMPORT_PARENT
    = Pattern.compile("\\s*import\\s+\\{(\\w+)}\\s+from\\s*\"\\.\\./([^\"]*\\w)\"\\s*;.*");

  //import {OrgUnitKind} from "@/org_unit/OrgUnitKind";
  private static final Pattern IMPORT_SOURCE
    = Pattern.compile("\\s*import\\s+\\{(\\w+)}\\s+from\\s*\"@/([^\"]*\\w)\"\\s*;.*");

  //public bArray: OrgUnitRoot|null[];
  private static final Pattern CLASS_FIELD
    = Pattern.compile("\\s*public\\s*(\\w+)\\s*!?:\\s*(null\\s*\\|)?\\s*(\\w+)\\s*(\\[\\s*])?\\s*(\\|\\s*null)?\\s*(\\[\\s*])?\\s*(=.*)?;.*");


  //public hasChildren: boolean|null[];
  private static final Pattern BOOLEAN_FIELD
    = Pattern.compile("\\s*public\\s+(\\w+)\\s*!?:\\s*boolean\\s*(\\[\\s*])?\\s*(\\|\\s*null)?\\s*(\\[\\s*])?\\s*(=.*)?;.*");

  private static final Pattern BOOLEAN_FIELD_null
    = Pattern.compile("\\s*public\\s+(\\w+)\\s*!?:\\s*null\\s*\\|\\s*boolean\\s*(\\[\\s*])?\\s*(=.*)?;.*");

  final Map<String, Import> importMap = new HashMap<>();
  public final List<ClassAttr> attrList = new ArrayList<>();
  public final List<EnumElement> enumElementList = new ArrayList<>();

  private static final Pattern COMMENT_BEGIN = Pattern.compile("\\s*/\\*\\*\\s*");
  private static final Pattern COMMENT_END = Pattern.compile("\\s*\\*/\\s*");
  private final List<String> comment = new ArrayList<>();
  boolean inComment = false;

  private void parseLine(int lineNo, String line) throws Exception {

    if (inComment) {
      inComment = !COMMENT_END.matcher(line).matches();
      comment.add(line);
      return;
    }
    if (COMMENT_BEGIN.matcher(line).matches()) {
      comment.clear();
      comment.add(line);
      inComment = true;
      return;
    }

    {
      Matcher matcher = IMPORT.matcher(line);
      if (matcher.matches()) {
        String className = matcher.group(1);
        File importedFile = tsFile.getParentFile().toPath().resolve(matcher.group(2) + ".ts").toFile();

        registerImport(lineNo, className, importedFile);
        comment.clear();
        return;
      }
    }
    {
      Matcher matcher = IMPORT_PARENT.matcher(line);
      if (matcher.matches()) {
        String className = matcher.group(1);
        File importedFile = tsFile.getParentFile().getParentFile().toPath().resolve(matcher.group(2) + ".ts").toFile();

        registerImport(lineNo, className, importedFile);
        comment.clear();
        return;
      }
    }
    {
      Matcher matcher = IMPORT_SOURCE.matcher(line);
      if (matcher.matches()) {
        String className = matcher.group(1);
        File importedFile = sourceDir.toPath().resolve(matcher.group(2) + ".ts").toFile();

        registerImport(lineNo, className, importedFile);
        comment.clear();
        return;
      }
    }

    {
      Matcher matcher = CLASS_DEFINITION.matcher(line);
      //noinspection Duplicates
      if (matcher.matches()) {
        if (wasEnumDefinition) {
          throw new RuntimeException("You cannot define enum and class in one file: " + place(lineNo));
        }
        String lineClassName = matcher.group(1);
        registerImport(lineNo, lineClassName, tsFile);
        if (lineClassName.equals(className)) {
          wasClassDefinition = true;
          classComment.addAll(comment);
          comment.clear();
          return;
        }
        comment.clear();
        throw new RuntimeException("Left class name " + lineClassName + " at " + place(lineNo));
      }
    }

    {
      Matcher matcher = ENUM_DEFINITION.matcher(line);
      //noinspection Duplicates
      if (matcher.matches()) {
        if (wasClassDefinition) {
          throw new RuntimeException("You cannot define enum and class in one file: " + place(lineNo));
        }
        String lineClassName = matcher.group(1);
        registerImport(lineNo, lineClassName, tsFile);
        if (lineClassName.equals(className)) {
          wasEnumDefinition = true;
          classComment.addAll(comment);
          comment.clear();
          return;
        }
        comment.clear();
        throw new RuntimeException("Left class name " + lineClassName + " at " + place(lineNo));
      }
    }

    if (wasClassDefinition) parseForClassField(line, lineNo);
    if (wasEnumDefinition) parseForEnumElement(line, lineNo);

  }

  private static final Pattern ENUM_ELEMENT1 = Pattern.compile("\\s*(\\w+)\\s*=\\s*\"([^\"]*)\"\\s*(,)?.*");
  private static final Pattern ENUM_ELEMENT2 = Pattern.compile("\\s*(\\w+)\\s*=\\s*'([^']*)'\\s*(,)?.*");

  private void parseForEnumElement(String line, @SuppressWarnings("unused") int lineNo) {
    {
      Matcher matcher = ENUM_ELEMENT1.matcher(line);
      //noinspection Duplicates
      if (matcher.matches()) {

        String elementName = matcher.group(1);
        String elementValue = matcher.group(2);
        boolean hasComma = matcher.group(3) != null;

        if (!hasComma) throw new CommaAtLastEnumElement(place(lineNo));

        enumElementList.add(new EnumElement(elementName, elementValue, comment));

        comment.clear();
        return;
      }
    }
    {
      Matcher matcher = ENUM_ELEMENT2.matcher(line);
      //noinspection Duplicates
      if (matcher.matches()) {

        String elementName = matcher.group(1);
        String elementValue = matcher.group(2);
        boolean hasComma = matcher.group(3) != null;

        if (!hasComma) throw new CommaAtLastEnumElement(place(lineNo));

        enumElementList.add(new EnumElement(elementName, elementValue, comment));

        comment.clear();
        return;
      }
    }
  }

  private void parseForClassField(String line, int lineNo) {
    {
      Matcher matcher = STRING_FIELD.matcher(line);
      if (matcher.matches()) {
        attrList.add(new ClassAttr(SimpleTypeStr.get(), matcher.group(1), matcher.group(2) != null, comment));
        comment.clear();
        return;
      }
    }

    {
      Matcher matcher = STRING_FIELD2.matcher(line);
      if (matcher.matches()) {
        attrList.add(new ClassAttr(SimpleTypeStr.get(), matcher.group(1), matcher.group(2) != null, comment));
        comment.clear();
        return;
      }
    }

    {
      Matcher matcher = NUMBER_FIELD_array_hasOrAbsent.matcher(line);
      if (matcher.matches()) {
        String fieldName = matcher.group(1);
        boolean leftSquareBrackets = matcher.group(2) != null;
        String strType = matcher.group(4);
        boolean rightSquareBrackets = matcher.group(5) != null;
        if (leftSquareBrackets && rightSquareBrackets) throw new NumberCannotBeMultipleArray(place(lineNo));
        if (strType == null) throw new NoNumberTypeForJava(place(lineNo));
        attrList.add(new ClassAttr(
          SimpleType.fromStr(strType, false, place(lineNo)),
          fieldName,
          leftSquareBrackets || rightSquareBrackets,
          comment
        ));
        comment.clear();
        return;
      }
    }

    {
      Matcher matcher = NUMBER_FIELD_null.matcher(line);
      if (matcher.matches()) {
        String fieldName = matcher.group(1);
        boolean nullLeft = matcher.group(3) != null;
        String strType = matcher.group(5);
        boolean nullRight = matcher.group(7) != null;

        boolean hasNull = nullLeft || nullRight;

        if (strType == null) throw new NoNumberTypeForJava(place(lineNo));
        attrList.add(new ClassAttr(
          SimpleType.fromStr(strType, hasNull, place(lineNo)),
          fieldName,
          false,
          comment
        ));
        comment.clear();
        return;
      }
    }

    {
      Matcher matcher = NUMBER_FIELD_null2.matcher(line);
      if (matcher.matches()) {
        String fieldName = matcher.group(1);
        String strType = matcher.group(3);

        if (strType == null) throw new NoNumberTypeForJava(place(lineNo));
        attrList.add(new ClassAttr(
          SimpleType.fromStr(strType, true, place(lineNo)),
          fieldName,
          false,
          comment
        ));
        comment.clear();
        return;
      }
    }

    {
      Matcher matcher = BOOLEAN_FIELD.matcher(line);
      if (matcher.matches()) {

        String fieldName = matcher.group(1);
        boolean leftArray = matcher.group(2) != null;
        boolean isNull = matcher.group(3) != null;
        boolean rightArray = matcher.group(4) != null;

        if (leftArray && rightArray) throw new BooleanCannotBeMultipleArray(place(lineNo));

        boolean boxed = isNull, isArray = leftArray || rightArray;
        if (isArray) boxed = true;

        attrList.add(new ClassAttr(
          boxed ? SimpleTypeBoxedBoolean.get() : SimpleTypeBoolean.get(),
          fieldName,
          isArray,
          comment
        ));

        comment.clear();
        return;
      }
    }
    {
      Matcher matcher = BOOLEAN_FIELD_null.matcher(line);
      if (matcher.matches()) {

        String fieldName = matcher.group(1);
        boolean isArray = matcher.group(2) != null;

        attrList.add(new ClassAttr(
          SimpleTypeBoxedBoolean.get(),
          fieldName,
          isArray,
          comment
        ));

        comment.clear();
        return;
      }
    }

    {
      Matcher matcher = CLASS_FIELD.matcher(line);
      if (matcher.matches()) {
        String fieldName = matcher.group(1);
        String className = matcher.group(3);
        boolean isArray1 = matcher.group(4) != null;
        boolean isArray2 = matcher.group(6) != null;

        if (isArray1 && isArray2) throw new ClassCannotBeMultipleArray(place(lineNo));
        boolean isArray = isArray1 || isArray2;

        if ("Date".equals(className)) {

          attrList.add(new ClassAttr(TypeDate.get(), fieldName, isArray, comment));
          comment.clear();

          return;
        }

        Import anImport = importMap.get(className);
        if (anImport == null) throw new CannotFindClassInImports(className, place(lineNo));

        attrList.add(new ClassAttr(anImport.toClassStructure(), fieldName, isArray, comment));
        comment.clear();
        return;
      }
    }
  }

  private void registerImport(int lineNo, String className, File importedFile) throws Exception {
    {
      Import anotherImport = importMap.get(className);
      if (anotherImport != null) {
        throw new RuntimeException(className + " already defined at line " + anotherImport.lineNo
          + " in " + place(lineNo));
      }
    }

    if (!importedFile.exists()) {
      throw new NoFileInImport(importedFile, place(lineNo));
    }

    TsFileReference ref = tsFile.equals(importedFile) ? this : findTsFileReference(importedFile, lineNo);

    if (ref != null) importMap.put(className, new Import(className, ref, lineNo));
  }

  private TsFileReference findTsFileReference(File importedFile, int lineNo) throws IOException {
    File canonicalFile = importedFile.getCanonicalFile();

    for (TsFileReference anotherFile : anotherFiles) {
      if (canonicalFile.equals(anotherFile.tsFile.getCanonicalFile())) return anotherFile;
    }

    if (ConvertModelUtil.isParent(sourceDir, importedFile)) {
      throw new RuntimeException("Cannot find " + importedFile + " at " + place(lineNo));
    }

    return null;
  }

  private String place(int lineNo) {
    return tsFile + ":" + lineNo;
  }
}
