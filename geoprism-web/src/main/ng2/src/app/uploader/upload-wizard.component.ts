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

import { Component, EventEmitter, Output, OnDestroy } from '@angular/core';

import { Observable } from 'rxjs/Observable';
import { Subscription }   from 'rxjs/Subscription';

import * as _ from 'lodash';

import { Dataset } from '../model/dataset';
import { UploadInformation, Step, Sheet, Snapshot, Page, Locations, Problems, DatasetResponse } from './uploader-model';

import { EventService, IdService } from '../core/service/core.service';
import { LocalizationService } from '../core/service/localization.service';
import { ProgressService } from '../core/progress-bar/progress.service';

import { UploadService } from '../service/upload.service';
import { NavigationService } from './navigation.service';

@Component({
  
  selector: 'upload-wizard',
  templateUrl: './upload-wizard.component.html',
  styleUrls: []
})
export class UploadWizardComponent implements OnDestroy {

  @Output() onSuccess = new EventEmitter<any>();
  
  steps : Step[];
  
  info: UploadInformation;
  sheet: Sheet;
  page: Page;
  problems: Problems;
  
  pageDirection: string;
  currentStep: number;
  subscription: Subscription;
  
  constructor(
    private localizationService: LocalizationService,
    private uploadService: UploadService,
    private navigationService: NavigationService,
    private progressService: ProgressService,
    private idService: IdService) {
 
    this.subscription = navigationService.navigationAnnounced$.subscribe(
      direction => {
        if(direction === 'next') {
          this.next(null, null);
        }         
        else if(direction === 'prev') {
          this.prev();
        }         
        else if(direction === 'cancel') {
          this.cancel();
        } 
        else if(direction === 'ready') {
          this.persist();
        } 
    });
  }
  
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }  
  
  initialize(info: string): void {
    this.info = JSON.parse(info) as UploadInformation;
    this.sheet = this.info.information.sheets[0];
    	  
	if(this.sheet.attributes == null) {
	  this.sheet.attributes = new Locations();
	  this.sheet.attributes.ids = [];
	  this.sheet.attributes.values = {};
	}
    
	if(this.sheet.coordinates == null) {
	  this.sheet.coordinates = [];
	}    
	
    if(this.info.information.locationExclusions == null){
      this.info.information.locationExclusions = [];    	
    }
    
    if(this.sheet.matches.length > 0) {
      this.page = new Page('MATCH-INITIAL', null);
    }
    else {
      this.page = new Page('BEGINNING-INFO', null);    
    }
                  
    this.initializeAttributes();
    
    this.refreshSteps();
  }
  
  initializeAttributes(): void {
    let fields = this.sheet.fields;
    
    let lats = [
      this.localizationService.localize("dataUploader", "attributeLatAbbreviation").toLowerCase(),
      this.localizationService.localize("dataUploader", "attributeLatitudeName").toLowerCase()      
    ];
    
    let longs = [
      this.localizationService.localize("dataUploader", "attributeLngAbbreviation").toLowerCase(),
      this.localizationService.localize("dataUploader", "attributeLongAbbreviation").toLowerCase(),
      this.localizationService.localize("dataUploader", "attributeLongitudeName").toLowerCase()
    ];
    
    for(let i=0; i<fields.length; i++){
      let field = fields[i];
        
      if(field.columnType === "NUMBER"){
        let label = field.label.toLowerCase();
        
        for(let j = 0; j < lats.length; j++) {
          if(label.includes(lats[j]) ){
            field.type = 'LATITUDE'; 
          }
        }
        
        for(let j = 0; j < longs.length; j++) {
          if(label.includes(longs[j]) ){
            field.type = 'LONGITUDE'; 
          }
        }
      }
    }
  }
  
  refreshSteps(): void {
    this.steps = new Array<Step>();
    this.steps.push(new Step("1", "INITIAL"));
    this.steps.push(new Step("2", "FIELDS"));
    this.steps.push(new Step("3", "SUMMARY"));
          
    let locationStep = new Step("4", "LOCATION");
    let coordinateStep = new Step("5", "COORDINATE");
    let geoProblemResStep = new Step("6", "GEO-VALIDATION"); 
    let categoryProblemResStep = new Step("7", "CATEGORY-VALIDATION");
    
    let hasLocationField = this.hasLocationField();
    let hasCoordinateField = this.hasCoordinateField();
    let hasCategoryField = this.hasCategoryField();
            
    if(hasLocationField && hasCoordinateField && hasCategoryField){
      this.steps.splice(2, 0, locationStep, coordinateStep);
      this.steps.splice(5, 0, geoProblemResStep);
      this.steps.splice(6, 0, categoryProblemResStep);
    }
    else if(!hasLocationField && hasCoordinateField && hasCategoryField){
      this.steps.splice(2, 0, coordinateStep);
      this.steps.splice(4, 0, geoProblemResStep);
      this.steps.splice(5, 0, categoryProblemResStep);
    }
    else if(hasLocationField && !hasCoordinateField && hasCategoryField){
      this.steps.splice(2, 0, locationStep);
      this.steps.splice(4, 0, geoProblemResStep);
      this.steps.splice(5, 0, categoryProblemResStep);
    }
    else if(hasLocationField && hasCoordinateField && !hasCategoryField){
      this.steps.splice(2, 0, locationStep, coordinateStep);
      this.steps.splice(5, 0, geoProblemResStep);
    }
    else if(hasLocationField){
      this.steps.splice(2, 0, locationStep);
      this.steps.splice(4, 0, geoProblemResStep);
    }
    else if(hasCoordinateField){
      this.steps.splice(2, 0, coordinateStep);
      this.steps.splice(4, 0, geoProblemResStep);
    }
    else if(hasCategoryField){
      this.steps.splice(3, 0, categoryProblemResStep);
    }
  }
  
  hasLocationField() : boolean {
    for(let i = 0; i < this.sheet.fields.length; i++) {     
      let field = this.sheet.fields[i];
          
      if(field.type == 'LOCATION') {
        return true;
      }
    }        
      
    return false;
  }
  
  hasCoordinateField() : boolean {
    for(let i = 0; i < this.sheet.fields.length; i++) {     
      let field = this.sheet.fields[i];
              
      if(field.type == 'LONGITUDE' || field.type == 'LATITUDE' ) {
        return true;
      }
    }        
          
    return false;      
  }
  
  hasCategoryField() : boolean {
    for(let i = 0; i < this.sheet.fields.length; i++) {     
      let field = this.sheet.fields[i];
          
      if(field.type == 'CATEGORY') {
        return true;
      }
    }        
      
    return false;
  }
  
  incrementStep(targetPage: string): void {
    if(targetPage === 'MATCH-INITIAL') {
      this.currentStep = -1;
    }
    else if(targetPage == 'MATCH') {
      this.currentStep = -1;
    }
    else if(targetPage == 'BEGINNING-INFO') {
      this.currentStep = -1;
    }
    else if(targetPage == 'INITIAL') {
      this.currentStep = 1;        
    }
    else if(targetPage === 'FIELDS') {
      this.currentStep = 2; 
    }
    else if(targetPage === 'LOCATION') {
      this.currentStep = 3;
    }
    else if(targetPage === 'COORDINATE') {
      if(this.hasLocationField()) {
        this.currentStep = 4;
      }
      else{
        this.currentStep = 3;
      }
    }
    else if(targetPage === 'GEO-VALIDATION') {
      if(this.hasLocationField() && this.hasCoordinateField()) {
        this.currentStep = 5;
      }
      else if (this.hasLocationField()){
        this.currentStep = 4;
      }
      else if (this.hasCoordinateField()) {
        this.currentStep = 4;
      }
      else {
        this.currentStep = 3;
      }
    }
  }
  
  
  /**
   * @param targetPage <optional> 
   * @param sourcePage <optional> 
   */
  next(targetPage: string, sourcePage: string) :void {
    this.pageDirection = "NEXT";
      
    if(targetPage && sourcePage){
      this.page.name = targetPage
      
      this.page.snapshot = _.cloneDeep(this.sheet) as Sheet;
      
      let page = new Page(targetPage, this.page);
      page.hasNext = this.hasNextPage(targetPage);
      page.isReady = this.isReady(targetPage);
      
      this.page = page;
    }
    else{
      this.page.snapshot = _.cloneDeep(this.sheet) as Sheet;
    	
        // Linear logic
      if(this.page.name == 'MATCH-INITIAL') {
        let page = new Page('MATCH', this.page);
        page.hasNext = this.hasNextPage('MATCH');
        page.isReady = this.isReady('MATCH');
        
        this.page = page;
      }
      else if(this.page.name == 'MATCH') {
        let page = new Page('BEGINNING-INFO', this.page);
        page.hasNext = this.hasNextPage('BEGINNING-INFO');
        page.isReady = this.isReady('BEGINNING-INFO');
      
        this.page = page;
      }
      else if(this.page.name == 'BEGINNING-INFO') {
        let page = new Page('INITIAL', this.page);
        page.hasNext = this.hasNextPage('INITIAL');
        page.isReady = this.isReady('INITIAL');
        page.confirm = true;
      
        this.page = page;
        
        this.incrementStep(this.page.name);
      }
      else if(this.page.name === 'INITIAL') {
        let page = new Page('FIELDS', this.page);
        page.hasNext = this.hasNextPage('FIELDS');
        page.isReady = this.isReady('FIELDS');
        page.layout = 'wide-holder';
        page.confirm = true;
      
        this.page = page;
      }
      else if(this.page.name === 'FIELDS') {
        if(this.hasLocationField()) {
          // Go to location attribute page
          let page = new Page('LOCATION', this.page);
          page.hasNext = this.hasNextPage('LOCATION');
          page.isReady = this.isReady('LOCATION');
          page.confirm = true;
      
          this.page = page;
        }
        else if (this.hasCoordinateField()) {
          // Go to location attribute page
          let page = new Page('COORDINATE', this.page);
          page.hasNext = this.hasNextPage('COORDINATE');
          page.isReady = this.isReady('COORDINATE');
          page.confirm = true;

          this.page = page;          
        }
        else {
          // Go to summary page
          let page = new Page('SUMMARY', this.page);
          page.hasNext = this.hasNextPage('SUMMARY');
          page.isReady = this.isReady('SUMMARY');
      
          this.page = page;          
        }
          
        this.incrementStep(this.page.name);          
      }
      else if(this.page.name === 'LOCATION') {
        if (this.hasCoordinateField()) {
          // Go to coordinate page
          let page = new Page('COORDINATE', this.page);
          page.hasNext = this.hasNextPage('COORDINATE');
          page.isReady = this.isReady('COORDINATE');
          page.confirm = true;
      
          this.page = page;          
        }
        else {
          // Go to summary page
          let page = new Page('SUMMARY', this.page);
          page.hasNext = this.hasNextPage('SUMMARY');
          page.isReady = this.isReady('SUMMARY');
      
          this.page = page;          
        }
      }
      else if(this.page.name === 'COORDINATE') {
        // Go to summary page
        let page = new Page('SUMMARY', this.page);
        page.hasNext = this.hasNextPage('SUMMARY');
        page.isReady = this.isReady('SUMMARY');
      
        this.page = page;          

        this.incrementStep(this.page.name);
      }
      else if(this.page.name === 'GEO-VALIDATION') {
        // Go to summary page
        let page = new Page('CATEGORY-VALIDATION', this.page);
        page.hasNext = this.hasNextPage('CATEGORY-VALIDATION');
        page.isReady = this.isReady('CATEGORY-VALIDATION');
        page.layout = 'wide-holder';

        this.page = page;
        
        this.incrementStep(this.page.name);
      }
    }
  }
  
  prev(): void {
    this.pageDirection = "PREVIOUS";
	  
    if(this.page.prev != null) { 
      this.page = this.page.prev;
      this.sheet = this.page.snapshot;
      this.page.snapshot = null;
    }      
      
    if(this.page.name === "SUMMARY" || this.page.name === 'CATEGORY-VALIDATION'){
      let stepCt = 4;
      
      if (!this.hasCoordinateField()) {
        stepCt = stepCt - 1;
      }
        
      if(!this.hasLocationField()) {
        stepCt = stepCt - 1;
      }
        
      if(this.page.name === 'CATEGORY-VALIDATION') {
        stepCt = stepCt - 1;
      }
        
      this.currentStep = stepCt;
    }
    else if(this.page.name === "COORDINATE"){
      let stepCt = 3;
      if(!this.hasLocationField()) {
        stepCt = stepCt - 1;
      }
        
      this.currentStep = stepCt;
    }
    else if(this.page.name === "LOCATION"){
      this.currentStep = 2;
    }
    else if(this.page.name === "FIELDS"){
      this.currentStep = 1;
    }
    else if(this.page.name === "INITIAL"){
      this.currentStep = 0;
    }      
  }
    
  onSubmit(): void {
    this.onSuccess.emit();
    this.clear();
  }
    
  cancel(): void {  
    this.uploadService.cancelImport(this.info.information)
      .then(response => {
        this.clear();
      });    
  }
  
  clear(): void {
    this.steps = null;
    this.info = null;
    this.sheet = null;
    this.page = null;
  
    this.pageDirection = null;
    this.currentStep = null;
  }
  
    
  persist(): void {
    this.info.information.sheets[0] = _.cloneDeep(this.sheet) as Sheet;
    
    let uploadId = this.idService.generateId();
    
    this.info.information.uploadId = uploadId;
    
    /*
     * Setup progress observable
     */
    let subscription = Observable.interval(1000)
      .subscribe(() => {
        this.uploadService.progress(uploadId).then(progress => {
          this.progressService.progress(progress);
        });
      });
    
    this.progressService.start();    
	  
    this.uploadService.importData(this.info.information)
      .finally(()=>{    	  
        subscription.unsubscribe();
        
        this.progressService.complete();            
      })
      .toPromise()      
      .then((response: any) => {
        let result = response.json() as DatasetResponse;        
    	  
        if(result.success) {          
          this.clear();
          
          this.onSuccess.emit({datasets:result.datasets, summary:result.summary, finished : true});          
        }
        else {          
            
          if(this.hasLocationField() && this.hasCoordinateField()) {
            this.currentStep = 5;
          }
          else if(this.hasLocationField() || this.hasCoordinateField()) {
            this.currentStep = 4;
          }
          else{
            this.currentStep = 3;
          }
            
          this.problems = result.problems;
          this.info.information.sheets = result.sheets;
          
          result.sheets[0].format = this.sheet.format
          this.sheet = result.sheets[0];
          
          if( !result.problems.locations || result.problems.locations.length > 0) {
        	
            let page = new Page('GEO-VALIDATION', null);
            page.hasNext = this.hasNextPage('GEO-VALIDATION');
            page.isReady = this.isReady('GEO-VALIDATION');
            page.layout = 'wide-holder';

            this.page = page;
          }
          else {
            let page = new Page('CATEGORY-VALIDATION', null);
            page.hasNext = false;
            page.isReady = true;
            page.layout = 'wide-holder';

            this.page = page;        	  
          }
                                 
          this.onSuccess.emit({datasets:result.datasets, finished : false});          
        }         
      });	  
  }
  
  isReady(name: string) : boolean {      
    return (name === 'SUMMARY' || name === 'CATEGORY-VALIDATION' || (name === 'GEO-VALIDATION' && this.problems.categories !== null && this.problems.categories.length === 0));
  }
      
  hasNextPage(name: string): boolean {
    if(name == 'GEO-VALIDATION') {
      return (this.problems.categories !== null && this.problems.categories.length > 0);
    }
      
    return (name !== 'MATCH-INITIAL' && name !== 'SUMMARY' && name !== 'MATCH' && name !== 'CATEGORY-VALIDATION');
  }  
  
  onNextPage(data: any) : void {
    this.next(data.targetPage, data.sourcePage);
  }
  
  onSelectSheet(sheet: Sheet): void {

    this.page.snapshot = _.cloneDeep(this.sheet) as Sheet;
	  
    // Go to summary page
    let page = new Page('SUMMARY', this.page);
    page.hasNext = this.hasNextPage('SUMMARY');
    page.isReady = this.isReady('SUMMARY');
    
    this.page = page;          
    this.sheet = sheet;
  }
  
  showStep(): boolean {
	
    let names = ['MATCH-INITIAL', 'MATCH'];
	
    return this.page && (names.indexOf(this.page.name) === -1) && !this.sheet.exists;
  }
}
