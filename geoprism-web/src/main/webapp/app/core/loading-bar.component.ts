import { Component, OnInit } from '@angular/core';

import { EventService, IEventListener } from '../service/core.service';

@Component({
  moduleId: module.id,
  selector: 'loading-bar',
  templateUrl: 'loading-bar.component.jsp',
  styleUrls: []
})
export class LoadingBarComponent implements OnInit, IEventListener {
  showIndicator: boolean = false;

  constructor(private service: EventService) { }

  ngOnInit(): void {
    this.service.registerListener(this);
  }
  
  ngOnDestroy(): void {
    this.service.deregisterListener(this);
  }
  
  start(): void {
    this.showIndicator = true;    
  }
  
  complete(): void {
    this.showIndicator = false;    
  }  
  
  onError(error:any): void {
    console.log('error');
  }
}
