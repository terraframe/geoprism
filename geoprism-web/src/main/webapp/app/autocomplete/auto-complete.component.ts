import { Component, ElementRef, Input, Output, OnInit, ViewEncapsulation, EventEmitter, ViewChild} from "@angular/core";

@Component({
  moduleId: module.id,	
  selector: "auto-complete",
  templateUrl: 'auto-complete.component.jsp',
  styleUrls: ['auto-complete.component.css'],
})
export class AutoCompleteComponent {

  // Emit a selected event when an item in the list is selected
  @Output() public onDropdownSelect = new EventEmitter<{text: string, data: any}>();

  public list: any;

  /**
   * Listen for a click event on the list
   */
  public onClick(item: {text: string, data: any}) {
    this.onDropdownSelect.emit(item);
  }
}