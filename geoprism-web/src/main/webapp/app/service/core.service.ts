import { Injectable } from '@angular/core';
import { Response } from '@angular/http';

import { RunwayException } from '../model/runway-exception';

export interface IEventListener {
  start(): void;
  complete(): void;
  onError(error:RunwayException): void;
}

@Injectable()
export class EventService {
  private listeners: IEventListener[] = [];

  public constructor() {}
  
  public registerListener(listener: IEventListener): void {
   this.listeners.push(listener);
  }
  
  public deregisterListener(listener: IEventListener): boolean {
    let indexOfItem = this.listeners.indexOf(listener);

    if (indexOfItem === -1) {
      return false;
    }

    this.listeners.splice(indexOfItem, 1);

    return true;
  }
  
  public start(): void {
    for (const listener of this.listeners) {
      listener.start();
    }
  }
  
  public complete(): void {
    for (const listener of this.listeners) {
      listener.complete();
    }
  }
  
  public onError(error:any) : void {
    let rError = null;
	
	if(error instanceof Response) {
      rError = error.json() as RunwayException;
	}
	else {
      rError = JSON.parse(error) as RunwayException;
	}

    for (const listener of this.listeners) {
      listener.onError(rError);
    }  
  }
}

@Injectable()
export class BasicService {
  service: EventService;

  constructor(service: EventService) {
    this.service = service;
  }

  protected handleError(error: any): Promise<any> {
   /*
    * Must add the null check on this because the this reference gets messed up when
    * this code is executed from ng2 zone.js
    */
	  
    if(this != null) {
      this.service.onError(error);     
    }
      
    return Promise.reject(error);
  }
}

@Injectable()
export class IdService {

  constructor() {}
  
  generateId(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
      return v.toString(16);
    });
  }    
}
