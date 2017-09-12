import { Component, ElementRef, Input, Output, OnInit, ViewEncapsulation, EventEmitter, ViewChild} from "@angular/core";

class Item {
  text: string;
  data: any;
  selected: boolean;

  constructor(item: {text: string, data: any}) {
    this.text = item.text;
    this.data = item.data;
    this.selected = false;
  }
}

@Component({
    
  selector: "auto-complete-component",
  templateUrl: './auto-complete.component.html',
  styles: [
    '.search-results { position: relative; right: 0; display: block; overflow-y: auto; overflow-x: hidden; max-height: 100px; }',
	'.search-hover { background:#DEDEDE; }'
  ],
})
export class AutoCompleteComponent {

  // Emit a selected event when an item in the list is selected
  @Output() public onDropdownSelect = new EventEmitter<{text: string, data: any}>();

  list: Item[];
  index: number;
  

  /**
   * Listen for a click event on the list
   */
  public onClick(item: Item): void {
    this.onDropdownSelect.emit(item);
  }
  
  public onMouseEnter(item: Item, index: number): void {
    item.selected = true;
    
    this.index = index;
  }
  
  public onMouseLeave(item: Item, index: number): void {
    item.selected = false;
    
    this.index = -1;
  }
  
  public up(): void {  
    if(this.index === -1) {
      this.index = (this.list.length - 1);
    }
    else {
      this.list[this.index].selected = false;
    	
      this.index--;
    }
    
    if(this.index < 0) {
      this.index = (this.list.length - 1);
    }
    
    this.list[this.index].selected = true;    
  }
  
  public down(): void {	  
    if(this.index === -1) {
      this.index = 0;
    }
    else {
      this.list[this.index].selected = false;
      
      this.index++;      
    }
    
    if(this.index === this.list.length) {
      this.index = 0;
    }
    
    this.list[this.index].selected = true;
  }
  
  public enter(): void {
    this.onDropdownSelect.emit(this.list[this.index]);    
  }
  
  public setOptions(items: {text: string, data: any}[]): void {
    this.list = [];
    this.index = -1;
    
    items.forEach(item => {
      this.list.push(new Item(item));
    });
  }
}