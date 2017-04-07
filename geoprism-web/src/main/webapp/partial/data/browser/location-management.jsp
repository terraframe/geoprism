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
      <input type="text" placeholder="<gdb:localize key="location.management.autocomplete"/>" autocomplete="on" callback-auto-complete source="ctrl.getGeoEntitySuggestions" setter="ctrl.open"></input>
    </div>
    
    <div class="location-management-widget-section">
      <ul class = "breadcrumb">
        <li ng-repeat="entity in previous" ng-class="{'active':$last}" ng-if="$index < previous.length">
          <a ng-if="!$last" href="" ng-click="ctrl.back($index)"> {{entity.displayLabel}}</a>
          <span ng-if="$last"> {{entity.displayLabel}}</span>
        </li>
      </ul>
    </div>
    <div class="location-management-widget-section" ng-show="entity != null">
      <div><label><gdb:localize key="location.management.entity"/></label></div>
      <div class="text">{{entity.displayLabel}} ({{entity.universal}}) : {{entity.geoId}}</div>
    </div>
    <div ng-if="universal.options.length > 1">
      <div><label><gdb:localize key="location.management.filter"/></label></div>
      <div>
        <select ng-model="universal.value" ng-options="opt.id as opt.displayLabel for opt in universal.options" ng-change="ctrl.setUniversal()">
<!-- 
          <option value=""><gdb:localize key="location.management.all"/></option>
 -->        
        </select>                
      </div>
    </div>
    <div class="location-management-widget-section location-management-list-container" ng-show="children.length > 0">
      <div><label><gdb:localize key="location.management.sublocations"/></label></div>
      <div>
        <div class="list-group">
          <a href ng-repeat="child in children" id="{{child.geoId}}{{child.id}}" class="list-group-item" ng-class="{'hover' : hoverId === child.id}" ng-click="ctrl.select(child, $event)" ng-mouseover="ctrl.listItemHover(child, $event)" ng-mouseleave="ctrl.listItemHoverOff(child, $event)">
            {{child.displayLabel}} : {{child.geoId}}
            <div class="pull-right">
            <!-- Disabled feature (due to lack of resources): The ability to edit a feature by clicking in the dropdown list
              <span class="inner-action fa fa-globe ico-edit-geometry" ng-click="ctrl.editGeometry(child)"></span>
              -->
              <span class="inner-action fa fa-pencil ico-edit" ng-click="ctrl.edit(child)" title="<gdb:localize key="location.management.editTooltip"/>"></span>
              <span class="inner-action fa fa-link ico-synonym" ng-click="ctrl.viewSynonyms(child)" title="<gdb:localize key="location.management.synonymsTooltip"/>"></span>
              <span class="inner-action fa fa-trash-o ico-remove" ng-click="ctrl.remove(child)" title="<gdb:localize key="location.management.removeTooltip"/>"></span>                         
            </div>
          </a>
        </div>
      </div>
    </div>    
  </div>
  <div class="col-md-9">
  	<editable-map-webgl enable-edits="true" include-context-layer="true" base-map-type="Bing"></editable-map-webgl>
  </div>
  </div>
  
  <location-modal layers="layers"></location-modal>
  <location-synonym-modal layers="layers"></location-synonym-modal>
</div>
