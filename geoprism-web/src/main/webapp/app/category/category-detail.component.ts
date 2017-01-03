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

import { Component, EventEmitter, Input, OnInit, Output, Inject } from '@angular/core';
import { ActivatedRoute, Params, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Location } from '@angular/common';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { Category } from '../model/category';
import { CategoryService } from '../service/category.service';

export class CategoryResolver implements Resolve<Category> {
  constructor(@Inject(CategoryService) private categoryService: CategoryService) {}
  
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):Promise<Category> {
    return this.categoryService.get(route.params['id']);
  }
}

class Instance {
  active: boolean;
  label: string; 	
}

@Component({
  moduleId: module.id,
  selector: 'category-detail',
  templateUrl: 'category-detail.component.jsp',
  styleUrls: []
})
export class CategoryDetailComponent implements OnInit {
  @Input() category: Category;
  @Output() close = new EventEmitter();
  
  instance : Instance = new Instance();  
  
  error: any;

  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private location: Location) {
  }

  ngOnInit(): void {
    this.category = this.route.snapshot.data['category'];
    
    this.instance.active = false; 
    this.instance.label = '';
  }
  
  onSubmit(): void {
    this.categoryService.apply(this.category)
      .then(category => {
    	this.goBack(category);
      })
      .catch(error => {
        this.error = error;
      });        
  }
  
  goBack(category : Category): void {
    this.close.emit(category);
    
    this.location.back();
  }
}
