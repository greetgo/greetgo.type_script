import {AnotherClass} from "./AnotherClass";

export class ClassWithClassField {

  public field: AnotherClass;
  public fieldArray: AnotherClass[];
  public fieldNull: AnotherClass | null;
  public fieldArrayNull1: AnotherClass[] | null;
  public fieldArrayNull2: AnotherClass | null [];

  public nullField: null | AnotherClass;
  public nullFieldArray: null | AnotherClass [];

}
