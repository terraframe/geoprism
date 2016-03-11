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
<%@ taglib uri="../../WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

       
<div id="agg-level-holder" class="row-holder">
  <div class="label-holder style03">
    <strong><gdb:localize key="DashboardThematicLayer.form.defineAggMeth"/></strong>
  </div>
  <div class="holder add">
  
    <!-- Aggregation level (i.e. country, state, city, etc...) -->
    <div class="box">
      <label><gdb:localize key="DashboardThematicLayer.form.groupBy"/></label>
      <styled-select options="dynamicDataModel.aggregationStrategyOptions" model="dynamicDataModel.aggregationStrategy" class="method-select"></styled-select>
    </div>
    
    <!-- Aggregation method (i.e. sum, max, min, majority, minority) -->
    <div class="box" ng-show="ctrl.showAggregationMethods()">
      <label><gdb:localize key="DashboardThematicLayer.form.accordingTo"/></label>
      <styled-select options="dynamicDataModel.aggregationMethods" model="layerModel.aggregationType" value="method" class="method-select"></styled-select>
    </div>  
  </div>
</div>
