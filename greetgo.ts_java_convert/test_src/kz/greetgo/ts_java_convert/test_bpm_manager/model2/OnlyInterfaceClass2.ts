import {SuperTestInterface1} from "../out_of_model/SuperTestInterface1";
import {SuperTestInterface2} from "../out_of_model/SuperTestInterface2";

export class OnlyInterfaceClass2 implements SuperTestInterface1, SuperTestInterface2 {
  public onlyInterfaceAttr2: string;
}