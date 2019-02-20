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

import { Category, BasicCategory } from '../model/category';

import { CategoryService } from '../service/category.service';

class Instance {
  active: boolean;
  label: string;   
}

@Component({
  
  selector: 'categories',
  templateUrl: './categories.component.html',
  styleUrls: []
})
export class CategoriesComponent implements OnInit {
  categories: BasicCategory[];
  instance : Instance = new Instance();  

  constructor(
    private router: Router,
    private categoryService: CategoryService) { }

  ngOnInit(): void {
    this.getCategories();    
  }
  
  getCategories() : void {
    this.categoryService
      .getAll()
      .then(categories => {
        this.categories = categories
      })
  }
  
  remove(category: BasicCategory) : void {    
    this.categoryService
      .remove(category.oid)
      .then(response => {
        this.categories = this.categories.filter(h => h !== category);    
      });
  }
  
  edit(category: BasicCategory) : void {
    this.router.navigate(['/data/category', category.oid]);
  }
  
  newInstance() : void {
    this.instance.active = true;
  }
  
  create() : void {
    this.categoryService.create(this.instance.label, '', true)
      .then((category:BasicCategory) => {
        this.categories.push(category);
        
        this.instance.active = false;
        this.instance.label = '';
      });
  }
  
  cancel(): void {
    this.instance.active = false;
    this.instance.label = '';
  }  
}
