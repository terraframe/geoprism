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
    <div class="row-holder" ng-repeat="field in sheet.fields">
      <div class="inline-text">
        <label><gdb:localize key="dataUploader.label"/></label>
        <input ng-model="field.label" name="{{::$index + '-name'}}" ng-class="{textInputDisabled : field.type == 'IGNORE'}" ng-required="true" type="text" validate-unique validator="ctrl.isUniqueLabel"></input>
      </div>
      <div class="inline-box" ng-if="field.columnType == 'TEXT'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" ng-class="{selectInputDisabled : field.type == 'IGNORE'}" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="LOCATION"><gdb:localize key="dataUploader.location"/></option>
          <option value="CATEGORY"><gdb:localize key="dataUploader.category"/></option>
          <option value="TEXT"><gdb:localize key="dataUploader.text"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
          <option value=""><gdb:localize key="dataUploader.undefined"/></option>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == 'NUMBER'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="LONG"><gdb:localize key="dataUploader.long"/></option>
          <option value="DOUBLE"><gdb:localize key="dataUploader.double"/></option>
          <option value="LONGITUDE"><gdb:localize key="dataUploader.longitude"/></option>
          <option value="LATITUDE"><gdb:localize key="dataUploader.latitude"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
          <option value=""><gdb:localize key="dataUploader.undefined"/></option>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == 'DATE'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="DATE"><gdb:localize key="dataUploader.date"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
          <option value=""><gdb:localize key="dataUploader.undefined"/></option>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == 'BOOLEAN'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="BOOLEAN"><gdb:localize key="dataUploader.boolean"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
          <option value=""><gdb:localize key="dataUploader.undefined"/></option>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == ''">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
          <option value=""><gdb:localize key="dataUploader.undefined"/></option>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.type == 'LOCATION'">
        <label><gdb:localize key="dataUploader.locationType"/></label>
        <select class="select-area" ng-model="field.universal" name="{{::$index + '-universal'}}" ng-required="true" ng-options="opt.value as opt.label for opt in universals">
          <option value=""></option>          
        </select>
      </div>      
      <div class="inline-number" ng-if="field.type == 'DOUBLE'">
        <label><gdb:localize key="dataUploader.precision"/></label>
        <input ng-model="field.precision" name="{{::$index + 'precision'}}" ng-required="true" type="text" integer-only></input>
      </div>
      <div class="inline-number" ng-if="field.type == 'DOUBLE'">
        <label><gdb:localize key="dataUploader.scale"/></label>
        <input ng-model="field.scale" name="{{::$index + 'scale'}}" ng-required="true" type="text" integer-only></input>
      </div>
      <div class="inline-error-message">
<!-- 
        <p ng-show="form[$index + '-name'].$error.required || form[$index + '-type'].$error.required || form[$index + '-universal'].$error.required">
          <gdb:localize key="dashboard.Required"/>
        </p>    
 -->      
        <p ng-show="form[$index + '-name'].$error.unique">
          <gdb:localize key="dataUploader.unique"/>
        </p>    
        <p ng-show="form[$index + '-type'].$error.accepted" style="padding-top: 26px;">
          <i class="fa fa-exclamation" ng-click="ctrl.accept(field)" title="<gdb:localize key="dataUploader.acceptType"/>"></i>
        </p>
      </div>      
    </div>
  </div> 
</div>
