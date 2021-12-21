<%--

    Copyright (c) 2022 TerraFrame, Inc. All rights reserved.

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
<!--   <div class="label-holder"> -->
<!--     <strong> </strong> -->
<!--   </div> -->
  <div class="wide-holder">
    <div class="row-holder">
    	<p><gdb:localize key="dataUploader.attributeConfiguration.heading.paragraph"/></p>
    </div>
  </div>
  
<!--   <div class="label-holder"> -->
<%--     <strong><gdb:localize key="dataUploader.fields"/></strong> --%>
<!--   </div> -->
  <div class="wide-holder">
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
<%--           <option value=""><gdb:localize key="dataUploader.undefined"/></option> --%>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == 'NUMBER'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="LONG"><gdb:localize key="dataUploader.long"/></option>
          <option value="DOUBLE"><gdb:localize key="dataUploader.double"/></option>
          <option value="LOCATION"><gdb:localize key="dataUploader.location"/></option>
          <option value="LATITUDE"><gdb:localize key="dataUploader.latitude"/></option>
          <option value="LONGITUDE"><gdb:localize key="dataUploader.longitude"/></option>
          <option value="TEXT"><gdb:localize key="dataUploader.text"/></option>     
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>     
<%--           <option value=""><gdb:localize key="dataUploader.undefined"/></option> --%>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == 'DATE'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="DATE"><gdb:localize key="dataUploader.date"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
<%--           <option value=""><gdb:localize key="dataUploader.undefined"/></option> --%>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == 'BOOLEAN'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="BOOLEAN"><gdb:localize key="dataUploader.boolean"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
<%--           <option value=""><gdb:localize key="dataUploader.undefined"/></option> --%>
        </select>      
      </div>      
      <div class="inline-box" ng-if="field.columnType == ''">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" ng-model="field.type" name="{{::$index + '-type'}}" ng-required="true" validate-accepted field="field" ng-change="ctrl.accept(field)">
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
<%--           <option value=""><gdb:localize key="dataUploader.undefined"/></option> --%>
        </select>      
      </div>      
      <div class="inline-box fade-ngIf" ng-if="field.type == 'LOCATION'">
        <label><gdb:localize key="dataUploader.locationType"/></label>
        <select class="select-area" ng-model="field.universal" name="{{::$index + '-universal'}}" ng-required="true" ng-options="opt.value as opt.label for opt in universals">
          <option value=""></option>          
        </select>
      </div>      
      <div class="inline-box fade-ngIf" ng-if="field.type == 'CATEGORY'">
        <label><gdb:localize key="dataUploader.domainRoot"/></label>
        <select class="select-area" ng-model="field.root" name="{{::$index + '-root'}}" ng-options="opt.value as opt.label for opt in classifiers | orderBy:'label'">
          <option value=""><gdb:localize key="dataUploader.new"/></option>          
        </select>
      </div>      
      <div class="inline-text fade-ngIf" ng-if="field.type == 'CATEGORY' && field.root == null">
        <label><gdb:localize key="dataUploader.categoryLabel"/></label>
        <input ng-model="field.categoryLabel" name="{{::$index + '-categoryLabel'}}" ng-init="ctrl.initializeField(field)" ng-required="true" type="text" validate-unique validator="ctrl.isUniqueCategory"></input>
      </div>      

      <div class="inline-error-message pull-right">
        <p ng-show="form[$index + '-name'].$error.unique">
          <gdb:localize key="dataUploader.unique"/>
        </p>    
        <p ng-show="form[$index + '-categoryLabel'].$error.unique">
          <gdb:localize key="dataUploader.uniqueCategory"/>
        </p>    
        <p ng-show="form[$index + '-type'].$error.accepted" style="padding-top: 26px;">
          <i class="fa fa-exclamation" ng-click="ctrl.accept(field)" title="<gdb:localize key="dataUploader.acceptType"/>"></i>
        </p>
      </div>      
    </div>
  </div> 
<!--   <div class="label-holder"></div> -->
  <div class="wide-holder">
    <div class="error-message">
      <p ng-show="form.$error.coordinate"><gdb:localize key="dataUploader.coordinateMismatch"/></p>
      <p ng-show="form.$error.coordinateText"><gdb:localize key="dataUploader.coordinateNoLabel"/></p>
    </div>          
  </div>    
</div>
