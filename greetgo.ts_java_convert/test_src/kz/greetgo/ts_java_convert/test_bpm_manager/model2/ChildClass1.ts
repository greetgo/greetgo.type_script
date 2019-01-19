import {BaseClass} from "./BaseClass";
import {SuperTestInterface1} from "../out_of_model/SuperTestInterface1";

export class ChildClass1 extends BaseClass implements SuperTestInterface1 {
  public wowAttr: string;
}