import { Component, ElementRef, Input, Output, OnInit, ViewEncapsulation, EventEmitter, ViewChild} from "@angular/core";

@Component({
  moduleId: module.id,  
  selector: "confirm-modal",
  templateUrl: 'confirm-modal.component.jsp',
  styleUrls: ['confirm-modal.component.css'],
})
export class ConfirmModalComponent {

  @Output() public onConfirm = new EventEmitter<void>();
  @Output() public onCancel = new EventEmitter<void>();

  message: String;
  
  public setMessage(message: string): void {
    this.message = message;
  }
  
  public confirm(): void {
    this.onConfirm.emit();
  }
  
  public cancel(): void {
    this.onCancel.emit();
  }
}