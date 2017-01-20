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

import { Component, OnInit, Input, Output, EventEmitter, Directive} from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS } from '@angular/forms';

import { LocalValidator } from '../core/function-validator.directive';
import { UploadInformation, Sheet, Page, Field, Universal, CoordinateAttribute, Coordinates} from './uploader-model';
import { LocalizationService } from '../service/localization.service';

@Component({
  moduleId: module.id,
  selector: 'coordinate-page',
  templateUrl: 'coordinate-page.component.jsp',
  styleUrls: []
})
export class CoordinatePageComponent implements OnInit, LocalValidator {

  @Input() info: UploadInformation;
  @Input() sheet: Sheet;
  @Input() page: Page;
  
  locations: {[key : string] : string}[];
  
  longitudes: Field[];
  featureLabels: Field[];
  featureIds: Field[];
  
  universals: Universal[];
  labels: {[key : string] : string};  
  
  constructor(private localizationService: LocalizationService) {
    this.longitudes = [];
    this.featureLabels = [];
    this.locations = [];
    this.featureIds = [];
    this.labels = {};	      
  }  

  ngOnInit(): void {
	if(this.sheet.coordinates == null) {
	  this.sheet.coordinates = [];
	}    
    
    let countries = this.info.options.countries;
      
    for(let i = 0; i < countries.length; i++) {
      let country = countries[i];
      this.universals = country.options;
               
      if(country.value == this.sheet.country) {
          
        for(let j = 0; j < country.options.length; j++) {
          let universal = country.options[j];
            
          this.labels[universal.value] = universal.label;
        }
      }
    }      
      
    for(let i = 0; i < this.sheet.fields.length; i++) {
      let field = this.sheet.fields[i];
        
      if(field.type == 'LATITUDE') {
          
        if(!this.hasCoordinateField(field)) {
          let coordinate = new CoordinateAttribute();
          coordinate.label = "";
          coordinate.latitude = field.label;
          coordinate.longitude = this.getSuggestedLongitude(field);
          coordinate.featureLabel = "";
          coordinate.location = "";
          coordinate.featureId = "";
          coordinate.id = field.label
            
          this.sheet.coordinates.push(coordinate);
        }
      }
      else if(field.type == 'LONGITUDE') {
        this.longitudes.push(field);          
      }
      else if(field.type == 'TEXT') {
        this.featureLabels.push(field);          
      }
      else if(this.isBasic(field)) {
        this.featureIds.push(field);          
      }
    }
      
    /*
     * If there is only 1 longitude field then set that value
     * automatically and don't give the user a drop-down that
     * they need to select from
     */
    if(this.longitudes.length == 1) {
      for(let i = 0; i < this.sheet.coordinates.length; i++) {
        let coordinate = this.sheet.coordinates[i];          
          
        coordinate.longitude = this.longitudes[0].label;
      }
    }
      
    if(this.sheet.attributes != null) {
      for(let i = 0; i < this.sheet.attributes.ids.length; i++) {
        let id = this.sheet.attributes.ids[i];
        let attribute = this.sheet.attributes.values[id];          
        
        this.locations.push({
          label : attribute.label,
          universal : attribute.universal
        });
      }
    }
  }
    
  getSuggestedLongitude(targetField: Field): string {
    let fields = this.sheet.fields;
    let trackingPosition = null;
    let mostLikelyLongitudeField = null;
    let label = targetField.label.toLowerCase();
    
    let labels = [
      this.localizationService.localize("dataUploader", "attributeLatAbbreviation").toLowerCase(),
      this.localizationService.localize("dataUploader", "attributeLatitudeName").toLowerCase(),     
      this.localizationService.localize("dataUploader", "attributeLngAbbreviation").toLowerCase(),
      this.localizationService.localize("dataUploader", "attributeLongAbbreviation").toLowerCase(),
      this.localizationService.localize("dataUploader", "attributeLongitudeName").toLowerCase()   
    ];
    
    for(let i=0; i<fields.length; i++){
      let field = fields[i];
      if(field.type === "LATITUDE" && field.name === targetField.label){
        trackingPosition = field.fieldPosition;
      }
      else if(field.type === "LONGITUDE"){
        // if fields are located next to each other in the source data (spreadsheet)
        if(field.fieldPosition === trackingPosition + 1 || field.fieldPosition === trackingPosition - 1){
          return field.name;
        }
        else {        	
          for(let j = 0; j < labels.length; j++) {
            if(label.includes(labels[j]) ){
              return field.name;
            }
          }
        }
      }
    }
    
    return null;
  }
    
  hasCoordinateField(field: Field): boolean {
    for(let i = 0; i < this.sheet.coordinates.length; i++) {
      let coordinate = this.sheet.coordinates[i];
        
      if(coordinate.id === field.label) {
        return true;
      }
    }
      
    return false;
  }
    
  isBasic(field: Field): boolean {
    return (field.type == 'TEXT' || field.type == 'LONG' || field.type == 'DOUBLE');  
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
        
      if(this.sheet.attributes != null) {
        for(let i = 0; i < this.sheet.attributes.ids.length; i++) {
          let id = this.sheet.attributes.ids[i];
          let attribute = this.sheet.attributes.values[id];          
            
          if(attribute.label == label) {
            count++;
          }            
        }          
      }
        
      for(let i = 0; i < this.sheet.coordinates.length; i++) {
        let coordinate = this.sheet.coordinates[i];          
          
        if(coordinate.label == label) {
          count++;
        }            
      }
        
      if(count > 1) {
        return {unique:false};
      }
    }  
      
    return null;
  }
}
