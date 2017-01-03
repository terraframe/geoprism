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
import { Headers, Http, Response, URLSearchParams } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';

import { Category } from '../model/category';

declare var acp: any;

@Injectable()
export class CategoryService extends BasicService {
  constructor(private http: Http) { super(); }
//    
//    service.createOption = function(connection, option) {
//      var request = runwayService.createConnectionRequest(connection);
//      
//      net.geoprism.ontology.ClassifierController.createOption(request, option);
//    }
//    
//    service.deleteOption = function(connection, id) {
//      var request = runwayService.createConnectionRequest(connection);
//      
//      net.geoprism.ontology.ClassifierController.deleteOption(request, id);
//    }
//    
//    service.applyOption = function(connection, config) {
//      var request = runwayService.createConnectionRequest(connection);
//      
//      net.geoprism.ontology.ClassifierController.applyOption(request, config);
//    }
//    
//    service.updateCategory = function(connection, category) {
//      var request = runwayService.createConnectionRequest(connection);
//      
//      net.geoprism.ontology.ClassifierController.updateCategory(request, category);
//    }
  

  getAll(): Promise<Category[]> {
    return this.http
      .get(acp + '/category/all')
      .toPromise()
      .then(response => {
        return response.json() as Category[];
      })
      .catch(this.handleError);
  }
  
  edit(id : string): Promise<Category> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.http
      .post(acp + '/category/edit', JSON.stringify({id:id}), { headers: headers })
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
    
    return this.http
    .post(acp + '/category/get', JSON.stringify({id:id}), { headers: headers })
    .toPromise()
    .then(response => {
      return response.json() as Category;
    })      
    .catch(this.handleError);
  }
  
  unlock(category: Category): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.http
      .post(acp + '/category/unlock', JSON.stringify({id:category.id}), { headers: headers })
      .toPromise()
      .catch(this.handleError);
  }
  
  apply(category: Category): Promise<Category> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.http
    .post(acp + '/category/apply', JSON.stringify({category:category}), { headers: headers })
    .toPromise()
    .then(response => {
      return response.json() as Category;
    })          
    .catch(this.handleError);
  }
  
  remove(category: Category): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.http
      .post(acp + '/category/remove', JSON.stringify({id:category.id}), { headers: headers })
      .toPromise()
      .catch(this.handleError);
  }
  
  validate(name: string, id:string): Promise<Response> {
	  
	let params: URLSearchParams = new URLSearchParams();
    params.set('name', name);
    params.set(id, id);	  
	  
    return this.http
      .get(acp + '/category/validate', params)
      .toPromise()
      .catch(this.handleError);
  }
  
}
