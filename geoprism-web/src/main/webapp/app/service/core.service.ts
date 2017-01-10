import { Injectable } from '@angular/core';

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
	let rError = error.json() as RunwayException
	
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

//  protected handleError(error: any): Promise<any> {
//    console.error('An error occurred', error);
//    
//    return Promise.reject(error.json() as RunwayException);
//  }
  
  protected handleError(error: any): Promise<any> {
    if(this != null) {
      this.service.onError(error);     	
    }
    	  
    return Promise.reject(error);
  }
}
