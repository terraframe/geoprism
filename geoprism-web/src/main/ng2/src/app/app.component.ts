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

import { Component } from '@angular/core';
import { DHIS2Service } from './service/dhis2.service';

declare var acp: any;

@Component({
  
  selector: 'my-app',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  context: string;
  public isDhis2Enabled: boolean;

  constructor(private dhis2Service: DHIS2Service) {
    this.context = acp as string;
    
    this.isDhis2Enabled = false;
    this.dhis2Service.isEnabled().then(isEnabled => {
      this.isDhis2Enabled = isEnabled;
      console.log("setting isDhis2Enabled = " + this.isDhis2Enabled);
    })
  }
}
