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
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html ng-app="dashboard">

  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
	<meta http-equiv="pragma" content="no-cache" />

	<title><gdb:localize key="dashboardViewer.title"/></title>
	
    <!-- Tell Runway what the application context path is. -->
    <script>
      window.com = window.com || {};
      window.com.runwaysdk = window.com.runwaysdk || {};
      window.com.runwaysdk.__applicationContextPath = "<%=request.getContextPath()%>";
    </script>	
	
	<!-- CSS imports -->
	<jwr:style src="/bundles/main.css" useRandomParam="false" />
	<jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
	
	<!-- Default imports -->
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.7/angular.min.js"></script>	
	
	<jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
	<jwr:script src="/bundles/main.js" useRandomParam="false"/>  
	<jwr:script src="/bundles/widget.js" useRandomParam="false"/>	
	<jwr:script src="/bundles/localization.js" useRandomParam="false"/>
	
	<!-- Dynamic map CSS -->
	<jwr:style src="/com/runwaysdk/geodashboard/report/ReportTable.css" useRandomParam="false"/>  
	<jwr:style src="/bundles/dynamic-map.css" useRandomParam="false" />
	<jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
	<jwr:style src="/bundles/termtree.css" useRandomParam="false"/>  
	<jwr:style src="/font-awesome-font-icons/font-awesome-4.3.0/css/font-awesome.min.css" useRandomParam="false"/>  
	
	<!-- Dynamic map Javascript -->
	<jwr:script src="/bundles/termtree.js" useRandomParam="false"/>
	<jwr:script src="/bundles/dynamic-map.js" useRandomParam="false"/> 
	<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
	<jwr:script src="/bundles/ontology.js" useRandomParam="false"/>
	
	<script type="text/javascript">${js}</script>
	<%-- <jwr:style src="/com/runwaysdk/geodashboard/MapConfig.json" useRandomParam="false"/>   --%>
	<script src="/com/runwaysdk/geodashboard/MapConfig.json"></script>
	
	<!-- Google maps API -->
	<script src="https://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>	
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/Localized.js.jsp"></script>
	
	<jwr:script src="/bundles/dashboard.js" useRandomParam="false"/> 
	
		
	<script type="text/javascript">
	$(document).ready(function(){
		com.runwaysdk.ui.Manager.setFactory("JQuery"); 

		// Render the map
		var map = new GDB.gis.DynamicMap({    
			mapDivId: "mapDivId",    
			mapId: '${mapId}',
			dashboardId : '${dashboardId}',
			workspace : '${workspace}',
			aggregationMap : ${aggregationMap},
			criteria : ${conditions},
			editDashboard : ${editDashboard},
			editData : ${editData},
			layerCategoriesTree: {
				termType : '${type}',
				relationshipTypes : [ '${relationType}' ],
				rootTerm : '${rootId}',
				checkable: true
			}    
		});
		map.render();
	});
	</script>	

  </head>

  <body ng-controller="DashboardController as dashboard" ng-init="init('${dashboardId}', '${workspace}')">

    <form action="#" class="control-form" id="control-form">
    <div id="control-form-collapse-button">
    	<i class="fa fa-angle-double-left toggle-left expanded"></i>
    </div>
      <fieldset>
        <legend class="none"><gdb:localize key="dashboardViewer.controlForm"/></legend>
        <button class="none"><gdb:localize key="dashboardViewer.save"/></button>
        
		<!-- Overlay Layers Panel -->
		<thematic-layers cache="dashboard.thematicLayerCache" dashboard="dashboard" ></thematic-layers>
        
       	<!-- Reference Layer Panel -->       
		<reference-layers cache="dashboard.referenceLayerCache" dashboard="dashboard" ></reference-layers>

		<!-- Base Layers Panel -->     
		<base-layers layers="dashboard.baseLayers" dashboard="dashboard" ></base-layers>
        
		<!-- Legend Panel -->       
        <article class="accordion info-box" id="legend-collapse-container">
        	<div class="accordion-group sales-accortion" id="legend-sub-container">
        		<div class="accordion-heading">
        			<a class="legend-opener opener" id="legend-opener-button" data-toggle="collapse" data-parent="#legend-collapse-container" href="#collapse-legend"><gdb:localize key="dashboardViewer.legend"/></a>
        		</div>	
              	<div id="collapse-legend" class="accordion-body" style="height: 0px;">
              		<ul id="legend-list-group"></ul> 
			    </div>
		    </div>
		</article>
		 
        <!-- Map Tools Panel -->       
        <article class="accordion info-box" id="map-tools-collapse-container">
          <div class="accordion-group sales-accortion" id="map-tools-sub-container">
            <div id="collapse-map-tools" class="accordion-body">
              <div class="accordion-inner holder" id="mapToolsContainer">
                <a id="map-export-btn" class="fa fa-file-image-o map-tool-icon" download="map.png" title="<gdb:localize key='dashboardViewer.exportMapTooltip'/>" ></a>
                <a ng-click="dashboard.centerMap()" class="fa fa-arrows-alt map-tool-icon" title="<gdb:localize key='dashboardViewer.zoomMapToExtentTooltip'/>" ></a>
              </div>              
            </div>
          </div>
        </article>		 
      </fieldset>
    </form>
    
    <!-- contain aside of the page -->
  <aside class="aside animated legend-snapable expanded" id="dashboardMetadata">
        <div id="data-panel-toggle-container">
      		<i id="data-panel-expand-toggle" class="fa fa-angle-double-right"></i>
      	</div>
		<div class="nav-bar">
