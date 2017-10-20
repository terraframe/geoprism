import {Component, Input, Output, EventEmitter} from '@angular/core';

declare var MDSS:any;
declare var dss:any;
declare var acp:string;

@Component({    
  selector: 'boolean-field',
  templateUrl: './boolean-field.component.html',
  styles: ['.modal-form .holder .check-block .chk-area {margin: 10px 0px 0 0;}']
})
export class BooleanFieldComponent {

  @Input() value:boolean = false;  
  @Input() localizeLabelKey:string = ""; // localization key used to localize in the component template
  @Input() label:string = ""; // raw string input
  
  @Output() public valueChange = new EventEmitter<boolean>();

  constructor(){}
  
  toggle():void {
    this.value = !this.value;
    
    this.valueChange.emit(this.value);
  }
}