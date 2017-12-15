export class Class1 {
  public fieldStr: string;
  public fieldStr0: string | null;
  public fieldInt: number/*int*/;
  public fieldLong: number/*long*/;

  public assign(a: any) {
    this.fieldStr = a.fieldStr;
    this.fieldStr0 = a.fieldStr0;
    this.fieldInt = a.fieldInt;
    this.fieldLong = a.fieldLong;
  }

  public static copyFrom(a: any): Class1 {
    let ret = new Class1();
    ret.assign(a);
    return ret;
  }
}