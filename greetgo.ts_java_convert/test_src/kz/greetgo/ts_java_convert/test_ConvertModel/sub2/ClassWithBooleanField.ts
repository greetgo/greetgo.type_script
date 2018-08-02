export class ClassWithBooleanField {

  public boolField: boolean;
  public boolOrNullField: boolean | null;
  public boolArrayField: boolean [];
  public boolArrayOrNullField: boolean [] | null;

  public boolField_null: null | boolean;
  public boolField_null_array: null | boolean [];

  // with init

  public boolField_init!: boolean = true;
  public boolOrNullField_init!: boolean | null = true;
  public boolArrayField_init!: boolean [] = [true];
  public boolArrayOrNullField_init!: boolean [] | null = [true];
  public boolField_null_init!: null | boolean = true;
  public boolField_null_array_init!: null | boolean [] = [true];
}
