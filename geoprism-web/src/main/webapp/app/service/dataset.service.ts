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
import { Headers, Http, Response } from '@angular/http';

import 'rxjs/add/operator/toPromise';

import { BasicService } from './basic.service';

import { Dataset } from '../model/dataset';

declare var acp: any;

@Injectable()
export class DatasetService extends BasicService {
  constructor(private http: Http) { super(); }

  getDatasets(): Promise<Dataset[]> {
    return this.http
      .get(acp + '/prism/datasets')
      .toPromise()
      .then(response => {
        return response.json() as Dataset[];
      })
      .catch(this.handleError);
  }
  
  edit(id : string): Promise<Dataset> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });	  
	  
    return this.http
      .post(acp + '/prism/edit-dataset', JSON.stringify({id:id}), { headers: headers })
      .toPromise()
      .then(response => {
        return response.json() as Dataset;
      })      
      .catch(this.handleError);
  }
  
  unlock(dataset: Dataset): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.http
      .post(acp + '/prism/unlock-dataset', JSON.stringify({id:dataset.id}), { headers: headers })
      .toPromise()
      .catch(this.handleError);
  }
  
  apply(dataset: Dataset): Promise<Dataset> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    return this.http
    .post(acp + '/prism/apply-dataset', JSON.stringify({dataset:dataset}), { headers: headers })
    .toPromise()
    .then(response => {
      return response.json() as Dataset;
    })          
    .catch(this.handleError);
  }
  
  remove(dataset: Dataset): Promise<Response> {
    let headers = new Headers({
      'Content-Type': 'application/json'
    });	  
	  
    return this.http
      .post(acp + '/prism/remove', JSON.stringify({id:dataset.id}), { headers: headers })
      .toPromise()
      .catch(this.handleError);
  }
}
