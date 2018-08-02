import {AnotherClass} from "./AnotherClass";

export class ClassWithClassField {

  public field: AnotherClass;
  public fieldArray: AnotherClass[];
  public fieldNull: AnotherClass | null;
  public fieldArrayNull1: AnotherClass[] | null;
  public fieldArrayNull2: AnotherClass | null [];

  public nullField: null | AnotherClass;
  public nullFieldArray: null | AnotherClass [];

  //with init

  public field_init!: AnotherClass = null;
  public fieldArray_init!: AnotherClass[] = null;
  public fieldNull_init!: AnotherClass | null = null;
  public fieldArrayNull1_init!: AnotherClass[] | null = null;
  public fieldArrayNull2_init!: AnotherClass | null [] = null;
  public nullField_init!: null | AnotherClass = null;
  public nullFieldArray_init!: null | AnotherClass [] = null;

}
