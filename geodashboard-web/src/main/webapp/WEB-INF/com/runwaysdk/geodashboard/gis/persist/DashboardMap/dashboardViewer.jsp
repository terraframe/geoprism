<%--

    Copyright (c) 2013 TerraFrame, Inc. All rights reserved.

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

<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="com.runwaysdk.geodashboard.DashboardController"%>
<%@page import="com.runwaysdk.geodashboard.DashboardDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardLayerViewDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThan"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqual"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThan"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqual"%>
<%@page import="com.runwaysdk.geodashboard.report.ReportItemController"%>
<%@page import="com.runwaysdk.system.gis.geo.GeoEntityDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.LocatedInDTO" %>
<%@page import="com.runwaysdk.constants.DeployProperties"%>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO" %>

<%
  String webappRoot = request.getContextPath() + "/";
%>

<%
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>

<gdb:localize var="page_title" key="dashboardViewer.title"/>

<script type="text/javascript">
<%// use a try catch before printing out the definitions, otherwise, if an
  // error occurs here, javascript spills onto the actual page (ugly!)
  try
  {
    String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] {
        DashboardMapDTO.CLASS, DashboardLayerDTO.CLASS, DashboardLayerViewDTO.CLASS, DashboardLayerController.CLASS,
        DashboardGreaterThan.CLASS, DashboardGreaterThanOrEqual.CLASS, DashboardLessThan.CLASS, DashboardLessThanOrEqual.CLASS,
        DashboardController.CLASS, DashboardDTO.CLASS, GeoEntityDTO.CLASS, LocatedInDTO.CLASS, ReportItemController.CLASS
      }, true);
    out.print(js);
  }
  catch(Exception e)
  {
    // perform cleanup
    throw e;
  }

  /* doIt(request, out); */%>
</script>

<script type="text/javascript" src="<% out.print(webappRoot); %>jquery/datatables/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="<% out.print(webappRoot); %>jquery/datatables/css/jquery.dataTables.css" ></link>
<link rel="stylesheet" href="<% out.print(webappRoot); %>jquery/datatables/css/jquery.dataTables_themeroller.css" ></link>

<!-- Runway Factory -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/runway.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/widget/Widget.js"></script>
<script type="text/javascript" src="<%out.print(webappRoot);%>com/runwaysdk/ui/factory/runway/checkbox/CheckBox.js"></script>

<!-- Generic -->

<!-- JQuery -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Factory.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Dialog.js"></script>

<!-- Runway Generic -->

<!-- Geodashboard -->
<script type="text/javascript" src="<%out.print(webappRoot);%>com/runwaysdk/geodashboard/ontology/TermTree.js"></script>
<script type="text/javascript" src="<%out.print(webappRoot);%>com/runwaysdk/geodashboard/ontology/UniversalTree.js"></script>

<script type="text/javascript">

$(document).ready(function(){
  
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  // Render the map
  var map = new GDB.gis.DynamicMap({    
    mapDivId: "mapDivId",    
    mapId: '${mapId}',
    dashboardId : '<%=request.getAttribute("dashboardId")%>',    
    layerCategoriesTree: {
      termType : <% out.print("\"" + GeoEntityDTO.CLASS + "\""); %>,
      relationshipTypes : [ <% out.print("\"" + LocatedInDTO.CLASS + "\""); %> ],
      rootTerm : <% out.print("\"" + GeoEntityDTO.getRoot(clientRequest).getId() + "\""); %>,
      checkable: true
    }
    
  });
  
  map.render();
  
});

</script>

    <form action="#" class="control-form" id="control-form">
      <fieldset>
        <legend class="none"><gdb:localize key="dashboardViewer.controlForm"/></legend>
        <button class="none"><gdb:localize key="dashboardViewer.save"/></button>
        
        <!-- This will eventually need to be collapsible -->
        <article class="info-box">
          <h3><gdb:localize key="dashboardViewer.mapLayers"/></h3>
          <div id="overlayLayerContainer" class="holder"></div>
        </article>
        
        <article class="accordion info-box" id="base-map-container">
            <div class="accordion-group sales-accortion">
              <div class="accordion-heading">
                <a class="map-layers-opener opener" data-toggle="collapse" data-parent="#base-map-container" href="#collapse-base-maps"><gdb:localize key="dashboardViewer.baseMaps"/></a>
              </div>
              <div id="collapse-base-maps" class="accordion-body" style="height: 0px;">
                <div class="accordion-inner holder" id="baseLayerContainer"></div>
              </div>
            </div>
        </article>

		<!--        <ul class="scale-nav"> -->
