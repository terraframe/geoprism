///
/// Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
///
/// This file is part of Runway SDK(tm).
///
/// Runway SDK(tm) is free software: you can redistribute it and/or modify
/// it under the terms of the GNU Lesser General Public License as
/// published by the Free Software Foundation, either version 3 of the
/// License, or (at your option) any later version.
///
/// Runway SDK(tm) is distributed in the hope that it will be useful, but
/// WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU Lesser General Public License for more details.
///
/// You should have received a copy of the GNU Lesser General Public
/// License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { Headers, Http, Response, URLSearchParams, RequestOptions, ResponseContentType } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/toPromise';

import { EventService, BasicService } from '../core/service/core.service';
import { EventHttpService } from '../core/service/event-http.service';
import { Progress } from '../core/progress-bar/progress';

import { Pair } from '../model/pair';
import { Sheet, Workbook, GeoSynonym, ClassifierSynonym, Entity, DatasetResponse } from '../uploader/uploader-model';

declare var acp: any;

@Injectable()
export class UploadService extends BasicService {

  constructor(service: EventService, private ehttp: EventHttpService, private http: Http) { super(service); }

  getSavedConfiguration(id: string, sheetName: string): Promise<any> {
    
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({id:id, sheetName: sheetName});
    
    return this.ehttp
      .post(acp + '/uploader/getSavedConfiguration', data, {headers: headers})
      .toPromise() 
      .then((response: any) => {
        return response.json();
      })           
      .catch(this.handleError.bind(this));
  }
    
  cancelImport(workbook: Workbook): Promise<Response> {
    
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({configuration : workbook });
    
    return this.ehttp
      .post(acp + '/uploader/cancelImport', data, {headers: headers})
      .toPromise() 
      .catch(this.handleError.bind(this));
  }
  
  importData(workbook: Workbook): Observable<DatasetResponse> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({configuration : workbook });
    
    return this.http
      .post(acp + '/uploader/importData', data, {headers:headers})
      .catch(this.handleError.bind(this));
  }
  
  getErrorFile(id:string): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({id : id });
    
    let options = new RequestOptions({responseType: ResponseContentType.Blob, headers });
    
    return this.ehttp
    .post(acp + '/uploader/getErrorFile', data, options)
    .toPromise() 
    .catch(this.handleError.bind(this));
  }
  
  getGeoEntitySuggestions(parentId: string, universalId: string, text: string, limit: string): Promise<Array<{ text: string, data: any }>> {
    
    let params: URLSearchParams = new URLSearchParams();
    params.set('parentId', parentId);
    params.set('universalId', universalId);    
    params.set('text', text);    
    params.set('limit', limit);    
    
    return this.http
      .get(acp + '/uploader/getGeoEntitySuggestions', {search: params})
      .toPromise()
      .then((response: any) => {
        return response.json() as Array<{ text: string, data: any }>;
      })
      .catch(this.handleError.bind(this));    
  }

  createGeoEntitySynonym(entityId: string, label: string): Promise<GeoSynonym> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({entityId: entityId, label: label });
    
    return this.ehttp
      .post(acp + '/uploader/createGeoEntitySynonym', data, {headers: headers})
      .toPromise() 
      .then((response: any) => {
        return response.json() as GeoSynonym;
      })      
      .catch(this.handleError.bind(this));
  }
  
  createGeoEntity(parentId: string, universalId: string, label: string): Promise<Entity> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({parentId: parentId, universalId: universalId, label: label });
    
    return this.ehttp
      .post(acp + '/uploader/createGeoEntity', data, {headers: headers})
      .toPromise() 
      .then((response: any) => {
        return response.json() as Entity;
      })      
      .catch(this.handleError.bind(this));    
  }
  
  deleteGeoEntity(entityId: string): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({entityId: entityId});
    
    return this.ehttp
    .post(acp + '/uploader/deleteGeoEntity', data, {headers: headers})
    .toPromise() 
    .catch(this.handleError.bind(this));    
  }
  
  deleteGeoEntitySynonym(synonymId: string): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({synonymId: synonymId});
    
    return this.ehttp
    .post(acp + '/uploader/deleteGeoEntitySynonym', data, {headers: headers})
    .toPromise() 
    .catch(this.handleError.bind(this));    
  }
    
  getClassifierSuggestions(mdAttributeId: string, text: string, limit: string): Promise<Array<{ text: string, data: any }>> {
    
    let params: URLSearchParams = new URLSearchParams();
    params.set('mdAttributeId', mdAttributeId);
    params.set('text', text);    
    params.set('limit', limit);    
  
    return this.http
      .get(acp + '/uploader/getClassifierSuggestions', {search: params})
      .toPromise()
      .then((response: any) => {
        return response.json() as Array<{ text: string, data: any }>;
      })
      .catch(this.handleError.bind(this));    
  }
  
  createClassifierSynonym(classifierId: string, label: string): Promise<ClassifierSynonym> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({classifierId: classifierId, label: label });
    
    return this.ehttp
    .post(acp + '/uploader/createClassifierSynonym', data, {headers: headers})
    .toPromise() 
    .then((response: any) => {
      return response.json() as ClassifierSynonym;
    })      
    .catch(this.handleError.bind(this));
  }
  
  deleteClassifierSynonym(synonymId: string): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({synonymId: synonymId});
    
    return this.ehttp
    .post(acp + '/uploader/deleteClassifierSynonym', data, {headers: headers})
    .toPromise() 
    .catch(this.handleError.bind(this));    
  }
  
  progress(id: string): Promise<Progress> {
    let params: URLSearchParams = new URLSearchParams();
    params.set('id', id);
    
    return this.http
      .get(acp + '/uploader/progress', {search: params})
      .toPromise()
      .then((response: any) => {
        return response.json();
      })
      .catch(this.handleError.bind(this));    
  }
  
  
}
