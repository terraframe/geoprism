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

import { Component, Input, Output, EventEmitter} from '@angular/core';
import { FormGroup} from '@angular/forms';

import { Page } from './uploader-model';
import { NavigationService } from './navigation.service';

@Component({
  moduleId: module.id,
  selector: 'paging',
  templateUrl: 'paging.component.html',
  styleUrls: []
})
export class PagingComponent {
  @Input() form: FormGroup;
  @Input() page: Page;
  @Input() global: boolean = true;
  
  constructor(private service: NavigationService) { } 
  
  next(): void {
    this.service.navigate('next');
  }
  
  prev(): void {
    this.service.navigate('prev');
  }
  
  cancel(): void {
    this.service.navigate('cancel');
  }
  
  ready(): void {
    this.service.navigate('ready');
  }
}
