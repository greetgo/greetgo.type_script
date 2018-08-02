export class ClassWithStringField {

  public strField: string;
  public strOrNullField: string | null;
  public strOrNullField2: null | string;
  public strArrayField: string[];
  public strArrayOrNullField: string[] | null;
  public strArrayOrNullField2: null | string[];

  // with init

  public strField_init!: string = "asd";
  public strOrNullField_init!: string | null = null;
  public strOrNullField2_init!: null | string = "asd";
  public strArrayField_init!: string[] = ["wow"];
  public strArrayOrNullField_init!: string[] | null = null;
  public strArrayOrNullField2_init!: null | string[] = [null, "wow"];
}
