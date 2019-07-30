<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<aside class="aside animated legend-snapable" ng-class="{'expanded' : ctrl.expanded}" id="dashboardMetadata" ng-cloak> 
  <div id="data-panel-toggle-container">
    <i id="data-panel-expand-toggle" ng-click="ctrl.toggle()" class="fa" ng-class="{'fa-angle-double-right' : ctrl.expanded, 'fa-angle-double-left' : !ctrl.expanded}"></i>
  </div>
      
  <div class="nav-bar sidebar-nav-bar">
   		<ul class="nav">
   			<li id="dashboard-dropdown" class="sales-menu dropdown ng-cloak">
		      <a href="#" class="link-opener dropdown-toggle active" data-toggle="dropdown">{{dashboard.model.label}}</a>
		      <ul id="gdb-dashboard-dropdown-menu" class="dropdown-menu" role="menu" aria-labelledby="sales-dropdown">
		        <li ng-repeat="da in dashboard.dashboards | orderBy:'label'">
		          <a ng-if="dashboard.dashboardId != da.dashboardId" ng-click="dashboard.setDashboardId(da.dashboardId)">{{da.label}}</a>
		        </li>
		      </ul>
		    </li>
		    <li class="sidebar-nav-bar-btn">  
		    	<i ng-click="dashboard.openDashboard()" class="fa fa-external-link ico-dashboard-tab" title="<gdb:localize key='dashboardViewer.newDashboardTabTooltip'/>" ></i> 
		    </li>
		    <li class="sidebar-nav-bar-btn">
		   		<i ng-if="dashboard.canEdit()" ng-click="dashboard.editOptions()" id="dashboard-options-btn" class="fa fa-cog ico-dashboard-options" title="<gdb:localize key='dashboardViewer.dashboardOptionsTooltip'/>" ></i>
		 	</li>
		 	
   			<li class="dropdown navigation-dropdown">      
   				<a href="#" class="fa fa-bars opener-drop dropdown-toggle dropdown-toggle-compact pull-right" ></a>
	    		<ul class="dropdown-menu navigation-menu pull-right">
	    			<a href="${pageContext.request.contextPath}/prism/home">
	       				<li>
	       					<i class="fa fa-home"></i> 
	       					<p ><gdb:localize key="dashboardViewer.userMenuLinkLabel"/></p>
	       				</li>
       				</a>
       				<a href="${pageContext.request.contextPath}/nav/kaleidoscopes">
	       				<li>
	       					<i class="fa fa-th"></i> 
	       					<p><gdb:localize key="dashboardViewer.userDashboardsLinkLabel"/></p>
	       				</li>
	       			</a>
	       			<a href="${pageContext.request.contextPath}/prism/management#/datasets">
	           			<li ng-if="dashboard.canEdit()">
	           				<i class="fa fa-table"></i>
	           				<p><gdb:localize key="dashboardViewer.dataManagementLinkLabel"/></p>
	           			</li>
           			</a>
	       			<a href="${pageContext.request.contextPath}/prism/admin">
	           			<li ng-if="dashboard.canEdit()">
	           				<i class="fa fa-lock"></i>
	           				<p><gdb:localize key="dashboardViewer.administrationLinkLabel"/></p>
	           			</li>
           			</a>
       			</ul>
       		</li>
       	</ul>
  </div>
    
  <ng-form name="form">    
    <!-- Global geo filter -->
    <location-filter filter="dashboard.model.location" dashboard-id="dashboard.dashboardId"></location-filter>

    <type-accordion types="dashboard.model.types" new-layer="dashboard.newLayer(mdAttributeId)"></type-accordion>                                                  
    <div id="filter-buttons-container">
      <a id="refreshFilterIco" href="#" ng-click="form.$invalid || dashboard.refresh('refreshFilterIco', false)" ng-disabled="form.$invalid" ng-class="{'fa-spin' : dashboard.icoSpin == 'refreshFilterIco' }" class="fa fa-refresh filters-button apply-filters-button" title="<gdb:localize key="dashboardViewer.applyFiltersTooltip"/>" data-placement="left""></a>
      <a href="#" ng-click="form.$invalid || dashboard.save(false, 'saveToMapIco')" ng-disabled="form.$invalid" class="fa fa-floppy-o filters-button save-filters-button" title="<gdb:localize key="dashboardViewer.saveFiltersTooltip"/>" data-placement="left"">
      	<i ng-show="dashboard.icoSpin == 'saveToMapIco'" class="fa fa-refresh fa-spin" style="position: absolute; right: 0px; bottom: 10px; font-size: 23px; color:#bababa; border-radius: 25px; background-color: rgba(66,66,66,0.9);"></i>
      </a>
      <a ng-if="dashboard.canEdit()" href="#" ng-click="form.$invalid || dashboard.save(true, 'saveToDashboardIco')"  ng-disabled="form.$invalid" class="icon-dashboard-icons filters-button save-global-filters-button" title="<gdb:localize key="dashboardViewer.saveGlobalFiltersTooltip"/>">
      	<i ng-show="dashboard.icoSpin == 'saveToDashboardIco'" class="fa fa-refresh fa-spin" style="position: absolute; right: 10px; bottom: 10px; font-size: 23px; color:#bababa; border-radius: 25px; background-color: rgba(66,66,66,0.9);"></i>
      </a>
    </div>

  </ng-form>
</aside>