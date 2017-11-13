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

import { Component, EventEmitter, Input, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { BrowserService } from './browser.service';
import { Browser } from './browser';

declare var Mojo:any;
declare var net:any;
declare var com:any;

@Component({
  
  selector: 'browser',
  templateUrl: './browser.component.html',
  styles: []
})
export class BrowserComponent implements OnInit {
  
  constructor(private service:BrowserService) {}

  ngOnInit() {
    this.service.getInstance().then(browser => {
      var db = new net.geoprism.data.browser.DataBrowser({
        types: com.runwaysdk.DTOUtil.convertToType(browser.types.returnValue[0]).getResultSet(),
        editData : browser.edit
      });
      db.render("#databrowser");    	
    });
  }  
}
