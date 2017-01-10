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

import { Component, EventEmitter, OnInit, Output, Inject} from '@angular/core';
import { ActivatedRoute, Params, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Location } from '@angular/common';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { Category, BasicCategory } from '../model/category';
import { Synonym } from '../model/synonym';

import { EventService } from '../service/core.service';
import { LocalizationService } from '../service/localization.service';
import { CategoryService } from '../service/category.service';


export class OptionResolver implements Resolve<Category> {
  constructor(@Inject(CategoryService) private categoryService: CategoryService, @Inject(EventService) private eventService: EventService) {}
  
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):Promise<Category> {
    return this.categoryService.edit(route.params['parentId'], route.params['id'])
      .catch((error:any) => {
        this.eventService.onError(error); 
    	  
        return Promise.reject(error);
      });
  }
}

class Action {
  synonym: string;
  restore: string[];   

  constructor() {
    this.synonym = '';
    this.restore = [];
  }
}

@Component({
  moduleId: module.id,
  selector: 'option-detail',
  templateUrl: 'option-detail.component.jsp',
  styleUrls: []
})
export class OptionDetailComponent implements OnInit {
  @Output() close = new EventEmitter();
  
  category: Category;
  action: Action;
  
  constructor(
    private categoryService: CategoryService,
    private route: ActivatedRoute,
    private location: Location,
    private localizationService: LocalizationService) {
  }

  ngOnInit(): void {
    this.category = this.route.snapshot.data['category'];
    
    this.action = new Action();
  }
  
  onSubmit(): void {
    let config = {
      option : this.category,
      synonym : this.action.synonym,
      restore : this.action.restore
    }
      
    this.categoryService.apply(config)
      .then(response => {
        this.goBack(this.category);
      });
  }
  
  cancel(): void {
    this.categoryService.unlock(this.category)
      .then(response => {
        this.goBack(this.category);
      })
  } 
  
  goBack(category : Category): void {
    this.close.emit(category);
    
    this.location.back();
  }
  
  restore(synonym: Synonym): void {
	  
	let message = this.localizationService.localize("category.management", "restoreConfirm");
    message = message.replace('{0}', this.category.label);

    if(confirm(message)) {
      this.action.restore.push(synonym.id);
        	
      this.category.synonyms = this.category.synonyms.filter(h => h !== synonym);            	        	
    }	  
  } 
  
}
