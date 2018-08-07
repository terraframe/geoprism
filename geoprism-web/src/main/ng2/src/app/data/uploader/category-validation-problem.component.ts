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

import { Page, CategoryProblem, Workbook} from './uploader-model';
import { Pair } from '../model/pair';

import { CategoryService } from '../service/category.service';
import { UploadService } from '../service/upload.service';

@Component({
  
  selector: 'category-validation-problem',
  templateUrl: './category-validation-problem.component.html',
  styleUrls: []
})
export class CategoryValidationProblemComponent implements OnInit {

  @Input() problem: CategoryProblem;
  @Input() index: number;
  @Input() workbook: Workbook;
  @Input() options: Pair[];
  @Input() format: number;

  @Output() onProblemChange = new EventEmitter();
  
  show: boolean;
  synonym: string;
  
  constructor(private uploadService: UploadService, private categoryService: CategoryService) {
  }   
  
  ngOnInit(): void {
    this.synonym = '';
    this.show = false;
  }
  
  source = (text: string) => {
    let limit = '20';

    return this.uploadService.getClassifierSuggestions(this.problem.mdAttributeId, text, limit);
  }
  
  setSynonym() {
  
  }
    
  createSynonym(): void {
    if(this.synonym !== ''){      
      this.uploadService.createClassifierSynonym(this.synonym, this.problem.label)
        .then(response => {
          this.problem.resolved = true;
          this.problem.action = {
            name : 'SYNONYM',
            synonymId : response.synonymId,
            label : response.label
          };
          
          this.onProblemChange.emit(this.problem);          
        });
    }
  }
  
  createOption(): void {
    this.categoryService.create(this.problem.label, this.problem.categoryId, false)
      .then(response => {
        this.problem.resolved = true;
        this.problem.action = {
          name : 'OPTION',
          optionId : response.id
        };
        
        this.onProblemChange.emit(this.problem);
      });
  }
    
  ignoreValue(): void {
    this.problem.resolved = true;
      
    this.problem.action = {
      name : 'IGNORE'
    };
      
    let mdAttributeId = this.problem.mdAttributeId;
      
    if(!this.workbook.categoryExclusion){
      this.workbook.categoryExclusion = {};
    }
      
    if(!this.workbook.categoryExclusion[mdAttributeId]) {
      this.workbook.categoryExclusion[mdAttributeId] = [];
    }
      
    this.workbook.categoryExclusion[mdAttributeId].push(this.problem.label);
    
    this.onProblemChange.emit(this.problem);
  }
    
  removeExclusion(): void {
      
    let mdAttributeId = this.problem.mdAttributeId;
    let label = this.problem.label;
      
    if(this.workbook.categoryExclusion && this.workbook.categoryExclusion[mdAttributeId]){          
      this.workbook.categoryExclusion[mdAttributeId] = this.workbook.categoryExclusion[mdAttributeId].filter(h => h !== label);
    }
      
    if(this.workbook.categoryExclusion[mdAttributeId].length === 0) {
      delete this.workbook.categoryExclusion[mdAttributeId];
    }
  }    
    
  undoAction(): void {
      
    if(this.problem.resolved) {
        
      let action = this.problem.action;
        
      if(action.name === 'IGNORE'){
        this.problem.resolved = false;
          
        this.removeExclusion();
        
        this.onProblemChange.emit(this.problem);
      }
      else if(action.name === 'SYNONYM')  {    	
        this.uploadService.deleteClassifierSynonym(action.synonymId)
          .then(response => {
          this.problem.resolved = false;
          this.synonym = '';
          this.problem.action = null;
          
          this.onProblemChange.emit(this.problem);
        });
      }
      else if(action.name === 'OPTION')  {    	
        this.categoryService.remove(action.optionId)
          .then(response => {
            this.problem.resolved = false;
            this.problem.optionId = null;
            this.problem.action = null;
            
            this.onProblemChange.emit(this.problem);
          });
      }
    }
  }
}
