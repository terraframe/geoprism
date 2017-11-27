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

import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { Icon } from '../model/icon';
import { IconService } from '../service/icon.service';

declare let acp: string;

@Component({
  
  selector: 'icons',
  templateUrl: './icons.component.html',
  styleUrls: []
})
export class IconsComponent implements OnInit {
  public icons: Icon[];
  context: string;

  constructor(
    private router: Router,
    private iconService: IconService) {
    this.context = acp as string;	  
  }

  ngOnInit(): void {
    this.getIcons();    
  }
    
  getIcons() : void {
    this.iconService
      .getIcons()
      .then(icons => {
        this.icons = icons        
      })
  }
  
  remove(icon: Icon) : void {
    this.iconService
      .remove(icon.id)
      .then((response: any) => {
        this.icons = this.icons.filter(h => h.id !== icon.id);    
      });
  }
  
  edit(icon: Icon) : void {
    this.router.navigate(['/data/icon', icon.id]);
  }  
  
  add() : void {
    this.router.navigate(['/data/icon', -1]);
  }  
}
