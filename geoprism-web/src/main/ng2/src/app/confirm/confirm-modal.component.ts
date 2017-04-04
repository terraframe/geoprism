import { Component, OnInit} from "@angular/core";

import { ConfirmService, IListener, IAction } from "./confirm-modal.service";

@Component({
    
  selector: "confirm-modal",
  templateUrl: './confirm-modal.component.html',
  styleUrls: ['./confirm-modal.component.css'],
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