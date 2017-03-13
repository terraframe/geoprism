import {Directive, Input, Output, EventEmitter, HostListener} from "@angular/core";

import { ConfirmService, IAction } from "./confirm-modal.service";

@Directive({
  selector: "[confirm-modal]"
})
export class ConfirmModalDirective implements IAction {

  @Input() enabled: boolean = true;
  @Input() message: string = "Are you sure?";
    
  @Output() onConfirm = new EventEmitter();

  constructor(private service: ConfirmService) {
  }
  
  /** 
   * On key event is triggered when a key is released on the host component
   * the event starts a timer to prevent concurrent requests
   */
  @HostListener('click')
  public onClick(): void {
    if(this.enabled){
      this.service.open(this);
    }
    else {
      this.onConfirm.emit();    	
    }
  }
  
  getMessage(): string {
    return this.message;
  }
  
  confirm(): void {
    this.onConfirm.emit();    	
  }
  
  cancel(): void {
	  
  }
}