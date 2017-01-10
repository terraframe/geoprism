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

import { EventService, BasicService } from './core.service';

import { EventHttpService } from './event-http.service';

import { Category, BasicCategory } from '../model/category';

declare var acp: any;

@Injectable()
export class CategoryService extends BasicService {
	
  constructor(service: EventService, private ehttp: EventHttpService, private http: Http) {
    super(service); 
  }
  
  getAll(): Promise<Category[]> {
    return this.ehttp
      .get(acp + '/category/all')
      .toPromise()
      .then(response => {
        return response.json() as Category[];
      })
      .catch(this.handleError);
  }
  
  edit(parentId: string, id : string): Promise<Category> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.ehttp
      .post(acp + '/category/edit', JSON.stringify({parentId:parentId, id:id}), { headers: headers })
      .toPromise()
      .then(response => {
        return response.json() as Category;
      })      
      .catch(this.handleError);
  }
  
  get(id : string): Promise<Category> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.ehttp
    .post(acp + '/category/get', JSON.stringify({id:id}), { headers: headers })
    .toPromise()
    .then(response => {
      return response.json() as Category;
    });
  }
  
  unlock(category: Category): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.ehttp
      .post(acp + '/category/unlock', JSON.stringify({id:category.id}), { headers: headers })
      .toPromise()
      .catch(this.handleError);
  }
  
  apply(config: any): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
       
    return this.ehttp
    .post(acp + '/category/apply', JSON.stringify({config:config}), { headers: headers })
    .toPromise()
    .catch(this.handleError);
  }
  
  remove(category: BasicCategory): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });
    
    return this.ehttp
      .post(acp + '/category/remove', JSON.stringify({id:category.id}), { headers: headers })
      .toPromise()
      .catch(this.handleError);
  }
  
  validate(name: string, id:string): Promise<Response> {
	  
	let params: URLSearchParams = new URLSearchParams();
    params.set('name', name);
    params.set('id', id);	  
	  
    return this.http
      .get(acp + '/category/validate', {search: params})
      .toPromise()
      .catch(this.handleError);
  }
  
    
  create(label: string, parentId: string): Promise<BasicCategory> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let option = {label:label, parentId:parentId, validate:false};
    
    return this.ehttp
    .post(acp + '/category/create', JSON.stringify({option:option}), { headers: headers })
    .toPromise()
    .then((response:any) => {
      return response.json() as BasicCategory;
    })          
    .catch(this.handleError);
  }
  
  update(category:Category): Promise<Category> {
	  let headers = new Headers({
		  'Content-Type': 'application/json'
	  });    
	  
	  return this.ehttp
	  .post(acp + '/category/update', JSON.stringify({category:category}), { headers: headers })
	  .toPromise()
	  .then((response:any) => {
		  return response.json() as BasicCategory;
	  })          
	  .catch(this.handleError);
  }
  
}
