import {BaseClass} from "./base/BaseClass";
import {SuperTestInterface1} from "../out_of_model/SuperTestInterface1";
import {SuperTestInterface2} from "../out_of_model/SuperTestInterface2";

export class ChildClass2 extends BaseClass implements SuperTestInterface1, SuperTestInterface2 {
  public wowAttr2: string;
}