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

<article class="accordion info-box" id="ref-layer-container">
  <div class="accordion-group sales-accortion" id="ref-layer-sub-container">
    <div class="accordion-heading">
      <a class="map-layers-opener opener" id="ref-layer-opener-button" data-toggle="collapse" data-parent="#ref-layer-container" href="#collapse-ref-layer"><gdb:localize key="dashboardViewer.refLayer"/></a>
    </div>	
    <div id="collapse-ref-layer" class="accordion-body" style="height: 0px;">
<!--       <div style="display: block;"> -->
<%--         <a href="#" ng-click="ctrl.add()" class="fa fa-plus referenceLayer ico-enable" title="<gdb:localize key="dashboardViewer.refLayerEnableTooltip"/>"></a>      --%>
<!--       </div> -->
      <div class="accordion-inner holder" id="refLayerContainer">
      
        <div class="row-form" ng-repeat="universalId in cache.ids" ng-init="layer = cache.values[universalId]">
          <div class="com-runwaysdk-ui-factory-runway-Widget check" ng-class="{'com-runwaysdk-ui-factory-runway-checkbox-CheckBox' : layer.layerExists, 'checked' : layer.isActive}" ng-click="ctrl.toggle(layer.layerId, layer.universalId)"></div>
          <label>{{layer.layerName}}</label>
          
          <div class="cell" ng-if="ctrl.canEdit()">
            <a href="#" class="fa fa-times ico-remove" ng-click="ctrl.remove(layer.layerId, layer.universalId)" title="<gdb:localize key="dashboardViewer.deleteLayerTooltip"/>"></a>
            <a href="#" class="fa fa-pencil ico-edit" ng-click="ctrl.edit(layer.layerId, layer.universalId)" title="<gdb:localize key="dashboardViewer.editLayerTooltip"/>"></a>            
          </div>
        </div>
         <a href="#" ng-click="ctrl.add()" class="fa fa-plus referenceLayer ico-enable" title="<gdb:localize key="dashboardViewer.refLayerEnableTooltip"/>">map new</a>     
      </div>
    </div>
  </div>
</article>