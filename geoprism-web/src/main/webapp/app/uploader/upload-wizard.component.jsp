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
  <div id="uploader-div" class='ng-modal-dialog'>
  
    <error-message ></error-message>
  
    <div class='ng-modal-dialog-content'>
      <div role="dialog" class="ng-modal-content modal-content">       
        <div class="uploader-step-indicator-container" *ngIf="showStep()">
          <ol class="wizard-progress clearfix">
            <li *ngFor="let step of steps; let i = index;" [ngClass]="{'active-step' : page.name === step.page, 'status-li-disabled' : page.name !== step.page}">
              <span *ngIf="step.label == '1'" class="step-name fade-ngIf"><localize key="dataUploader.uploadStepsLabelStep1"></localize></span>
              <span *ngIf="step.label == '2'" class="step-name fade-ngIf"><localize key="dataUploader.uploadStepsLabelStep2"></localize></span>
              <span *ngIf="step.label == '3'" class="step-name fade-ngIf"><localize key="dataUploader.uploadStepsLabelStep3"></localize></span>
              <span *ngIf="step.label == '4'" class="step-name fade-ngIf"><localize key="dataUploader.uploadStepsLabelStep4"></localize></span>
              <span *ngIf="step.label == '5'" class="step-name fade-ngIf"><localize key="dataUploader.uploadStepsLabelStep5"></localize></span>
              <span *ngIf="step.label == '6'" class="step-name fade-ngIf"><localize key="dataUploader.uploadStepsLabelStep6"></localize></span>
              <span *ngIf="step.label == '7'" class="step-name fade-ngIf"><localize key="dataUploader.uploadStepsLabelStep7"></localize></span>
              <span class="visuallyhidden">Step </span><span class="step-num">{{i + 1}}</span>
            </li>
          </ol>
        </div>      
    
      <div class="heading">
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'MATCH-INITIAL'"><localize key="dataUploader.titleUploadToExistingOrNew"></localize></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'MATCH'"><localize key="dataUploader.titleUploadToExistingOrNew"></localize></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'BEGINNING-INFO'"><localize key="dataUploader.uploadBeginningMessageTitle"></localize></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'INITIAL'"><localize key="dataUploader.titleNameCountry"></localize></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'FIELDS'"><localize key="dataUploader.titleAttributeConfiguration"></localize></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'LOCATION'"><localize key="dataUploader.titleTextLocationConfiguration"></localize></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'COORDINATE'"><localize key="dataUploader.titleCoordinateLocationConfiguration"></localize></h1>
        <h1 class="ui-dialog-title" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" *ngIf="page.name == 'SUMMARY'"><localize key="dataUploader.titleSummary"></localize></h1>
        <h1 class="ui-dialog-title" *ngIf="page.name == 'GEO-VALIDATION'"><localize key="dataUploader.titleLocationValidation"></localize></h1>
        <h1 class="ui-dialog-title" *ngIf="page.name == 'CATEGORY-VALIDATION'"><localize key="dataUploader.titleCategoryValidation"></localize></h1>
      </div>
      
      <match-initial-page *ngIf="page.name == 'MATCH-INITIAL'" (onNextPage)="onNextPage($event)" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></match-initial-page> 
      <match-page *ngIf="page.name == 'MATCH'" [sheet]="sheet" (onSelect)="onSelectSheet($event)" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></match-page>  
      <beginning-info-page *ngIf="page.name == 'BEGINNING-INFO'" [page]="page" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></beginning-info-page>            
      <name-page *ngIf="page.name == 'INITIAL'" [page]="page"  [sheet]="sheet" [options]="info.options" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></name-page>
      <attributes-page *ngIf="page.name == 'FIELDS'" [page]="page" [sheet]="sheet" [info]="info" (onFieldChange)="refreshSteps()" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></attributes-page>
      <location-page *ngIf="page.name == 'LOCATION'" [page]="page" [sheet]="sheet" [info]="info" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></location-page>
      <coordinate-page *ngIf="page.name == 'COORDINATE'" [page]="page" [sheet]="sheet" [info]="info" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></coordinate-page>
      <summary-page *ngIf="page.name == 'SUMMARY'" [page]="page" [sheet]="sheet" [info]="info" [ngClass]="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></summary-page>
      <geo-validation-page *ngIf="page.name == 'GEO-VALIDATION'" [page]="page" [problems]="problems" [workbook]="info.information"></geo-validation-page>
      <category-validation-page *ngIf="page.name == 'CATEGORY-VALIDATION'" [page]="page" [problems]="problems" [workbook]="info.information"></category-validation-page>      
    </div>
    </div>
  </div>  
</div>
