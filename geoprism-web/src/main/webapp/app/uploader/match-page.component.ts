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

import { Component, Input } from '@angular/core';

import { UploadInformation, Step, Sheet, Page } from './uploader-model';

import { UploadService } from '../service/upload.service';


@Component({
  moduleId: module.id,
  selector: 'match-page',
  templateUrl: 'match-page.component.jsp',
  styleUrls: []
})
export class MatchPageComponent {
	
  @Input() sheet: Sheet;

  constructor(private service: UploadService) { }
  
  select(match:any, overwrite:boolean) : void {
   
    this.service.getSavedConfiguration(match.id, this.sheet.name)
      .then(sheet => {
        this.sheet = sheet;
        this.sheet.replaceExisting = overwrite;          
      });      
  }
}
