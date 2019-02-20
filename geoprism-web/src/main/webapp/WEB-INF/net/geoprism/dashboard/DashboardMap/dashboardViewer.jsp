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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
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
		<link rel="icon" href="${pageContext.request.contextPath}/logo/view?id=logo"/>
	  
	  <!-- Tell Runway what the application context path is. -->
	  <script>
	    window.com = window.com || {};
	    window.com.runwaysdk = window.com.runwaysdk || {};
	    window.com.runwaysdk.__applicationContextPath = "<%=request.getContextPath()%>";
	  </script>  
	  
	  <!-- CSS imports -->
	  <jwr:style src="/bundles/main.css" useRandomParam="false" />
	  <jwr:style src="/bundles/dashboard.css" useRandomParam="false" />
	  <jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
	  
	  <!-- Default imports -->
	  <jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
	  <jwr:script src="/bundles/main.js" useRandomParam="false"/>  
	  <jwr:script src="/bundles/widget.js" useRandomParam="false"/>  
	  <jwr:script src="/bundles/localization.js" useRandomParam="false"/>
	  
	  <!-- Dynamic map CSS -->
	  <jwr:style src="/net/geoprism/report/ReportTable.css" useRandomParam="false"/>  
	  <jwr:style src="/bundles/dynamic-map.css" useRandomParam="false" />
	  <jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
	  <jwr:style src="/bundles/termtree.css" useRandomParam="false"/>  
	  
	  <!-- Dynamic map Javascript -->
	  <jwr:script src="/bundles/termtree.js" useRandomParam="false"/>
	  <jwr:script src="/bundles/dynamic-map.js" useRandomParam="false"/> 
	  <jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
	  <jwr:script src="/bundles/ontology.js" useRandomParam="false"/>
	  
	  <script type="text/javascript">${js}</script>
	  <%-- <jwr:style src="/net/geoprism/MapConfig.json" useRandomParam="false"/>   --%>
	  <script src="${pageContext.request.contextPath}/net/geoprism/MapConfig.json"></script>
	  
	  <!-- Google maps API -->
	  <script src="https://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>  
	<!--   THE DEVELPER KEY BELOW NEEDS TO BE REPLACED WITH A CUSTOMER KEY -->
	<!--   <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.21&key=AIzaSyAwPuAIr2I9bM8P03J6zUdaA2dy7o6AsY4"></script> -->
	  
	  <script type="text/javascript" src="${pageContext.request.contextPath}/net/geoprism/Localized.js.jsp"></script>
	  
	  <jwr:script src="/bundles/dashboard.js" useRandomParam="false"/> 
	  
	    
	  <script type="text/javascript">
	  $(document).ready(function(){
	    com.runwaysdk.ui.Manager.setFactory("JQuery");
	  });
	  </script>  

  </head>

  <body ng-controller="DashboardController as dashboard" ng-init="init('${workspace}', ${editDashboard}, ${editData})">

    <map-panel dashboard="dashboard"></map-panel>
    <dashboard-panel dashboard="dashboard"></dashboard-panel>
  
    <!-- modal -->
    <!-- <div class="modal fade" id="modal01" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false"> -->
    <div class="modal fade" id="modal01" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
      <!-- Filled in by ajax call to create/edit layer -->
    </div>
  
    <!-- modal -->
    <div class="modal fade" id="dashboardModal01" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
      <!-- Filled in by ajax call to create new dashboard -->
    </div>

    <floating-legends thematic-cache="dashboard.thematicLayerCache" reference-cache="dashboard.referenceLayerCache"></floating-legends>  
  
    <!-- map container -->
    <div class="bg-stretch">
      <div id="mapDivId" class="dynamicMap">
        <map-popup ng-if="dashboard.feature != null && dashboard.feature.show" feature="dashboard.feature"></map-popup>
      </div>
    </div>
  
    <!-- reporting container -->
    <report-panel has-report="dashboard.model.hasReport"></report-panel>
  
    <!-- allow a user to go to the top of the page -->
    <div class="skip">
      <a href="#wrapper"><gdb:localize key="dashboardViewer.backToTop"/></a>
    </div>

    <!-- dashboard builder modal -->  
    <builder-dialog></builder-dialog>
    
    <uploader-dialog></uploader-dialog>
    
    <report-dialog></report-dialog>
    
    <!-- Dialog for cloning a dashboard  -->
    <div id="clone-container"></div>  
    <div id="dashboard-edit-container"></div>      
    
    <!-- thematic layer modal -->  
    <thematic-layer></thematic-layer>
    
    <!-- reference layer modal -->  
    <reference-layer></reference-layer>    
  </body>
</html>
