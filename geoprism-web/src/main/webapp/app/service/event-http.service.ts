import { Injectable } from '@angular/core';
import { Http, RequestOptions, RequestOptionsArgs, Response, ConnectionBackend } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { EventService } from './event.service';

@Injectable()
export class EventHttpService extends Http {
  private currentRequests: number = 0;

  public constructor(_backend: ConnectionBackend, _defaultOptions: RequestOptions, private service: EventService) {
    super(_backend, _defaultOptions);
  }
  
  public get(url: string, options?: RequestOptionsArgs) : Observable<Response> {
    this.incrementRequestCount();
    
    var response = super.get(url, options);
    response.subscribe(null, error => {
      this.decrementRequestCount();
    }, () => {
      this.decrementRequestCount();
    });
    return response;
  }
  
  public post(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
    this.incrementRequestCount();
    
    var response = super.post(url, body, options);
    response.subscribe(null, error => {
      this.decrementRequestCount();
    }, () => {
      this.decrementRequestCount();
    });
    return response;    
  }

  private decrementRequestCount() {
    if (--this.currentRequests == 0) {
      this.service.complete();
    }
  }

  private incrementRequestCount() {
    if (this.currentRequests++ == 0) {
      this.service.start();
    }
  }
}