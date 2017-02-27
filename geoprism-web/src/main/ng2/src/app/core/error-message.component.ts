import { Component, OnInit } from '@angular/core';

import { EventService, IEventListener } from '../service/core.service';

@Component({
  
  selector: 'error-message',
  templateUrl: './error-message.component.html',
  styleUrls: ['./error-message.component.css']
})
export class ErrorMessageComponent implements OnInit, IEventListener {
	
  private error: any = null;

  constructor(private service: EventService) { }

  ngOnInit(): void {
    this.service.registerListener(this);
  }
  
  ngOnDestroy(): void {
    this.service.deregisterListener(this);
  }
  
  start(): void {
    this.error = null;
  }
  
  complete(): void {
    console.log('complete');
  }  
  
  onError(error: any): void {
    this.error = error;
  }
}
