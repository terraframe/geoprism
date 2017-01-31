import {Directive, Input, Output, EventEmitter, ComponentRef, ViewContainerRef, ElementRef, ComponentFactoryResolver, OnInit, HostListener} from "@angular/core";

import { ConfirmModalComponent } from "./confirm-modal.component";

@Directive({
  selector: "[confirm-modal], [confirm-modal]"
})
export class ConfirmModalDirective implements OnInit {

  @Input() enable: boolean = true;
  @Input() message: string = "Are you sure?";
    
  @Output() onConfirm = new EventEmitter();
  
  private component: ComponentRef<ConfirmModalComponent> = undefined;

  constructor(
    private resolver: ComponentFactoryResolver,
    private viewContainerRef: ViewContainerRef,
    private el: ElementRef) {
	  
    console.log('Constructing'); 
  }
  
  public ngOnInit() {
    console.log('On init');	  
  }  
  /** 
   * On key event is triggered when a key is released on the host component
   * the event starts a timer to prevent concurrent requests
   */
  @HostListener('click')
  public onClick(): void {
    if (!this.component) {    	
      let factory = this.resolver.resolveComponentFactory(ConfirmModalComponent);

      this.component = this.viewContainerRef.createComponent(factory);      
      
      (<ConfirmModalComponent>(this.component.instance)).setMessage(this.message);
        
      (<ConfirmModalComponent>(this.component.instance)).onConfirm.subscribe(() => {
        this.onDestroy();
        
        this.onConfirm.emit();        
      });
      
      (<ConfirmModalComponent>(this.component.instance)).onCancel.subscribe(() => {
        this.onDestroy();
      });
    }
  }

  /**
   * remove the list component
   */
  private onDestroy() {
    if (this.component) {
      this.component.destroy();
      this.component = undefined;
    }
  }
}