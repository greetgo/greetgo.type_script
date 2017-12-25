# greetgo.type_script

Конвертирует модели на языке TypeScript в модели на Java

### Подключение через Maven Central

    testCompile "kz.greetgo.type_script:greetgo.ts_java_convert:0.0.1"

### Что делает и как использовать

Например имеются файлы:

<table>

<tr><td>
some/ts/dir/MainClass.ts
</td><td>

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

</td></tr>

<tr><td>
some/ts/dir/AnotherClass.ts :
</td><td>

```typescript
export class AnotherClass {
  public intField: number/*int*/;
}
```
</td></tr>

<tr><td>
some/ts/dir/SomeEnum.ts :
</td><td>

```typescript
export enum SomeEnum {
  FIELD1 = "FIELD1",
  FIELD2 = "FIELD2",
}
```

</td></tr>
</table>


И их нужно преобразовать в следующие классы Java:

<small>java/destination/src/dir/some/package_from/ts/MainClass.java :</small>
```java
package some.package_from.ts;

public class MainClass {
  public String strField;
  public int intField;
  public AnotherClass anotherField;
  public SomeEnum enumField;
}
```
<small>java/destination/src/dir/some/package_from/ts/AnotherClass.java :</small>
```java
package some.package_from.ts;

public class AnotherClass {
  public int intField;
}
```
<small>java/destination/src/dir/some/package_from/ts/SomeEnum.java :</small>
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
        .create().execute();
  }

}
```