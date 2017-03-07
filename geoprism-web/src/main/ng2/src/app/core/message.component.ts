import { Component, OnInit } from '@angular/core';

import { EventService, IEventListener } from '../service/core.service';

@Component({
  
  selector: 'message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit, IEventListener {
	
  private error: any = null;
  
  private message: string = "";

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
  
  onMessage(msg: string): void {
    this.message = msg;
  }
}