<!--          <li><a href="#">plus</a></li> -->
<!--          <li><a href="#" class="minus">minus</a></li> -->
<!--        </ul> -->
      </fieldset>
    </form>
    
    <div class="info-box" id="legend-container-group">
<!--     	<a class="legend-opener opener" href="#collapse-legend"><b>Legend</b></a> -->
    	<div id="legend-items-container-group">
    		<ul id="legend-list-group"></ul>
    	</div>
    </div>
    
    <!-- contain aside of the page -->
  <aside class="aside animated slideInRight legend-snapable" id="dashboardMetadata">
		<div class="nav-bar">
			<a href="<%=request.getContextPath() + "/"%>" class="opener-drop" data-toggle="tooltip" data-placement="bottom" title="Menu"><gdb:localize key="dashboardViewer.opener"/></a>
			<div class="sales-menu dropdown">
				<a href="#" class="link-opener dropdown-toggle" data-toggle="dropdown" data-id="${activeDashboard.id}">${activeDashboard.displayLabel.value}</a>
				<ul id="gdb-dashboard-dropdown-menu" class="dropdown-menu" role="menu" aria-labelledby="sales-dropdown">
					<c:forEach items="${dashboards}" var="dashboard" varStatus="status">
						<li><a class="gdb-dashboard" id="${dashboard.id}">${dashboard.displayLabel.value}</a></li>
					</c:forEach>
					<c:if test="${empty dashboards}">&nbsp;</c:if>
				</ul>
			</div>
		</div>
		<button class="none">submit</button>
        
        <a href="#" class="opener new-dashboard-btn" data-toggle="tooltip" data-placement="left" data-id="${attr.mdAttributeId}">
        	<span style="color:white;font-weight:bold;"><gdb:localize key="dashboardViewer.createNewDashboard"/></span>
		    </a>
		    
        <div class="choice-form">
          
          <div class="row-holder">
            <label class="none" for="geocode"><gdb:localize key="dashboardViewer.choiceSelect"/></label>
            <span class="jcf-unselectable select-choice-select select-area select-focus" style="width: 267px;">
            <input type="text" autocomplete="off" id="geocode" name="geocode" class="choice-select">
            </span>
          </div>
          
          <!-- datapicker block -->
          <div class="data-block">
            <div class="col">
              <label for="from-field"><gdb:localize var="dates_from" key="dashboardViewer.dates.from"/>${dates_from}</label>
              <span class="text">
                <input class="checkin" id="from-field" type="text" placeholder="">
                <a href="#" class="datapicker-opener">datapicker</a>
              </span>
            </div>
            <div class="col">
              <label for="to-field"><gdb:localize var="dates_to" key="dashboardViewer.dates.to"/>${dates_to}</label>
              <span class="text">
                <input class="checkout" id="to-field" type="text" placeholder="">
                <a href="#" class="datapicker-opener">datapicker</a>
              </span>
            </div>
          </div>
        </div>
        
        <div class="sales-accortion panel-group">
	
	        <!-- 
	        TEST
	        -->
	        <c:forEach items="${types}" var="type" varStatus="typeStatus">
	          <c:choose>
	            <c:when test="${typeStatus.index == 0}">
	            
	          <div class="panel panel-default">
	            <a class="opener" data-toggle="collapse" data-parent="#accordion" href="#collapse01">${type.displayLabel.value}</a>
	            <div id="collapse01" class="panel-collapse collapse">
	              <div class="panel-body">
	              <!-- slide block -->
	              <c:forEach items="${attrMap[type.id]}" var="attr" varStatus="attrStatus">
	                <div class="panel">
	                  <h4 class="panel-title"><a class="opener-link" data-toggle="collapse" data-parent="#accordion${attrStatus.index}" href="#collapse00${attrStatus.index}">${attr.displayLabel}</a>
	                    <a href="#" class="opener attributeLayer" data-toggle="tooltip"
											  data-original-title="New map layer" data-placement="left" data-id="${attr.mdAttributeId}">
	 										  <!-- <span data-toggle="modal" data-target="#modal01">map it</span> -->  <!-- This code calls modal.show() on this element when its clicked on. We can't have it doing that because the modal needs to be shown after a controller request returns. -->
	 										  <span><gdb:localize var="map_it" key="dashboardViewer.mapIt"/>${map_it}</span>
											</a>
										</h4>
										
	                  <!-- slide block -->
	                  <div id="collapse00${attrStatus.index}" class="panel-collapse collapse">
	                    <div class="panel-body">
	                      <div class="filter-block">
	                        <div class="row-holder">
	                          <label for="f${attrStatus.index}"><gdb:localize var="dbviewer_filter" key="dashboardViewer.filter"/>${dbviewer_filter}</label>
	                        </div>
	                        <div class="row-holder">
	                          <c:choose>
													    <c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDouble'}">
														    <!-- Number attribute -->
		                            <div class="select-holder">
		                              <select id="attrFilterOpt${attrStatus.index}" class="filter-select gdb-attr-filter ${attr.mdAttributeId}">
		                                <option value="gt">&gt;</option>
		                                <option value="ge">&gt;=</option>
		                                <option value="lt">&lt;</option>
		                                <option value="le">&lt;=</option>
		                              </select>
		                            </div>
		                            <div class="text">
		                              <label for="attrFilter${attrStatus.index}" class="none"><gdb:localize key="dashboardViewer.number"/></label>
		                              <input id="attrFilter${attrStatus.index}" type="text" placeholder="Number" class="gdb-attr-filter ${attr.mdAttributeId}"></input>
		                            </div>
													    </c:when>
													    <c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDate'}">
													      <!-- Date attribute -->
                                <div class="select-holder">
                                  <select id="attrFilterOpt${attrStatus.index}" class="filter-select gdb-attr-filter ${attr.mdAttributeId}">
                                    <option value="gt">&gt;</option>
                                    <option value="ge">&gt;=</option>
                                    <option value="lt">&lt;</option>
                                    <option value="le">&lt;=</option>
                                  </select>
                                </div>
                                
                                <div class="text">
                                  <label for="attrFilter${attrStatus.index}" class="none"><gdb:localize key="dashboardViewer.number"/></label>
                                  <input id="attrFilter${attrStatus.index}" type="text" placeholder="Number" class="gdb-attr-filter ${attr.mdAttributeId}"></input>
                                </div>
                                <!--
                                <div class="text submit-form">
	                                <div class="data-block">
		                                <div class="col">
		                                  <label for="attrFilter${attrStatus.index}" class="none">date</label>
			                                <input id="attrFilter${attrStatus.index}" type="text" placeholder="" class="checkin hasDatepicker gdb-attr-filter ${attr.mdAttributeId}"></input>
			                                <a class="datapicker-opener" href="#">datapicker</a>
			                              </div>
		                              </div>
	                              </div>
	                              -->
													    </c:when>
													    <c:otherwise>
													      
													    </c:otherwise>
														</c:choose>
	                        </div>
	                      </div>
	                    </div>
	                  </div>
	                </div>
	              </c:forEach>
	            </div></div></div>
	            
	            
	            </c:when>
	            <c:otherwise>
	              
	            </c:otherwise>
	          </c:choose>
	        </c:forEach>
       </div> <!-- END sales-accortion panel-group -->
    
    <a href="#" class="opener apply-filters-button" data-toggle="tooltip" data-placement="left"">
      <span style="color:white;font-weight:bold;"><gdb:localize key="dashboardViewer.applyFilters"/></span>
    </a>
  </aside>
  
  <!-- modal -->
  <div class="modal fade" id="modal01" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
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
  <article id="reporticng-container" class="reporticng-container">
    <div id="report-content"></div>
  </article>
  
  <!-- allow a user to go to the top of the page -->
  <div class="skip">
    <a href="#wrapper"><gdb:localize key="dashboardViewer.backToTop"/></a>
  </div>
</body>
</html>
