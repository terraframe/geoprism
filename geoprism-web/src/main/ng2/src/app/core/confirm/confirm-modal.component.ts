import { Component, OnInit} from "@angular/core";

import { ConfirmService, IListener, IAction } from "./confirm-modal.service";

@Component({
    
  selector: "confirm-modal",
  templateUrl: './confirm-modal.component.html',
  styles: [
    '.confirm-overlay { z-index: 99998; }',
    '.confirm-modal { background: #fff; position: absolute; color: #222; top: 50%; left: 50%; -ms-transform: translateX(-50%) translateY(-50%); -webkit-transform: translate(-50%, -50%); transform: translate(-50%, -50%); z-index: 99999; }',
    '.confirm-text { display: block; width: auto; min-height: 22px; max-height: none; height: auto;}'
  ],
})
export class ConfirmModalComponent implements OnInit, IListener  {
	
  private action: IAction;
  private message: string;
  private active: boolean = false;
	
  constructor(private service: ConfirmService) { }  
  
  ngOnInit(): void {
    this.service.registerListener(this);
  }
  
  ngOnDestroy(): void {
    this.service.deregisterListener(this);
  }
  
  public open(action: IAction): void {
    if(this.action == null) {
      this.action = action;

      this.message = action.getMessage();
      this.active = true;
    }
  }
  
  public confirm(): void {
    if(this.action != null) {
      this.action.confirm();
      
      this.action = undefined;      
    }
    
    this.active = false;
    this.message = undefined;
  }
  
  public cancel(): void {
    if(this.action != null) {
      this.action.cancel();
      
      this.action = undefined;      
    }
    
    this.active = false;
    this.message = undefined;
  }
}