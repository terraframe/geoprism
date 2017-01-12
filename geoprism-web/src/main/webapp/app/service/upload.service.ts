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

import { EventService, BasicService } from './core.service';
import { EventHttpService } from './event-http.service';

import { Sheet, Workbook } from '../uploader/uploader-model';

declare var acp: any;

@Injectable()
export class UploadService extends BasicService {

  constructor(service: EventService, private ehttp: EventHttpService, private http: Http) { super(service); }

  getSavedConfiguration(id: string, sheetName: string): Promise<Sheet> {
	  
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({id:id, sheetName: sheetName});
    
    return this.ehttp
      .post(acp + '/uploader/getSavedConfiguration', data, {headers: headers})
      .toPromise() 
      .then((response: any) => {
        return response.json() as Sheet;
      })           
      .catch(this.handleError);
  }
    
  cancelImport(workbook: Workbook): Promise<Response> {
    
    let headers = new Headers({
      'Content-Type': 'application/json'
    });    
    
    let data = JSON.stringify({configuration : workbook });
    
    return this.ehttp
      .post(acp + '/uploader/cancelImport', data, {headers: headers})
      .toPromise() 
      .catch(this.handleError);
  }
}