<%-- 		    <a href="<%=request.getContextPath() + "/menu"%>" class="fa fa-bars opener-drop" data-toggle="tooltip" data-placement="bottom" title="Menu"><gdb:localize key="dashboardViewer.opener"/></a> --%>
			<div id="dashboard-dropdown" class="sales-menu dropdown">
				<a href="#" class="link-opener dropdown-toggle active" data-toggle="dropdown" data-id="${activeDashboard.id}">${activeDashboard.displayLabel.value}</a>
				<ul id="gdb-dashboard-dropdown-menu" class="dropdown-menu" role="menu" aria-labelledby="sales-dropdown">
					<c:forEach items="${dashboards}" var="dashboard" varStatus="status">
						<li><a class="gdb-dashboard" id="${dashboard.id}">${dashboard.displayLabel.value}</a></li>
					</c:forEach>
					<c:if test="${empty dashboards}">&nbsp;</c:if>
				</ul>
			</div>
			
			<i class="fa fa-external-link ico-dashboard-tab" title="<gdb:localize key='dashboardViewer.newDashboardTabTooltip'/>" ></i> 
			
			<!-- Clone dashboard button -->
			<span id="clone-dashboard" class="" ng-if="dashboard.model.editDashboard">
				<i class="fa fa-plus ico-dashboard" title="<gdb:localize key='dashboardViewer.newDashboardTooltip'/>" ></i>
			</span>
			
			<span id="delete-dashboard" class="" ng-if="dashboard.model.editDashboard">
				<i class="fa fa-minus ico-dashboard" title="<gdb:localize key='dashboardViewer.deleteDashboardTooltip'/>" ></i>
			</span>
			
			<i  ng-if="dashboard.model.editDashboard" id="dashboard-options-btn" class="fa fa-cog ico-dashboard-options" title="<gdb:localize key='dashboardViewer.dashboardOptionsTooltip'/>" ></i>
  		    
  		    <a href="<%=request.getContextPath() + "/menu"%>" class="fa fa-bars opener-drop pull-right" data-toggle="tooltip" data-placement="bottom" title="Menu"></a>
		</div>
		
	  <ng-form name="form">		
	    <!-- Global geo filter -->
	    <location-filter filter="dashboard.model.location" dashboard="dashboard.dashboardId"></location-filter>
		                                        
        <div class="sales-accortion panel-group" id="type-accordion">
          <type-accordion types="dashboard.model.types" edittable="dashboard.model.editDashboard" new-layer="dashboard.newLayer(mdAttributeId)"></type-accordion>  
        </div> <!-- END sales-accortion panel-group -->
    
	    <div id="filter-buttons-container">
	      <a href="#" ng-click="form.$invalid || dashboard.refresh()" ng-disabled="form.$invalid" class="fa fa-refresh filters-button apply-filters-button" title="<gdb:localize key="dashboardViewer.applyFiltersTooltip"/>" data-placement="left""></a>
	      <a href="#" ng-click="form.$invalid || dashboard.save()" ng-disabled="form.$invalid" class="fa fa-floppy-o filters-button save-filters-button" title="<gdb:localize key="dashboardViewer.saveFiltersTooltip"/>" data-placement="left""></a>
  	      <a href="#" ng-click="form.$invalid || dashboard.saveGlobal()"  ng-disabled="form.$invalid" ng-if="dashboard.model.editDashboard" class="icon-dashboard-icons filters-button save-global-filters-button" title="<gdb:localize key="dashboardViewer.saveGlobalFiltersTooltip"/>"></a>
	    </div>
	  </ng-form>
  </aside>
  
  <!-- modal -->
  <!-- <div class="modal fade" id="modal01" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false"> -->
  <div class="modal fade" id="modal01" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <!-- Filled in by ajax call to create/edit layer -->
  </div>
  
  <!-- modal -->
  <div class="modal fade" id="dashboardModal01" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <!-- Filled in by ajax call to create new dashboard -->
  </div>
  
  
  <!-- map container -->
  <div class="bg-stretch">
    <div id="mapDivId" class="dynamicMap"></div>
  </div>
  
  <!-- reporting container -->
  <article id="reporticng-container" class="reporticng-container report-panel-closed">
    <c:if test="${editDashboard || hasReport}">
      <div id="report-toolbar">
        <div id="report-toggle-container">
        	<i id="report-collapse-toggle" class="fa fa-angle-double-down report-height-toggle" style="display:none;"></i>
      		<i id="report-expand-toggle" class="fa fa-angle-double-up report-height-toggle"></i>
      	</div>
      
        <c:if test="${editDashboard}">
          <a href="#" id="report-upload" title="<gdb:localize key='dashboardViewer.uploadReportTooltip'/>" ><gdb:localize key="dashboardViewer.upload"/></a>
        </c:if>
      
        <c:choose>
          <c:when test="${!hasReport}">
            <span id="report-export-container" style="display: none;">
          </c:when>      
          <c:otherwise>
            <span id="report-export-container">
          </c:otherwise>       
        </c:choose>
      
          <a href="#" class="report-export" data-format="docx" title="<gdb:localize key='dashboardViewer.exportReportTooltip'/> as docx" ><gdb:localize key="report.docx"/></a>
          <a href="#" class="report-export" data-format="xlsx" title="<gdb:localize key='dashboardViewer.exportReportTooltip'/> as xlsx" ><gdb:localize key="report.xlsx"/></a>
          <a href="#" class="report-export" data-format="pdf" title="<gdb:localize key='dashboardViewer.exportReportTooltip'/> as pdf" ><gdb:localize key="report.pdf"/></a>
        </span>
      </div>
      <div id="report-viewport">    
        <div id="report-content">
        </div>   
      </div>
    </c:if>
  </article>
  
  <!-- allow a user to go to the top of the page -->
  <div class="skip">
    <a href="#wrapper"><gdb:localize key="dashboardViewer.backToTop"/></a>
  </div>
  
</body>

<!-- Dialog for cloning a dashboard  -->
<div id="clone-container"></div>  
<div id="dashboard-edit-container"></div>  

</html>
