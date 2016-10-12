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
  <div class="col-md-3 lw-inner-col" id="location-explorer">
    <div class="location-management-widget-section">
      <input type="text" placeholder="<gdb:localize key="location.management.autocomplete"/>" autocomplete="on" ng-required="true" callback-auto-complete source="ctrl.getGeoEntitySuggestions" setter="ctrl.open"></input>
    </div>
    
    <div class="location-management-widget-section" ng-show="previous.length > 1">
      <span ng-repeat="entity in previous" ng-if="$index < previous.length - 1">
        > <a href ng-click="ctrl.back($index)"> {{entity.displayLabel}} </a>
      </span>
    </div>
    <div class="location-management-widget-section" ng-show="entity != null">
      <div><label><gdb:localize key="location.management.entity"/></label></div>
      <div class="text">{{entity.displayLabel}} ({{entity.universal}}) : {{entity.geoId}}</div>
    </div>
    <div ng-if="universal.options.length > 1">
      <div><label><gdb:localize key="location.management.filter"/></label></div>
      <div>
        <select ng-model="universal.value" ng-options="opt.id as opt.displayLabel for opt in universal.options" ng-change="ctrl.setUniversal()">
          <option value=""><gdb:localize key="location.management.all"/></option>
        </select>                
      </div>
    </div>
    <div class="location-management-widget-section location-management-list-container" ng-show="children.length > 0">
      <div><label><gdb:localize key="location.management.sublocations"/></label></div>
      <div>
        <ul class="location-management-list">
          <li ng-repeat="child in children">
            <a class="fa fa-pencil ico-edit" ng-click="ctrl.edit(child)" title="<gdb:localize key="location.management.editTooltip"/>"></a>                                       
            <a href ng-click="ctrl.select(child)"> {{child.displayLabel}} : {{child.geoId}} </a>
          </li>
        </ul>
      </div>
    </div>    
  </div>
  <div class="col-md-9">
  	<editable-map data-enableedits="true"></editable-map>
  </div>
  </div>
  
  <location-modal></location-modal>   
</div>
