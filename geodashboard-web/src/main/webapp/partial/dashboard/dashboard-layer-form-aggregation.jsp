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
       
       
       		<div id="agg-level-holder" class="row-holder" style="display:none;">
              <div class="label-holder style03">
                <strong><gdb:localize var="dl_form_defineAggMeth" key="DashboardThematicLayer.form.defineAggMeth"/>${dl_form_defineAggMeth}</strong>
              </div>
              <div class="holder add">
              
					<!-- Aggregation level (i.e. country, state, city, etc...) -->
	                <div class="box">
	                  <label for="agg-level-dd"><gdb:localize var="dl_form_groupBy" key="DashboardThematicLayer.form.groupBy"/>${dl_form_groupBy}</label>
	                  <div class="select-box">
		                    <select id="agg-level-dd" class="method-select" name="layer.aggregationStrategy" ng-model="thematicLayerModel.aggregationStrategy">
			                	<option ng-repeat="agg in dynamicDataModel.aggregationLevelOptions" value="{{agg.value}}" ng-selected="{{agg.value == thematicLayerModel.aggregationStrategy.value}}" data-type="{{agg.type}}" data-geomTypes="{{agg.geomTypes}}">{{agg.displayLabel}}</option>
		                    </select>
	                  </div>
	                </div>
	                
	                
					<!-- Aggregation method (i.e. sum, max, min, majority, minority) -->
	                <div class="box">
	                  <label for="agg-method-dd"><gdb:localize var="dl_form_accordingTo" key="DashboardThematicLayer.form.accordingTo"/>${dl_form_accordingTo}</label>
	                  <div class="select-box">
	                    
<!-- 	                          <select ng-init="thematicLayerModel.aggregationMethod = SUM" id="agg-method-dd" class="method-select" name="layer.aggregationType"  -->
<!-- 	                          	ng-model="thematicLayerModel.aggregationMethod" ng-options="for item in dynamicDataModel.aggregationMethods track by item"></select> -->

	                        <select id="agg-method-dd" class="method-select" name="layer.aggregationType" ng-model="thematicLayerModel.aggregationMethod" >
					           	<option ng-repeat="aggMeth in dynamicDataModel.aggregationMethods" value="{{aggMeth.id}}" ng-selected="{{aggMeth.id == dynamicDataModel.aggregationMethods[0].id}}" >{{aggMeth.label}}</option>
							</select>
							
<!-- 	         			<select id="agg-method-dd" class="method-select" name="layer.aggregationType">            -->
<%-- 	                      <c:forEach items="${aggregations}" var="aggregation"> --%>
<!-- 	                         	<c:choose> -->
<%-- 	                           		<c:when test="${aggregation.displayLabel.value == activeAggregation}"> --%>
<%-- 	                             		<option value="${aggregation.enumName}" selected="selected"> --%>
<%-- 	                               			${aggregation.displayLabel.value} --%>
<!-- 	                             		</option> -->
<!-- 	                           		</c:when> -->
<!-- 	                           		<c:otherwise> -->
<%-- 	                             		<option value="${aggregation.enumName}"> --%>
<%-- 	                               			${aggregation.displayLabel.value} --%>
<!-- 	                             		</option> -->
<!-- 	                           		</c:otherwise> -->
<!-- 	                         	</c:choose> -->
<!-- 	                      </c:forEach> -->
<!-- 	                    </select> -->
	                  </div>
	                </div>
              </div>
            </div>
