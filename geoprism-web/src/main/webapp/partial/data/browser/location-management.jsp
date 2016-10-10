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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<div id="app-container" class="container">

<%--   <h2> <gdb:localize key="location.management.title"/> </h2> --%>
  
  <div ng-if="errors.length > 0" class="error-container" ng-cloak>
    <div class="label-holder">
      <strong><gdb:localize key='dashboard.errorsLabel'/></strong>
    </div>
    <div class="holder">
      <div ng-repeat="error in errors" >
        <p class="error-message">{{error}}</p>
      </div>
    </div>
  </div>
  
  <div ng-if="location.management === null"><gdb:localize key='category.management.loadingData'/></div>
  
  <div class="row">
  <div class="col-md-6">
  
    <div ng-show="previous.length > 1">
      <span ng-repeat="entity in previous" ng-if="$index < previous.length - 1">
        > <a href ng-click="ctrl.back($index)"> {{entity.displayLabel}} ({{entity.geoId}}) </a>
      </span>
    </div>
    <div>
      <div><label>Location</label></div>
      <div class="text">{{entity.displayLabel}} ({{entity.geoId}})</div>
    </div>
    <div ng-if="universal.options.length > 0">
      <div><label>Sub location types</label></div>
      <div>
        <select ng-model="universal.value" ng-options="opt.id as opt.displayLabel for opt in universal.options" ng-change="ctrl.setUniversal()">
          <option value="">All</option>
        </select>                
      </div>
    </div>
    <div ng-show="children.length > 0">
      <div><label>Sub locations</label></div>
      <div>
        <ul>
          <li ng-repeat="child in children">
            <a href ng-click="ctrl.select(child)"> {{child.displayLabel}} ({{child.geoId}}) </a>
          </li>
        </ul>
      </div>
    </div>    
  </div>
  <div class="col-md-6">
  	<editable-map data-enableedits="true"></editable-map>
  </div>
</div>
  
   
</div>
