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
  <ng-form name="ctrl.coordinateForm" isolate-form>
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.coordinateCreatorWidgetLabel"/></strong>
    </div>
    <div class="holder">
      <div class="location-selector-container">
	      <span class="text">
	        <input ng-model="coordinate.label" name="label" ng-required="true" type="text" validate-unique validator="ctrl.isUniqueLabel"></input>
	      </span>
	      <div class="error-message">
	        <p ng-show="ctrl.coordinateForm.label.$error.unique">
	          <gdb:localize key="dataUploader.unique"/>
	        </p>    
	      </div>   
	              
	      <div class="row-holder">
	      	<p class="select-label"><gdb:localize key="dataUploader.latitude"/></p>
	        <div class="box">
	          <select class="select-area"  name="latitude" ng-model="coordinate.latitude" ng-required="true" ng-options="opt.label as opt.label for opt in latitudes">
	            <option value=""></option>
	          </select>
	        </div>
	        <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.latFieldHelpToolTip"/>"></i>      
	      </div>
	
	      <div class="row-holder">
	        <p class="select-label"><gdb:localize key="dataUploader.longitude"/></p>
	        <div class="box">
	          <select class="select-area"  name="longitude" ng-model="coordinate.longitude" ng-required="true" ng-options="opt.label as opt.label for opt in longitudes">
	            <option value=""></option>
	          </select>
	        </div>
	        <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.longFieldHelpToolTip"/>"></i>  
	      </div>
	
	      <div class="row-holder">
	        <p class="select-label"><gdb:localize key="dataUploader.featureLabel"/></p>
	        <div class="box">
	          <select class="select-area"  name="featureLabel" ng-model="coordinate.featureLabel" ng-required="true" ng-options="opt.label as opt.label for opt in featureLabels">
	            <option value=""></option>
	          </select>
	        </div> 
	        <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.featureLabelFieldHelpToolTip"/>"></i> 
	      </div>
	      
	      <div class="row-holder">
	        <p class="select-label"><gdb:localize key="dataUploader.associatedUniversal"/></p>
	        <div class="box">
	          <select class="select-area" ng-model="coordinate.universal" name="universal" ng-options="opt.value as opt.label for opt in universals" ng-change="coordinate.location = null" ng-required="true">
	            <option value=""></option>          
	          </select>
	        </div>   
	        <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.assocUniversalieldHelpToolTip"/>"></i>   
	      </div>
	      
	      <div class="row-holder">
	        <p class="select-label"><gdb:localize key="dataUploader.locationAttribute"/></p>
	        <div class="box">
	          <select class="select-area" ng-model="coordinate.location" name="location" ng-required="true">
	            <option value=""></option>          
	            <option value="DERIVE"><gdb:localize key="dataUploader.deriveLocation"/></option>
	            <option ng-repeat="location in locations" ng-if="location.universal == coordinate.universal" value="{{location.label}}">{{location.label}}</option>          
	          </select>          
	        </div> 
	        <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.locAttrFieldHelpToolTip"/>"></i>     
	      </div>
	      <div class="row-holder">
	        <div class="button-holder">
	          <input type="button" value="+" class="btn btn-primary set-location-btn pull-right"  ng-click="ctrl.newCoordinate()" ng-disabled="ctrl.coordinateForm.$invalid" />
	        </div>
	      </div>  
	            
	   </div>  <!-- end location-selector-container -->
    </div>  <!-- end holder -->
  </ng-form>  

  <div ng-if="sheet.coordinates.ids.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.attributes"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">
         <div ng-repeat="id in sheet.coordinates.ids" ng-init="coordinate = sheet.coordinates.values[id]" class="location-field-info-card">
            <h3 class="location-field-info-card-title">{{coordinate.label}}</h3>
            <div class="cell">            
            	<a href="#" class="fa fa-pencil ico-edit" ng-click="ctrl.edit(coordinate)" title="<gdb:localize key="dataUploader.editToolTip"/>"></a>
            	<a href="#" class="fa fa-trash-o ico-remove" ng-click="ctrl.remove(coordinate)" title="<gdb:localize key="dataUploader.deleteToolTip"/>"></a>
          	</div>
          	
            <ul class="location-field-list-display">
              <li><i class="fa fa-check-square"></i><gdb:localize key="dataUploader.latitude"/> : {{coordinate.latitude}}</li>
              <li><i class="fa fa-check-square"></i><gdb:localize key="dataUploader.longitude"/> : {{coordinate.longitude}}</li>
              <li><i class="fa fa-check-square"></i><gdb:localize key="dataUploader.featureLabel"/> : {{coordinate.featureLabel}}</li>
              <li ng-if="coordinate.location != 'DERIVE'"><i class="fa fa-check-square"></i><gdb:localize key="dataUploader.locationAttribute"/> : {{coordinate.location}}</li>
              <li ng-if="coordinate.location == 'DERIVE'"><i class="fa fa-check-square"></i><gdb:localize key="dataUploader.locationAttribute"/> : <gdb:localize key="dataUploader.deriveLocation"/></li>
            </ul>   
        </div>
      
      </div>  <!-- end row-holder   -->
    </div>  <!-- end holder   -->
  </div>  <!-- end ng-if   -->
  <div class="label-holder"></div>
  <div class="holder">
    <div class="error-message">
      <p ng-show="form.$error.size"><gdb:localize key="dataUploader.unassignedCoordinateFields"/></p>
    </div>          
  </div>    
</div>