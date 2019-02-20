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
import { Response} from '@angular/http';
import 'rxjs/add/operator/toPromise';

import { Dataset } from '../model/dataset';

@Injectable()
export class MockDatasetService {

  getMockDataset(): Dataset {
    let dataset = new Dataset();
    dataset.oid = 'test-oid';
    dataset.label = 'Test Label';
    dataset.description = 'Test Description';
    dataset.source = 'Test Source';
    
    return dataset;
  }
  
  getDatasets(): Promise<Dataset[]> {
    return new Promise((resolve, reject) => {     
      let datasets:Dataset[] = [];
      datasets.push(this.getMockDataset());
      
      resolve(datasets);  
    });  
  }
  
  edit(oid : string): Promise<Dataset> {
    return new Promise((resolve, reject) => {     
      resolve(this.getMockDataset());  
    });  
  }
  
  unlock(dataset: Dataset): Promise<Response> {
    return new Promise((resolve, reject) => {     
      resolve(null);  
    });  
  }
  
  apply(dataset: Dataset): Promise<Dataset> {
    return new Promise((resolve, reject) => {     
      resolve(this.getMockDataset());  
    });  
  }
  
  remove(dataset: Dataset): Promise<Response> {
    return new Promise((resolve, reject) => {     
      resolve(null);  
    });  
  }  
      
  validateDatasetName(name: string, oid: string): Promise<Response> {
    return new Promise((resolve, reject) => {     
      resolve(null);  
    });  
  }  
  
  xport(oid : string): Promise<Dataset> {
    return new Promise((resolve, reject) => {     
      resolve(this.getMockDataset());  
    });  
  }
}
