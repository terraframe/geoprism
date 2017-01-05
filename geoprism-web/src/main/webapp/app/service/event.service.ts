import { Injectable } from '@angular/core';

export interface IHttpEventListener {
  start(): void;
  complete(): void;
}

@Injectable()
export class EventService {
  private listeners: IHttpEventListener[] = [];

  public constructor() {}
  
  public registerListener(listener: IHttpEventListener): void {
   this.listeners.push(listener);
  }
  
  public deregisterListener(listener: IHttpEventListener): boolean {
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
}