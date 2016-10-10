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
  
    <h1>List Widget Here </h1>
    
    <div ng-show="previous.length > 1">
      <span>Bread Crumbs</span>
      <ul>
        <li ng-repeat="entity in previous" ng-if="$index < previous.length - 1" ng-click="ctrl.back($index)">
          {{entity.displayLabel}} ({{entity.geoId}})
        </li>
      </ul>
    </div>
    <div>
      <span>{{entity.displayLabel}} ({{entity.geoId}})</span>
      <ul ng-if="universal != null && universals.length > 0">
        <select ng-model="universal" ng-options="opt as opt.displayLabel for opt in universals track by opt.id" ng-change="ctrl.setUniversal()">
        </select>
      </ul>
    </div>
    <div ng-show="children.length > 0">
      <span>Children</span>
      <ul>
        <li ng-repeat="child in children" ng-click="ctrl.select(child)">
          {{child.displayLabel}} ({{child.geoId}})
        </li>
      </ul>
    </div>    
  </div>
  <div class="col-md-6">
  
  	<h1>Map Here </h1>
     <!-- map container -->
	<!-- <div class="bg-stretch"> -->
    	<div id="mapDivId" class="dynamicMap">
			<!-- <map-popup ng-if="dashboard.feature != null && dashboard.feature.show" feature="dashboard.feature"></map-popup> -->
		</div>
	<!-- </div> -->
  </div>
</div>
  
   
</div>
