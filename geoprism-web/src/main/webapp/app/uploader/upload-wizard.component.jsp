<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

    This file is part of Runway SDK(tm).

    Runway SDK(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Runway SDK(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>


<div class='ng-modal' *ngIf="info">
  <div id="uploader-overlay" class='ng-modal-overlay'></div>
  <div id="uploader-div" class='ng-modal-dialog' ng-style='dialogStyle'>
    <div class='ng-modal-dialog-content'>
      <div role="dialog" class="ng-modal-content modal-content">       
        <div class="uploader-step-indicator-container" *ngIf="page.name != 'MATCH-INITIAL' && page.name != 'MATCH' && !updateExistingDataset">
          <ol class="wizard-progress clearfix">
            <li *ngFor="let step of steps; let i = index;" [ngClass]="{'active-step' : page.name === step.page, 'status-li-disabled' : page.name !== step.page}">
              <span *ngIf="step.label == '1'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep1"/></span>
              <span *ngIf="step.label == '2'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep2"/></span>
              <span *ngIf="step.label == '3'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep3"/></span>
              <span *ngIf="step.label == '4'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep4"/></span>
              <span *ngIf="step.label == '5'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep5"/></span>
              <span *ngIf="step.label == '6'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep6"/></span>
              <span *ngIf="step.label == '7'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep7"/></span>
              <span class="visuallyhidden">Step </span><span class="step-num">{{i + 1}}</span>
            </li>
          </ol>
        </div>      
    
      <div class="heading">
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'MATCH-INITIAL'"><gdb:localize key="dataUploader.titleUploadToExistingOrNew"/></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'MATCH'"><gdb:localize key="dataUploader.titleUploadToExistingOrNew"/></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'BEGINNING-INFO'"><gdb:localize key="dataUploader.uploadBeginningMessageTitle"/></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'INITIAL'"><gdb:localize key="dataUploader.titleNameCountry"/></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'FIELDS'"><gdb:localize key="dataUploader.titleAttributeConfiguration"/></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'LOCATION'"><gdb:localize key="dataUploader.titleTextLocationConfiguration"/></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'COORDINATE'"><gdb:localize key="dataUploader.titleCoordinateLocationConfiguration"/></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'SUMMARY'"><gdb:localize key="dataUploader.titleSummary"/></h1>
        <h1 class="ui-dialog-title" *ngIf="page.name == 'GEO-VALIDATION'"><gdb:localize key="dataUploader.titleLocationValidation"/></h1>
        <h1 class="ui-dialog-title" *ngIf="page.name == 'CATEGORY-VALIDATION'"><gdb:localize key="dataUploader.titleCategoryValidation"/></h1>
      </div>
      
      <match-initial-page *ngIf="page.name == 'MATCH-INITIAL'"(onNextPage)="onNextPage($event)" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></match-initial-page> 
      <match-page *ngIf="page.name == 'MATCH'" [sheet]="sheet" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></match-page>  
      <beginning-info-page *ngIf="page.name == 'BEGINNING-INFO'" [page]="page" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></beginning-info-page>            
      <name-page *ngIf="page.name == 'INITIAL'" [page]="page"  [sheet]="sheet" [options]="info.options" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></name-page>
      <attributes-page *ngIf="page.name == 'FIELDS'" [page]="page" [sheet]="sheet" [info]="info" (onFieldChange)="refreshSteps()" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></attributes-page>
      <location-page *ngIf="page.name == 'LOCATION'" [page]="page" [sheet]="sheet" [info]="info" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></location-page>
<!-- 
              <coordinate-page *ngIf="page.name == 'COORDINATE'" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></coordinate-page>
              <summary-page *ngIf="page.name == 'SUMMARY'" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></summary-page>
              <geo-validation-page *ngIf="page.name == 'GEO-VALIDATION'"></geo-validation-page>
              <category-validation-page *ngIf="page.name == 'CATEGORY-VALIDATION'"></category-validation-page>              
 -->        
      
<!-- 
      <form #form="ngForm" name="form" class="modal-form" (ngSubmit)="form.valid && !busy && onSubmit()">
        <div>
          <fieldset>
            <section class="form-container">
            </section>            
          </fieldset>
          
          <div class="row-holder" >
            <div class="label-holder"></div>          
            <div [ngClass]="{'holder' : (page.name != 'GEO-VALIDATION' && page.name != 'CATEGORY-VALIDATION' && page.name != 'FIELDS'), 'wide-holder' : (page.name == 'GEO-VALIDATION' || page.name == 'CATEGORY-VALIDATION' || page.name == 'FIELDS')}">
              <div class="button-holder">
                <input
                  type="button"
                  value="<gdb:localize key="dashboard.Cancel"/>"
                  class="btn btn-default" 
                  (click)="cancel()"
                  [disabled]="busy"                  
                />
                <input
                  *ngIf="page.snapshots.length > 0"           
                  type="button"
                  value="<gdb:localize key="dataUploader.previous"/>"
                  class="btn btn-primary" 
                  (click)="prev()"
                  [disabled]="busy"
                /> 
                <input
                  *ngIf="hasNextPage()"      
                  type="button"
                  value="<gdb:localize key="dataUploader.next"/>"
                  class="btn btn-primary" 
                  (click)="next()"
                  [disabled]="!form.valid"
                />
                <input 
                  *ngIf="isReady()"
                  type="submit"
                  value="<gdb:localize key="dataUploader.import"/>"
                  class="btn btn-primary" 
                  [disabled]="!form.valid" />     
              </div>
            </div>
          </div>          
        </div>
      </form>
 -->            
    </div>
    </div>
  </div>  
</div>
