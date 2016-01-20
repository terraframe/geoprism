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
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<div>
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.fields"/></strong>
  </div>
  <div class="holder">
    <div class="row-holder" ng-repeat="attribute in sheet.attributes">
      <div class="inline-text">
        <input ng-model="attribute.name" name="{{::$index + '-name'}}" ng-required="true" type="text" validate-unique validator="ctrl.isUniqueLabel"></input>
      </div>
      <div class="inline-box">
        <select class="select-area" ng-model="attribute.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted attribute="attribute">
          <option value=""><gdb:localize key="dataUploader.undefined"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
          <option value="BOOLEAN"><gdb:localize key="dataUploader.boolean"/></option>
          <option value="SST_STRING"><gdb:localize key="dataUploader.text"/></option>
          <option value="NUMBER"><gdb:localize key="dataUploader.number"/></option>
          <option value="DATE"><gdb:localize key="dataUploader.date"/></option>
          <option value="CATEGORY"><gdb:localize key="dataUploader.category"/></option>
          <option value="LOCATION"><gdb:localize key="dataUploader.location"/></option>
          <option value="LONGITUDE"><gdb:localize key="dataUploader.longitude"/></option>
          <option value="LATITUDE"><gdb:localize key="dataUploader.latitude"/></option>
          <option value="WKT"><gdb:localize key="dataUploader.wkt"/></option>
        </select>      
      </div>      
      <div class="inline-box" ng-if="attribute.type == 'LOCATION'">
        <select class="select-area" ng-model="attribute.universal" name="{{::$index + '-universal'}}" ng-required="true" ng-options="opt.value as opt.label for opt in universals">
          <option value=""></option>          
        </select>
      </div>      
      <div class="inline-error-message">
        <p ng-show="form[$index + '-name'].$error.required || form[$index + '-type'].$error.required || form[$index + '-universal'].$error.required">
          <gdb:localize key="dashboard.Required"/>
        </p>    
        <p ng-show="form[$index + '-name'].$error.unique">
          <gdb:localize key="dataUploader.unique"/>
        </p>    
        <p ng-show="form[$index + '-type'].$error.accepted">
          <a ng-click="ctrl.accept(attribute)"><gdb:localize key="dataUploader.acceptType"/></a>
        </p>
      </div>      
    </div>
  </div> 
</div>
