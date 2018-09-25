# greetgo.type_script

Конвертирует модели на языке TypeScript в модели на Java

### Подключение через Maven Central

    testCompile "kz.greetgo.type_script:greetgo.ts_java_convert:0.0.6"

### Что делает и как использовать

Например имеются файлы:

Файл _some/ts/dir/MainClass.ts :_
```typescript
import {AnotherClass} from "./AnotherClass";
import {SomeEnum} from "./SomeEnum";

export class MainClass {
  public strField: string | null;
  public intField: number/*int*/;
  public anotherField: AnotherClass;
  public enumField: SomeEnum;
}
```
Файл _some/ts/dir/AnotherClass.ts :_
```typescript
export class AnotherClass {
  public intField: number/*int*/;
}
```
Файл _some/ts/dir/SomeEnum.ts :_
```typescript
export enum SomeEnum {
  FIELD1 = "FIELD1",
  FIELD2 = "FIELD2",
}
```

И их нужно преобразовать в следующие классы Java:

Файл _java/destination/src/dir/some/package_from/ts/MainClass.java :_

```java
package some.package_from.ts;

public class MainClass {
  public String strField;
  public int intField;
  public AnotherClass anotherField;
  public SomeEnum enumField;
}
```

Файл _java/destination/src/dir/some/package_from/ts/AnotherClass.java :_

```java
package some.package_from.ts;

public class AnotherClass {
  public int intField;
}
```

Файл _java/destination/src/dir/some/package_from/ts/SomeEnum.java :_

```java
package some.package_from.ts;

public enum SomeEnum {
  FIELD1,
  FIELD2,
  ;
}
```

Для этого нужно запустить такой Java-код:

```java
import kz.greetgo.ts_java_convert.ConvertModelBuilder;
  
public class SomeLauncher {

  public static void main() {
    new ConvertModelBuilder()
        .sourceDir("some/ts/dir")
        .destinationDir("java/destination/src")
        .destinationPackage("some.package_from.ts")
        .setAddingDefaultConstructor(true)
        .create().execute();
  }

}
```
