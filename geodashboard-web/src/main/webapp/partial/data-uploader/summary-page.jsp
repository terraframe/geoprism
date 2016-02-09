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
    <strong><gdb:localize key="dataUploader.summary"/></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <ul>
        <li ng-repeat="field in sheet.fields" ng-if="ctrl.isValid(field)">
          {{field.label}} :
          <span ng-switch on="field.type">
            <span ng-switch-when="CATEGORY"><gdb:localize key="dataUploader.category"/></span>
            <span ng-switch-when="TEXT"><gdb:localize key="dataUploader.text"/></span>
            <span ng-switch-when="LONG"><gdb:localize key="dataUploader.long"/></span>
            <span ng-switch-when="DOUBLE"><gdb:localize key="dataUploader.double"/></span>
            <span ng-switch-when="DATE"><gdb:localize key="dataUploader.date"/></span>
            <span ng-switch-when="BOOLEAN"><gdb:localize key="dataUploader.boolean"/></span>
          </span>          
        </li>
        <li ng-repeat="id in sheet.attributes.ids" ng-init="attribute = sheet.attributes.values[id]">
          {{attribute.label}}   
          <ul>
            <li ng-repeat="universal in universals" ng-if="attribute.fields[universal.value] != null && attribute.fields[universal.value] != 'EXCLUDE'">
              {{attribute.fields[universal.value]}}            
            </li>
          </ul>       
        </li>    
        <li ng-repeat="id in sheet.coordinates.ids" ng-init="coordinate = sheet.coordinates.values[id]">
          {{coordinate.label}}   
          <ul>
            <li><gdb:localize key="dataUploader.latitude"/> : {{coordinate.latitude}}</li>
            <li><gdb:localize key="dataUploader.longitude"/> : {{coordinate.longitude}}</li>
            <li><gdb:localize key="dataUploader.featureLabel"/> : {{coordinate.featureLabel}}</li>
            <li><gdb:localize key="dataUploader.associatedUniversal"/> : {{labels[coordinate.universal]}}</li>
            <li ng-if="coordinate.location != 'DERIVE'"><gdb:localize key="dataUploader.locationAttribute"/> : {{coordinate.location}}</li>
            <li ng-if="coordinate.location == 'DERIVE'"><gdb:localize key="dataUploader.locationAttribute"/> : <gdb:localize key="dataUploader.deriveLocation"/></li>
          </ul>       
        </li>        
      </ul>
    <div>
  </div>
</div>
