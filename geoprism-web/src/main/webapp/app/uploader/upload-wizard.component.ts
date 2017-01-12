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

import { Component, EventEmitter, Output } from '@angular/core';

import { Dataset } from '../model/dataset';
import { UploadInformation, Step, Sheet, Snapshot, Page } from './uploader-model';

import { EventService } from '../service/core.service';
import { LocalizationService } from '../service/localization.service';

import { UploadService } from '../service/upload.service';


@Component({
  moduleId: module.id,
  selector: 'upload-wizard',
  templateUrl: 'upload-wizard.component.jsp',
  styleUrls: []
})
export class UploadWizardComponent {

  @Output() public onSuccess = new EventEmitter<Dataset>();
  
  public steps : Step[];
  
  public info: UploadInformation;
  public sheet: Sheet;
  public page: Page;
  
  public pageDirection: string;
  public currentStep: number;
  
  constructor(
    private localizationService: LocalizationService,
    private uploadService: UploadService) { }
  
  initialize(info: string): void {
    this.info = JSON.parse(info) as UploadInformation;
    this.sheet = this.info.information.sheets[0];
    
    if(this.sheet.matches.length > 0) {
      this.page = new Page('MATCH-INITIAL');
    }
    else {
      this.page = new Page('BEGINNING-INFO');    
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
        
        for(let j = 0; j < lats.length; j++) {
          if(label.includes(lats[j]) ){
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
      this.page.current = targetPage
          
      let snapshot = new Snapshot(sourcePage, Object.assign(new Sheet(), this.sheet) as Sheet);
        
      this.page.snapshots.push(snapshot);
    }
    else{
        // Linear logic
      if(this.page.current == 'MATCH-INITIAL') {
        this.page.current = 'MATCH'
        
        let snapshot = new Snapshot('MATCH-INITIAL', Object.assign(new Sheet(), this.sheet) as Sheet);
            
        this.page.snapshots.push(snapshot);
      }
      else if(this.page.current == 'MATCH') {
        this.page.current = 'BEGINNING-INFO';      
          
        let snapshot = new Snapshot('MATCH', Object.assign(new Sheet(), this.sheet) as Sheet);
        
        this.page.snapshots.push(snapshot);
      }
      else if(this.page.current == 'BEGINNING-INFO') {
        this.page.current = 'INITIAL';
        
        this.incrementStep(this.page.current);
      }
      else if(this.page.current === 'INITIAL') {
        // Go to fields page  
        this.page.current = 'FIELDS';      
          
        let snapshot = new Snapshot('INITIAL', Object.assign(new Sheet(), this.sheet) as Sheet);
        
        this.page.snapshots.push(snapshot);
      }
      else if(this.page.current === 'FIELDS') {
        if(this.hasLocationField()) {
          // Go to location attribute page
          this.page.current = 'LOCATION';      
        }
        else if (this.hasCoordinateField()) {
          // Go to coordinate page
          this.page.current = 'COORDINATE';   
        }
        else {
          // Go to summary page
          this.page.current = 'SUMMARY'; 
        }
          
        this.incrementStep(this.page.current);
          
        let snapshot = new Snapshot('FIELDS', Object.assign(new Sheet(), this.sheet) as Sheet);
        
        this.page.snapshots.push(snapshot);
      }
      else if(this.page.current === 'LOCATION') {
        if (this.hasCoordinateField()) {
          // Go to coordinate page
          this.page.current = 'COORDINATE'; 
        }
        else {
          // Go to summary page
          this.page.current = 'SUMMARY';    
        }
          
        this.incrementStep(this.page.current);
        
        let snapshot = new Snapshot('LOCATION', Object.assign(new Sheet(), this.sheet) as Sheet);        
          
        this.page.snapshots.push(snapshot);
      }
      else if(this.page.current === 'COORDINATE') {
        // Go to summary page
        this.page.current = 'SUMMARY';  
          
        this.incrementStep(this.page.current);
          
        let snapshot = new Snapshot('COORDINATE', Object.assign(new Sheet(), this.sheet) as Sheet);        
        
        this.page.snapshots.push(snapshot);
      }
      else if(this.page.current === 'GEO-VALIDATION') {
          
        this.page.current = 'CATEGORY-VALIDATION';  
          
        this.incrementStep(this.page.current);
          
        let snapshot = new Snapshot('GEO-VALIDATION', Object.assign(new Sheet(), this.sheet) as Sheet);        

        this.page.snapshots.push(snapshot);
      }
    }
  }
  
  prev(): void{
    this.pageDirection = "PREVIOUS";
    
    if(this.page.current === 'MATCH' || this.page.current === "SUMMARY" || this.page.current === "BEGINNING-INFO" || this.page.current === "CATEGORY-VALIDATION") {
      this.handlePrev();        
    }
    else if(confirm(this.localizationService.localize("dataUploader", "prevDialogContent"))) {
      this.handlePrev();
    }
  }  
  
  handlePrev(): void {
    if(this.page.snapshots.length > 0) { 
      if(this.page.current === 'INITIAL') {
        this.page.current = 'BEGINNING-INFO'; 
      }
      else{
        let snapshot = this.page.snapshots.pop();
          
        this.page.current = snapshot.page;          
        this.sheet = snapshot.sheet;
      }
    }
      
      
    if(this.page.current === "SUMMARY" || this.page.current === 'CATEGORY-VALIDATION'){
      let stepCt = 4;
      
      if (!this.hasCoordinateField()) {
        stepCt = stepCt - 1;
      }
        
      if(!this.hasLocationField()) {
        stepCt = stepCt - 1;
      }
        
      if(this.page.current === 'CATEGORY-VALIDATION') {
        stepCt = stepCt - 1;
      }
        
      this.currentStep = stepCt;
    }
    else if(this.page.current === "COORDINATE"){
      let stepCt = 3;
      if(!this.hasLocationField()) {
        stepCt = stepCt - 1;
      }
        
      this.currentStep = stepCt;
    }
    else if(this.page.current === "LOCATION"){
      this.currentStep = 2;
    }
    else if(this.page.current === "FIELDS"){
      this.currentStep = 1;
    }
    else if(this.page.current === "INITIAL"){
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
  
  
  isReady() : boolean {
    let current = this.page.current;
      
// TODO   return (current === 'SUMMARY' || current === 'CATEGORY-VALIDATION' || (current === 'GEO-VALIDATION' && this.problems.categories !== null && this.problems.categories.length === 0));
    return (current === 'SUMMARY' || current === 'CATEGORY-VALIDATION');
  }
      
  hasNextPage(): boolean {
    let current = this.page.current;
      
    //
//    if(current == 'GEO-VALIDATION') {
//      return (this.problems.categories !== null && this.problems.categories.length > 0);
//    }
      
    return (current !== 'MATCH-INITIAL' && current !== 'SUMMARY' && current !== 'MATCH' && current !== 'CATEGORY-VALIDATION');
  }  
  
  onNextPage(data: any) : void {
    this.next(data.targetPage, data.sourcePage);
  }
}
