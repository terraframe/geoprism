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

<article class="accordion info-box" id="base-map-container">
  <div class="accordion-group sales-accortion">
    <div class="accordion-heading">
      <a class="map-layers-opener opener" data-toggle="collapse" data-parent="#base-map-container" href="#collapse-base-maps"><gdb:localize key="dashboardViewer.baseMaps"/></a>
    </div>
    <div id="collapse-base-maps" class="accordion-body" style="height: 0px;">
      <div class="accordion-inner holder" id="baseLayerContainer">
        <div class="checkbox-container" ng-repeat='layer in layers'>
          <div class="row-form jcf-class-check chk-area com-runwaysdk-ui-factory-runway-checkbox-CheckBox com-runwaysdk-ui-factory-runway-Widget" ng-class="{'checked' : layer.isActive}" ng-click="ctrl.toggle(layer.layerId)"></div>
          <label class="checkbox-label">{{layer.layerLabel}}</label>
        </div>      
      </div>
    </div>
  </div>
</article>
