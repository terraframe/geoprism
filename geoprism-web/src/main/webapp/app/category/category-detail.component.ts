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
import { Router, ActivatedRoute, Params, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Location } from '@angular/common';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { Category, BasicCategory } from '../model/category';

import { EventService } from '../service/core.service';
import { LocalizationService } from '../service/localization.service';
import { CategoryService } from '../service/category.service';

export class CategoryResolver implements Resolve<Category> {
  constructor(@Inject(CategoryService) private categoryService: CategoryService, @Inject(EventService) private eventService: EventService) {}
  
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):Promise<Category> {
    return this.categoryService.get(route.params['id'])
      .catch((error:any) => {
        this.eventService.onError(error); 
        
        return Promise.reject(error);
      });
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
  validName: boolean = true;


  constructor(
    private router: Router,      
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private location: Location,
    private localizationService: LocalizationService) {
  }

  ngOnInit(): void {
    this.category = this.route.snapshot.data['category'];
    
    this.instance.active = false; 
    this.instance.label = '';
  }
  
  onSubmit(): void {
    this.categoryService.update(this.category)
      .then(category => {
        this.goBack(category);
      });
  }
  
  goBack(category : Category): void {
    this.close.emit(category);
    
    this.location.back();
  }
  
  newInstance() : void {
    this.instance.active = true;
  }
  
  create() : void {
    this.categoryService.create(this.instance.label, this.category.id, false)
      .then((category:BasicCategory) => {
        this.category.descendants.push(category);
        
        this.instance.active = false;
        this.instance.label = '';
      });
  }
  
  cancel(): void {
    this.instance.active = false;
    this.instance.label = '';
  }
  
  remove(descendant: BasicCategory) {
//    let message = this.localizationService.localize("category.management", "removeCategoryConfirm");
//    message = message.replace('{0}', this.category.label);
//
//    if(confirm(message)) {
      this.categoryService.remove(descendant.id)
       .then((response:any) => {
         this.category.descendants = this.category.descendants.filter(h => h !== descendant);        
       });
//    }
  }
  
  
  edit(descendant: BasicCategory) : void {
    this.router.navigate(['/category-option', this.category.id, descendant.id]);
  }
  
  validateName(name: string) {
    this.categoryService.validate(name, this.category.id)
      .then((response:any) => {
        this.validName = true;
      })
     .catch((error:any) => {
        this.validName = false;       
     });        
  }  
}
