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

<div>
  <div class="label-holder"></div>
  <div class="holder">
    <div class="row-holder">
      <p><gdb:localize key="dataUploader.textLocationConfiguration.heading.paragraph"/></p>
    </div>
  </div>

  <div ng-if="unassignedFields.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.unassignedLocationFields"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">
        <div ng-repeat="field in unassignedFields" class="location-selector-container">
          <h3 class="location-field-info-card-title">{{field.label}}</h3>
        </div>
      </div> <!-- end row-holder -->
    </div> <!-- end holder -->
  </div> 
  	
  <ng-form name="ctrl.attributeForm" isolate-form ng-if="attribute != null">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.locationCreatorWidgetLabel"/></strong>
    </div>    
    <div class="holder">
      <div class="location-selector-container">
	      <div class="row-holder">
	      	<h4 class="location-select-container-heading-text"><gdb:localize key="dataUploader.locationContainerHeadingToolTip"/> {{attribute.label}}</h4>
<%-- 	      	<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.locationContainerHeadingHelpInfoToolTip"/>"></i>       --%>
	      </div>
        <span class="text">
	        <input ng-model="attribute.label" name="label" ng-required="true" type="text" validate-unique validator="ctrl.isUniqueLabel"></input>
	      </span>
	      <div class="error-message">
	        <p ng-show="ctrl.attributeForm.label.$error.unique">
	          <gdb:localize key="dataUploader.unique"/>
	        </p>    
	      </div>
	      <div class="row-holder">
	        <hr>
	      </div>	      
	      <div class="row-holder" ng-repeat="universal in universalOptions track by $index">
	      	<div class="label-help-ico-container">
	      		<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.fieldHelp01ToolTip"/> {{universal.label}} <gdb:localize key="dataUploader.fieldHelp02ToolTip"/> {{attribute.label}} <gdb:localize key="dataUploader.fieldHelp03ToolTip"/>"></i>      
	      		<p class="select-label">{{universal.label}} <gdb:localize key="dataUploader.selectLabelToolTip"/></p>
	      	</div>
	      	<div class="location-selector-box-right">
		        <div class="box">
		          <select class="select-area" ng-model="attribute.fields[universal.value]" ng-change="ctrl.change(attribute.fields)" name="{{::$index + '-universal'}}" ng-required="true">
		            <option value=""></option>          
		            <option ng-repeat="field in locationFields[universal.value]" value="{{field.label}}">{{field.label}}</option>   
		            <option value="EXCLUDE"><gdb:localize key="dataUploader.exclude"/></option>
		          </select>
		        </div>
<%-- 		        <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.fieldHelp01ToolTip"/> {{universal.label}} <gdb:localize key="dataUploader.fieldHelp02ToolTip"/> {{attribute.label}} <gdb:localize key="dataUploader.fieldHelp03ToolTip"/>"></i>       --%>
	      	</div>
	      </div>
      	<div class="row-holder">
        	<div class="button-holder">
          		<input type="button" value="+" class="btn btn-primary set-location-btn pull-right"  ng-click="ctrl.newAttribute()" ng-disabled="ctrl.attributeForm.$invalid" title="<gdb:localize key="dataUploader.createBtnToolTip"/>" />
        	</div>
      	</div>  
      </div>  
    </div>
  </ng-form>  
  
  <div ng-if="sheet.attributes.ids.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.attributes"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">
        
        <div ng-repeat="id in sheet.attributes.ids" ng-init="attribute = sheet.attributes.values[id]" class="location-selector-container">
            <h3 class="location-field-info-card-title">{{attribute.label}}</h3>
            <div class="cell" style="float: right;">            
            	<a href="#" class="fa fa-pencil ico-edit" ng-click="ctrl.edit(attribute)" title="<gdb:localize key="dataUploader.editToolTip"/>"></a>
            	<a href="#" class="fa fa-trash-o ico-remove" ng-click="ctrl.remove(attribute)" title="<gdb:localize key="dataUploader.deleteToolTip"/>"></a>
          	</div>
          	<div class="row-holder">
<!--           	  <hr> -->
          	</div>
            <ul class="location-field-list-display">
              <li class="" ng-repeat="universal in universals track by $index" ng-if="attribute.fields[universal.value] != null && attribute.fields[universal.value] != 'EXCLUDE'">
                <i class="fa fa-check-square"></i>{{attribute.fields[universal.value]}}            
              </li>
            </ul>   
        </div>
        
      </div> <!-- end row-holder -->
    </div> <!-- end holder -->
  </div> <!-- end ng-if -->
  
  <div class="label-holder"></div>
  <div class="holder">
    <div class="error-message">
      <p ng-show="form.$error.size"><gdb:localize key="dataUploader.unassignedLocationFields"/></p>
    </div>          
  </div>  
</div>
