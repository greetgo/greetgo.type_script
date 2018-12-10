export class BpmListRow {
  public id: string;
  public name: string;
  public productVersion: string;
  public draftVersion: string;

  assign(a: any) {
    this.id = a.id;
    this.name = a.name;
    this.productVersion = a.productVersion;
    this.draftVersion = a.draftVersion;
  }

  static of(a: any) {
    let ret = new BpmListRow();
    ret.assign(a);
    return ret;
  }
}
