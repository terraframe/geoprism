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

import { Component, OnInit, Input, Output, EventEmitter, AfterViewChecked} from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS } from '@angular/forms';

import { LocalValidator } from '../core/function-validator.directive';
import { RemoteValidator } from '../core/async-validator.directive';
import { UploadInformation, Sheet, Page, Field, Universal} from './uploader-model';
import { CategoryService } from '../service/category.service';

@Component({
  
  selector: 'attributes-page',
  templateUrl: './attributes-page.component.html',
  styleUrls: []
})
export class AttributesPageComponent implements OnInit, AfterViewChecked, LocalValidator, RemoteValidator {

  @Input() info: UploadInformation;
  @Input() sheet: Sheet;
  @Input() page: Page;
  
  @Output() onFieldChange = new EventEmitter();
  
  longitudeFields = {};
  latitudeFields = {};
  textFields = {};
  universals: Universal[];
  loaded: boolean;
  
  coordinateMismatch: boolean;
  coordinateText: boolean;

  constructor(private categoryService: CategoryService) {
    this.loaded = false;
    this.coordinateMismatch = false;
    this.coordinateText = false;
  }  

  ngOnInit(): void {
    for(let i = 0; i < this.sheet.fields.length; i++) {
      let field = this.sheet.fields[i];
      
      if(field.categoryLabel == null) {
        field.categoryLabel = field.label;
      }
      
      if (field.root == null && this.sheet.format == 1)
      {
        field.root = this.info.classifiers[0].value;
      }
      else if (field.root == null) {
        field.root = '';
      }
      
      this.accept(field);
    }
    
    // Initialize the universal options
    if(this.info.options != null) {
      let countries = this.info.options.countries;
              
      for(let i = 0; i < countries.length; i++) {
        let country = countries[i];
                 
        if(country.value == this.sheet.country) {
          this.universals = country.options;
        }
      }
    }
  }
  
  public ngAfterViewChecked(): void {
    this.loaded = true;	  
  }    
  
  accept(field: Field): void {
    if(field.type === "LATITUDE") {
      this.latitudeFields[field.name] = field;
    }
    else {
      delete this.latitudeFields[field.name];      
    }
      
    if(field.type === "LONGITUDE") {
      this.longitudeFields[field.name] = field;      
    }
    else {
      delete this.longitudeFields[field.name];
    }
      
    if(field.type === "TEXT") {
      this.textFields[field.name] = field;      
    }
    else {
      delete this.textFields[field.name];
    }
      
    this.coordinateMismatch = (Object.keys(this.latitudeFields).length != Object.keys(this.longitudeFields).length);
    
    if(Object.keys(this.latitudeFields).length > 0 || Object.keys(this.longitudeFields).length > 0) {
      this.coordinateText = (Object.keys(this.textFields).length == 0);        
    }
    else {
      this.coordinateText = false;        
    } 
    
    if(this.loaded) {
      this.onFieldChange.emit(field);
    }
  }
 
  localValidate(value: string, config: string): {[key : string] : any} {
    if(config == 'label') {
      return this.validateLabel(value);	
    }
    
    return null;
  }
  
  validateLabel(label: string): {[key : string] : any} {
    if(this.sheet != null) {
      let count = 0;
          
      for(let i = 0; i < this.sheet.fields.length; i++) {
        let field = this.sheet.fields[i];
            
        if(field.label == label) {
          count++;
        }            
      }
          
      if(count > 1) {
        return {unique:false};
      }
    }  
        
    return null;
  }

  validate(value:string, config:string): Promise<{[key : string] : any}> {
    if(config == 'category') {
      return this.validateCategory(value);	
    }
    
    return null;	  
  }
  
  validateCategory(label: string): Promise<{[key : string] : any}> {
    return this.categoryService.validate(label, '')
      .then((response:any) => {
        return null;
      })
     .catch((error:any) => {
        return {uniqueName: false};
     });	  	  
  }  
}
