export class ClassWithNumberField {

  public numberIntField: number /* int */;
  public numberIntArrayField1: number /* int */ [];
  public numberIntArrayField2: number [] /* int */;
  public numberIntOrNullField1: number /* int */ | null;
  public numberIntOrNullField2: number | /* int */ null;
  public numberIntOrNullField3: number | null /* int */;
  public numberIntOrNullField4: null | number /* int */;
  public numberLongField: number /* long */;
  public numberLongArrayField1: number /* long */ [];
  public numberLongArrayField2: number [] /* long */;
  public numberLongOrNullField1: number /* long */ | null;
  public numberLongOrNullField2: number | /* long */ null;
  public numberLongOrNullField3: number | null /* long */;
  public numberLongOrNullField4: null | number /* long */;

  // with init

  public numberIntField_init!: number /* int */ = 123;
  public numberIntArrayField1_init!: number /* int */ [] = [123];
  public numberIntArrayField2_init!: number [] /* int */ = [123];
  public numberIntOrNullField1_init!: number /* int */ | null = 123;
  public numberIntOrNullField2_init!: number | /* int */ null = 123;
  public numberIntOrNullField3_init!: number | null /* int */ = 123;
  public numberIntOrNullField4_init!: null | number /* int */ = 123;
  public numberLongField_init!: number /* long */ = 123;
  public numberLongArrayField1_init!: number /* long */ [] = [123];
  public numberLongArrayField2_init!: number [] /* long */ = [123];
  public numberLongOrNullField1_init!: number /* long */ | null = 123;
  public numberLongOrNullField2_init!: number | /* long */ null = 123;
  public numberLongOrNullField3_init!: number | null /* long */ = 123;
  public numberLongOrNullField4_init!: null | number /* long */ = 123;
}
