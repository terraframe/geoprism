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

import { Component, Input} from '@angular/core';

import { Sheet, Options, Field, Page } from './uploader-model';

import { RemoteValidator } from '../core/async-validator.directive';
import { DatasetService } from '../service/dataset.service';

@Component({
  moduleId: module.id,
  selector: 'name-page',
  templateUrl: 'name-page.component.html',
  styleUrls: []
})
export class NamePageComponent implements RemoteValidator {
  @Input() options: Options;
  @Input() sheet: Sheet;
  @Input() page: Page;  

  constructor(private service: DatasetService) { }
 
  validate(value:string): Promise<{[key : string] : any}> {
    return this.service.validateDatasetName(value, '')
      .then((response:any) => {
        return null;
      })
      .catch((error:any) => {
        return {uniqueName: false};
      });            
  }  
}
