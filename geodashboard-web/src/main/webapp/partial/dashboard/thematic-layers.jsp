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
<article class="accordion info-box" id="overlay-container">
  <div class="accordion-group sales-accortion">
    <div class="accordion-heading">
      <a class="map-layers-opener opener" id="overlay-opener-button" data-toggle="collapse" data-parent="#overlay-container" href="#collapse-overlay"><gdb:localize key="dashboardViewer.mapLayers"/></a>
    </div>
    <div id="collapse-overlay" class="accordion-body" style="height: 0px;">
      <div class="accordion-inner holder" id="overlayLayerContainer">
        <div class="row-form" ng-repeat="layerId in cache.ids" data-id="{{layerId}}" ng-init="layer = cache.values[layerId]">
                
          <div class="check com-runwaysdk-ui-factory-runway-checkbox-CheckBox com-runwaysdk-ui-factory-runway-Widget checked" ng-class="{'checked' : layer.checked || layer.isActive}" ng-click="ctrl.toggle(layer.layerId)"></div>
          <label>{{layer.layerName}}</label>
          <div class="cell" ng-if="true">            
            <a href="#" class="fa fa-times ico-remove" ng-click="ctrl.remove(layer.layerId)" title="<gdb:localize key="dashboardViewer.deleteLayerTooltip"/>"></a>
            <a href="#" class="fa fa-pencil ico-edit" ng-click="ctrl.edit(layer.layerId)" title="<gdb:localize key="dashboardViewer.editLayerTooltip"/>"></a>
          </div>
        </div>
      </div>      
    </div>
  </div>
</article>