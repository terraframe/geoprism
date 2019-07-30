<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>


<div>
  <modal-dialog modal="uploader-div" overlay="uploader-overlay" ng-if="show">
    <div role="dialog" class="ng-modal-content modal-content" style="display: none;" show-on-ready>
      	
      	<div class="uploader-step-indicator-container" ng-if="page.current != 'MATCH-INITIAL' && page.current != 'MATCH' && !updateExistingDataset">
	      	<ol class="wizard-progress clearfix">
			    <li ng-class="{'active-step' : page.current === step.page, 'status-li-disabled' : page.current !== step.page}" ng-repeat="step in userSteps track by $index">
			        <span ng-if="step.label == '1'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep1"/></span>
			        <span ng-if="step.label == '2'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep2"/></span>
			        <span ng-if="step.label == '3'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep3"/></span>
			        <span ng-if="step.label == '4'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep4"/></span>
			        <span ng-if="step.label == '5'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep5"/></span>
			        <span ng-if="step.label == '6'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep6"/></span>
			        <span ng-if="step.label == '7'" class="step-name fade-ngIf"><gdb:localize key="dataUploader.uploadStepsLabelStep7"/></span>
			        <span class="visuallyhidden">Step </span><span class="step-num">{{$index + 1}}</span>
			    </li>
			</ol>
		</div>      
      <div class="heading">
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'MATCH-INITIAL'"><gdb:localize key="dataUploader.titleUploadToExistingOrNew"/></h1>
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'MATCH'"><gdb:localize key="dataUploader.titleUploadToExistingOrNew"/></h1>
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'BEGINNING-INFO'"><gdb:localize key="dataUploader.uploadBeginningMessageTitle"/></h1>
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'INITIAL'"><gdb:localize key="dataUploader.titleNameCountry"/></h1>
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'FIELDS'"><gdb:localize key="dataUploader.titleAttributeConfiguration"/></h1>
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'LOCATION'"><gdb:localize key="dataUploader.titleTextLocationConfiguration"/></h1>
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'COORDINATE'"><gdb:localize key="dataUploader.titleCoordinateLocationConfiguration"/></h1>
        <h1 class="ui-dialog-title" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}" ng-if="page.current == 'SUMMARY'"><gdb:localize key="dataUploader.titleSummary"/></h1>
        <h1 class="ui-dialog-title" ng-if="page.current == 'GEO-VALIDATION'"><gdb:localize key="dataUploader.titleLocationValidation"/></h1>
        <h1 class="ui-dialog-title" ng-if="page.current == 'CATEGORY-VALIDATION'"><gdb:localize key="dataUploader.titleCategoryValidation"/></h1>
      </div>
      <form name="form" class="modal-form">
        <div ng-if="errors.length > 0" class="error-container">
          <div class="label-holder">
            <strong style="color: #8c0000;"><gdb:localize key='dashboard.errorsLabel'/></strong>
          </div>
          <div ng-class="{'holder' : (page.current != 'GEO-VALIDATION' && page.current != 'CATEGORY-VALIDATION'), 'wide-holder' : (page.current == 'GEO-VALIDATION' || page.current == 'CATEGORY-VALIDATION' || page.current == 'FIELDS')}">
            <div ng-repeat="error in errors">
              <p class="error-message">{{error}}</p>
            </div>
          </div>
        </div>
        <div>
          <fieldset>
            <section class="form-container">
              <match-initial-page ng-if="page.current == 'MATCH-INITIAL'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></match-initial-page> 
              <match-page ng-if="page.current == 'MATCH'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></match-page>  
              <beginning-info-page ng-if="page.current == 'BEGINNING-INFO'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></beginning-info-page>            
              <name-page ng-if="page.current == 'INITIAL'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></name-page>
              <attributes-page ng-if="page.current == 'FIELDS'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></attributes-page>
              <location-page ng-if="page.current == 'LOCATION'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></location-page>
              <coordinate-page ng-if="page.current == 'COORDINATE'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></coordinate-page>
              <summary-page ng-if="page.current == 'SUMMARY'" ng-class="{'slide-right': pageDirection == 'NEXT', 'slide-left': pageDirection == 'PREVIOUS'}"></summary-page>
              <geo-validation-page ng-if="page.current == 'GEO-VALIDATION'"></geo-validation-page>
              <category-validation-page ng-if="page.current == 'CATEGORY-VALIDATION'"></category-validation-page>              
            </section>            
          </fieldset>
          <div class="row-holder" >
            <div class="label-holder"></div>          
            <div ng-class="{'holder' : (page.current != 'GEO-VALIDATION' && page.current != 'CATEGORY-VALIDATION' && page.current != 'FIELDS'), 'wide-holder' : (page.current == 'GEO-VALIDATION' || page.current == 'CATEGORY-VALIDATION' || page.current == 'FIELDS')}">
              <div class="button-holder" fire-on-ready>
                <input
                  type="button"
                  value="<gdb:localize key="dashboard.Cancel"/>"
                  class="btn btn-default" 
                  ng-click="ctrl.cancel()"
                  ng-disabled="busy"                  
                />
                <input
                  ng-if="page.snapshots.length > 0"           
                  type="button"
                  value="<gdb:localize key="dataUploader.previous"/>"
                  class="btn btn-primary" 
                  ng-click="ctrl.prev()"
                  ng-disabled="busy"
                /> 
                <input
                  ng-if="ctrl.hasNextPage()"      
                  type="button"
                  value="<gdb:localize key="dataUploader.next"/>"
                  class="btn btn-primary" 
                  ng-click="ctrl.next()"
                  ng-disabled="form.$invalid || busy"
                />
                <input 
                  ng-if="ctrl.isReady()"
                  type="button"
                  value="<gdb:localize key="dataUploader.import"/>"
                  class="btn btn-primary" 
                  ng-click="ctrl.persist()"
                  ng-disabled="form.$invalid || busy"
                />     
              </div>
            </div>
          </div>          
        </div>
      </form>
    </div>
  </modal-dialog>       
</div>
