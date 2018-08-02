import { Injectable } from '@angular/core';
import { Headers, Http, Response, URLSearchParams } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { EventService, BasicService } from '../../core/service/core.service';
import { EventHttpService } from '../../core/service/event-http.service';

import { ExcelImportHistory } from './upload-manager.model';

import { Observable } from 'rxjs/Observable';

declare var acp: any;

@Injectable()
export class UploadManagerService extends BasicService {

  constructor(service: EventService, private ehttp: EventHttpService, private http: Http) { super(service); }

  getAllHistory(): Promise<ExcelImportHistory[]> {
    return this.ehttp
      .get(acp + '/net.geoprism.data.importer.ExcelController.getAllHistory.mojo')
      .toPromise()
      .then(response => {
    	  
        return response.json() as ExcelImportHistory[];
      })
      .catch(e => {
    	  this.handleError.bind(this)
    	  
    	  return [];
      });
  }
  
  pollAllHistory(): Observable<ExcelImportHistory[]> {
    return Observable.interval(5000)
      .flatMap(() => this.http.get(acp + '/net.geoprism.data.importer.ExcelController.getAllHistory.mojo').catch(err => {console.log("ignoring error: " + err); return Observable.empty() as Observable<Response>}))
      .map(res => res.json() as ExcelImportHistory[])
  }
  
  clearHistory(): Promise<void | Response> {
	
    return this.ehttp
    .get(acp + '/net.geoprism.data.importer.ExcelController.clearHistory.mojo')
    .toPromise()
    .catch(e => {
    	  	this.handleError.bind(this)
    });
  }
}
