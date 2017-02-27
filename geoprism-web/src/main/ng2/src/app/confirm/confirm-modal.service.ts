import { Injectable } from "@angular/core";

export interface IAction {
  getMessage(): string;
  confirm(): void;
  cancel(): void;
}

export interface IListener {
  open(action: IAction): void;
}

@Injectable()
export class ConfirmService {
  private listeners: IListener[] = [];

  public constructor() {}
  
  public registerListener(listener: IListener): void {
    this.listeners.push(listener);
  }
  
  public deregisterListener(listener: IListener): void {
    this.listeners = this.listeners.filter(h => h !== listener);    
  }
  
  public open(action: IAction): void {
    for (const listener of this.listeners) {
      listener.open(action);
    }
  }  
}

