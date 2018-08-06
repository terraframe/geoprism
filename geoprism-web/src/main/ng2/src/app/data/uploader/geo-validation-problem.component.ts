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

import { Component, Input, Output, EventEmitter, OnInit} from '@angular/core';

import { Page, LocationProblem, Workbook, LocationExclusion, LocationProblemSynonym} from './uploader-model';
import { UploadService } from '../service/upload.service';
import { IdService } from '../../core/service/core.service';

@Component({
  
  selector: 'geo-validation-problem',
  templateUrl: './geo-validation-problem.component.html',
  styleUrls: []
})
export class GeoValidationProblemComponent implements OnInit {

  @Input() problem: LocationProblem;
  @Input() index: number;
  @Input() workbook: Workbook;
  @Input() format: number;
  
  @Output() onProblemChange = new EventEmitter();
  
  show: boolean;
  
  constructor(private uploadService: UploadService, private idService: IdService) {
  }   
  
  ngOnInit(): void {
//	console.log(this.problem);
//    this.problem.synonym = {id :'', geoId:''};
	this.problem.synonym = new LocationProblemSynonym('', '');
    this.show = false;
  }
    
  createSynonym(): void {
    if(this.problem.synonym.id !== ''){    	
      this.uploadService.createGeoEntitySynonym(this.problem.synonym.id, this.problem.label)
        .then(response => {
          this.problem.resolved = true;
          this.problem.action = {
            name : 'SYNONYM',
            synonymId : response.synonymId,
            label : response.label,
            ancestors : response.ancestors            
          };
          
          this.onProblemChange.emit(this.problem);          
        });
    }      
  }
    
  createEntity(): void {
	  
    this.uploadService.createGeoEntity(this.problem.parentId, this.problem.universalId, this.problem.label)
      .then(response => {
        this.problem.resolved = true;
        this.problem.action = {
          name : 'ENTITY',
          entityId : response.entityId
        };
        
        this.onProblemChange.emit(this.problem);        
      });
  }
    
  removeLocationExclusion(exclusionId: string): void {

    if(this.workbook.locationExclusions){         
      this.workbook.locationExclusions = this.workbook.locationExclusions.filter(h => h.id !== exclusionId);
    }
  }
    
  ignoreDataAtLocation(): void {
    let locationLabel = this.problem.label;
    let universal = this.problem.universalId;
    let id = this.idService.generateId();
      
    this.problem.resolved = true;
      
    this.problem.action = {
      name : 'IGNOREATLOCATION',
      label : locationLabel,
      id : id
    };
      
    let exclusion = new LocationExclusion(id, universal, locationLabel, this.problem.parentId);
      
    if(this.workbook.locationExclusions){
      this.workbook.locationExclusions.push(exclusion);
    }
    else{
      this.workbook.locationExclusions = [exclusion];
    }
    
    this.onProblemChange.emit(this.problem);
  }    

  undoAction(): void {
    let locationLabel = this.problem.label;
    let universal = this.problem.universalId;
      
    if(this.problem.resolved) {
      let action = this.problem.action;
    	
      if(action.name === 'ENTITY')  {    	
        this.uploadService.deleteGeoEntity(action.entityId)
          .then(response => {
            this.problem.resolved = false;
            this.problem.synonym = new LocationProblemSynonym('', '');
            this.problem.action = null;
            
            this.onProblemChange.emit(this.problem);
          });
      }
      else if(action.name === 'IGNOREATLOCATION'){
        this.problem.resolved = false;
        this.problem.action = null;
        
        this.removeLocationExclusion(action.id);
        
        this.onProblemChange.emit(this.problem);
      }      
      else if(action.name === 'SYNONYM')  {    	
        this.uploadService.deleteGeoEntitySynonym(action.synonymId)
          .then(response => {
          this.problem.resolved = false;
          this.problem.synonym = new LocationProblemSynonym('', '');
          this.problem.action = null;
          
          this.onProblemChange.emit(this.problem);
        });
      }
        
    }
  }
  
  toggle(): void {
    this.show = !this.show;
  }
  
}
