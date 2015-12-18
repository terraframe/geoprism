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

<article class="accordion info-box" id="legend-collapse-container">
  <!-- Grouped legend items  -->
  <div class="accordion-group sales-accortion" id="legend-sub-container">
    <div class="accordion-heading">
      <a class="legend-opener opener" id="legend-opener-button" data-toggle="collapse" data-parent="#legend-collapse-container" href="#collapse-legend"><gdb:localize key="dashboardViewer.legend"/></a>
    </div>	
    
    <div id="collapse-legend" class="accordion-body" style="height: 0px;">
      <ul id="legend-list-group">
        <!-- Thematic layers  -->
        <div ng-repeat="layerId in thematicCache.ids" ng-init="layer = thematicCache.values[layerId]">
          <li ng-if="layer.inLegend && layer.isActive && layer.groupedInLegend" class="legend-item legend-grouped" ng-dblclick="ctrl.detach($event, layer)">
            <div class="legend-item-element-group">
            	<p ng-if="layer.featureStrategy != 'BASICPOINT' && layer.featureStrategy != 'BASICPOLYGON'" class="legend-item-title">{{layer.layerName}}</p>
            
            	<img class="legend-image" ng-src="{{ctrl.getSrc(layer)}}" alt="{{layer.layerName}}" />
            	
            	<!-- basic feature legends have a different layout -->
           	 	<p ng-if="layer.featureStrategy == 'BASICPOINT' || layer.featureStrategy == 'BASICPOLYGON'" class="legend-item-label">{{layer.layerName}}</p>
          	</div>
          </li>
        </div>

        <!-- Reference layers  -->        
        <div ng-repeat="layerId in referenceCache.ids" ng-init="layer = referenceCache.values[layerId]">
          <li ng-if="layer.inLegend && layer.isActive && layer.layerExists && layer.groupedInLegend" class="legend-item legend-grouped" ng-dblclick="ctrl.detach($event, layer)">
            <div class="legend-item-element-group">
            	<p ng-if="layer.featureStrategy != 'BASICPOINT' && layer.featureStrategy != 'BASICPOLYGON'" class="legend-item-title">{{layer.layerName}}</p>
            
            	<img class="legend-image" ng-src="{{ctrl.getSrc(layer)}}" alt="{{layer.layerName}}" />
            	
            	<!-- basic feature legends have a different layout -->
           	 	<p ng-if="layer.featureStrategy == 'BASICPOINT' || layer.featureStrategy == 'BASICPOLYGON'" class="legend-item-label">{{layer.layerName}}</p>
          	</div>
          </li>        
        </div>
      </ul> 
	</div>
  </div>
</article>