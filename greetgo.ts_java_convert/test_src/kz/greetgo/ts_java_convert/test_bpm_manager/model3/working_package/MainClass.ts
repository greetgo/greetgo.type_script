import {RefInSamePackage} from "./RefInSamePackage";
import {RefInSubPackage} from "./sub_package/RefInSubPackage";
import {RefInParentPackage} from "../RefInParentPackage";
import {RefInParallelPackage} from "../parallel_package/RefInParallelPackage";

export class MainClass {
  public atr1: string;
  public atrRefInSamePackage: RefInSamePackage;
  public atrRefInSubPackage: RefInSubPackage;
  public atrRefInParentPackage: RefInParentPackage;
  public atrRefInParallelPackage: RefInParallelPackage;
}