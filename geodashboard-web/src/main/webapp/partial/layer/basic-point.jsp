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

<div class="tab-pane" id="tab001basicpoint" ng-class="{ 'active' : layerModel.layerType == 'BASICPOINT' }" >
       
  <!-- BASIC FILL -->
  <style-basic-fill fill="styleModel.pointFill" opacity="styleModel.pointOpacity"></style-basic-fill>
      
  <!-- BASIC STROKE -->
  <style-stroke class="stroke-block" stroke="styleModel.pointStroke" stroke-width="styleModel.pointStrokeWidth" stroke-opacity="styleModel.pointStrokeOpacity"></style-stroke>
      
  <!-- BASIC SHAPE -->
  <div class="fill-block">
    <strong class="title"><gdb:localize key="DashboardThematicLayer.form.shapeHeading"/></strong>
    <div class="cell-holder">
      <div class="cell">
        <label for="basic-point-radius-select"><gdb:localize key="DashboardLayer.form.size"/></label>
        <div class="text">
          <input id="basic-point-radius-select" name="style.basicPointSize" type="text" ng-model="styleModel.basicPointSize" placeholder="{{styleModel.basicPointSize}}"></input>
        </div>
      </div>
    </div>
        
    <div id="point-type-container" class="cell">
      <label for="point-type"><gdb:localize key="DashboardLayer.form.pointType"/></label>
      <styled-basic-select options="dynamicDataModel.pointTypes" model="styleModel.pointWellKnownName" class="method-select"></styled-basic-select>            
    </div>
  </div>
</div>
