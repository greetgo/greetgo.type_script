import {BpmRecord} from './BpmRecord';

export class BpmManagerContent {
  public name: string;
  public draft: BpmRecord;
  public product: BpmRecord;
  public availableDrafts: BpmRecord[] = [];
}
