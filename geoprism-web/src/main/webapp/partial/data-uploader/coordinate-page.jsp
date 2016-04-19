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
  <div ng-repeat="id in sheet.coordinates.ids" ng-init="coordinate = sheet.coordinates.values[id]">
    <div class="label-holder">
      <strong ng-if="$index == 0"><gdb:localize key="dataUploader.coordinateCreatorWidgetLabel"/></strong>
    </div>
    <div class="holder">
      <div class="location-selector-container">
	      <span class="text">
	        <input ng-model="coordinate.label" name="{{::$index + '-label'}}" ng-required="true" type="text" validate-unique validator="ctrl.isUniqueLabel"></input>
	      </span>
	      <div class="error-message">
	        <p ng-show="form[$index + '-label'].$error.unique">
	          <gdb:localize key="dataUploader.unique"/>
	        </p>    
	      </div>   
	              
	      <div class="row-holder">
	        <div class="label-help-ico-container">
	        	<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.latFieldHelpToolTip"/>"></i>
	      		<p class="select-label"><gdb:localize key="dataUploader.latitude"/></p>
	      	</div>
	        <div class="inline-text" style="padding-right: 26px">
            <input ng-model="coordinate.latitude" name="{{::$index + '-latitude'}}" ng-required="true" type="text" disabled="disabled"></input>
	        </div>
	      </div>
	
	      <div class="row-holder">
	      	<div class="label-help-ico-container">
	      	    <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.longFieldHelpToolTip"/>"></i>
	        	<p class="select-label"><gdb:localize key="dataUploader.longitude"/></p>
	        </div>
	        <div class="box" ng-if="longitudes.length != 1">
	          <select class="select-area" name="{{::$index + '-longitude'}}" ng-model="coordinate.longitude" ng-required="true" ng-options="opt.label as opt.label for opt in longitudes">
	            <option value=""></option>
	          </select>
	        </div>
          <div class="inline-text" style="padding-right: 26px" ng-if="longitudes.length == 1">
            <input ng-model="coordinate.longitude" name="{{::$index + '-longitude'}}" ng-required="true" type="text" disabled="disabled"></input>
          </div>
	      </div>
	
	      <div class="row-holder">
	      	<div class="label-help-ico-container">
	      	    <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.featureLabelFieldHelpToolTip"/>"></i> 
	        	<p class="select-label"><gdb:localize key="dataUploader.featureLabel"/></p>
	        </div>
	        <div class="box">
	          <select class="select-area" name="{{::$index + '-featureLabel'}}" ng-model="coordinate.featureLabel" ng-required="true" ng-options="opt.label as opt.label for opt in featureLabels">
	            <option value=""></option>
	          </select>
	        </div> 
	      </div>
	      
	      <div class="row-holder">
	      	<div class="label-help-ico-container">
	      	    <i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.locAttrFieldHelpToolTip"/>"></i> 
	        	<p class="select-label"><gdb:localize key="dataUploader.locationAttribute"/></p>
	        </div>
	        <div class="box">
	          <select class="select-area" ng-model="coordinate.location" name="{{::$index + '-location'}}" ng-change="coordinate.universal = ''" ng-required="true">
	            <option value=""></option>          
	            <option value="DERIVE"><gdb:localize key="dataUploader.deriveLocation"/></option>
	            <option ng-repeat="location in locations" value="{{location.label}}">{{location.label}}</option>          
	          </select>          
	        </div> 
	      </div>	            
	      
        <div class="row-holder" ng-show="coordinate.location == 'DERIVE'">
          <div class="label-help-ico-container">
          	<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.assocUniversalieldHelpToolTip"/>"></i>
          	<p class="select-label"><gdb:localize key="dataUploader.associatedUniversal"/></p>
          </div>
          <div class="box">
            <select class="select-area" ng-model="coordinate.universal" name="{{::$index + '-universal'}}" ng-options="opt.value as opt.label for opt in universals" ng-required="coordinate.location == 'DERIVE'">
              <option value=""></option>          
            </select>
          </div>   
        </div>        	      
	   </div>  <!-- end location-selector-container -->
    </div>  <!-- end holder -->
  </div>

  <div class="label-holder"></div>
  <div class="holder">
    <div class="error-message">
      <p ng-show="form.$error.size"><gdb:localize key="dataUploader.unassignedCoordinateFields"/></p>
    </div>          
  </div>    
</div>