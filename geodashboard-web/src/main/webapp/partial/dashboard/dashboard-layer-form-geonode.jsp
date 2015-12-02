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
<%@ taglib uri="../../WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
       
       
       		<div id="geonode-holder" class="row-holder" style="display:none">
              <div class="label-holder style03">
                <strong><gdb:localize var="dl_form_defineGeoNode" key="DashboardThematicLayer.form.defineGeoNode"/>${dl_form_defineGeoNode}</strong>
              </div>
              <div class="holder add">
                <div class="box">
                  <label for="geonode-select"><gdb:localize var="dl_form_geoNode" key="DashboardThematicLayer.form.geoNode"/>${dl_form_geoNode}</label>
                  <div class="select-box">
                  
                        <select id="geonode-select" class="method-select" name="layer.geoNode" ng-model="thematicLayerModel.geoNode">
                          <option ng-repeat="node in geoNodes" value="{{node.id}}" ng-selected="{{node.id == thematicLayerModel.geoNode}}">{{node.displayLabel}}</option>
                        </select>
                        
<!--                         <select id="geonode-select" class="method-select" name="layer.geoNode" ng-model="thematicLayerModel.geoNodeId" ng-options="node.displayLabel for node in geoNodes track by node.id"></select> -->
                        
                  
                  </div>
                </div>
              </div>
            </div>
