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
/// License along with Runway SDK(tm).  If not, see <ehttp://www.gnu.org/licenses/>.
///

import { Injectable } from '@angular/core';
import { Headers, Http, Response, URLSearchParams } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { EventService, BasicService } from '../../core/service/core.service';
import { EventHttpService } from '../../core/service/event-http.service';

import { Category, BasicCategory } from '../model/category';

declare var acp: any;

@Injectable()
export class CategoryService extends BasicService {
  
  constructor(service: EventService, private ehttp: EventHttpService, private http: Http) {
    super(service); 
  }
  
  getAll(): Promise<BasicCategory[]> {
    return this.ehttp
      .get(acp + '/category/all')
      .toPromise()
      .then(response => {
        return response.json() as BasicCategory[];
      })
      .catch(this.handleError.bind(this));
  }
  
  edit(parentId: string, oid : string): Promise<Category> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.ehttp
      .post(acp + '/category/edit', JSON.stringify({parentId:parentId, oid:oid}), { headers: headers })
      .toPromise()
      .then(response => {
        return response.json() as Category;
      })      
      .catch(this.handleError.bind(this));
  }
  
  get(oid : string): Promise<Category> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.ehttp
    .post(acp + '/category/get', JSON.stringify({oid:oid}), { headers: headers })
    .toPromise()
    .then(response => {
      return response.json() as Category;
    })
    .catch(this.handleError.bind(this));
  }
  
  unlock(oid:string): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.ehttp
      .post(acp + '/category/unlock', JSON.stringify({oid:oid}), { headers: headers })
      .toPromise()
      .catch(this.handleError.bind(this));
  }
  
  apply(config: any): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
       
    return this.ehttp
    .post(acp + '/category/apply', JSON.stringify({config:config}), { headers: headers })
    .toPromise()
    .catch(this.handleError.bind(this));
  }
  
  remove(oid: string): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });
    
    return this.ehttp
      .post(acp + '/category/remove', JSON.stringify({oid:oid}), { headers: headers })
      .toPromise()
      .catch(this.handleError.bind(this));
  }
  
  validate(name: string, oid:string): Promise<Response> {
    
    let params: URLSearchParams = new URLSearchParams();
    params.set('name', name);
    params.set('oid', oid);    
    
    return this.http
      .get(acp + '/category/validate', {search: params})
      .toPromise();
  }
  
    
  create(label: string, parentId: string, validate: boolean): Promise<BasicCategory> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let option = {label:label, parentId:parentId, validate:validate};
    
    return this.ehttp
    .post(acp + '/category/create', JSON.stringify({option:option}), { headers: headers })
    .toPromise()
    .then((response:any) => {
      return response.json() as BasicCategory;
    })          
    .catch(this.handleError.bind(this));
  }
  
  update(category:Category): Promise<Category> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.ehttp
    .post(acp + '/category/update', JSON.stringify({category:category}), { headers: headers })
    .toPromise()
    .then((response:any) => {
      return response.json() as Category;
    })          
    .catch(this.handleError.bind(this));
  }
  
}
