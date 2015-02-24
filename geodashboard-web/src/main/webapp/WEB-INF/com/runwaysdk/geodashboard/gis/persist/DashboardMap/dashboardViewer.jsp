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
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardNotEqualDTO"%>
<%@page import="com.runwaysdk.geodashboard.ontology.ClassifierDisplayLabelDTO"%>
<%@page import="com.runwaysdk.geodashboard.ontology.ClassifierDTO"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="com.runwaysdk.geodashboard.DashboardController"%>
<%@page import="com.runwaysdk.geodashboard.DashboardDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardLayerDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardLayerViewDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanOrEqualDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardLessThanDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardGreaterThanOrEqualDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.DashboardEqualDTO"%>
<%@page import="com.runwaysdk.geodashboard.gis.persist.condition.LocationConditionDTO"%>
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
        DashboardGreaterThanDTO.CLASS, DashboardGreaterThanOrEqualDTO.CLASS, DashboardLessThanDTO.CLASS,
        DashboardLessThanOrEqualDTO.CLASS, DashboardEqualDTO.CLASS, DashboardNotEqualDTO.CLASS, DashboardController.CLASS,
        DashboardDTO.CLASS, LocationConditionDTO.CLASS, GeoEntityDTO.CLASS, LocatedInDTO.CLASS, ReportItemController.CLASS,
        ClassifierDTO.CLASS, ClassifierDisplayLabelDTO.CLASS
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

<!-- JQuery -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Factory.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Dialog.js"></script>

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
  <aside class="aside animated legend-snapable" id="dashboardMetadata">
		<div class="nav-bar">
		    <!-- Clone dashboard button -->
		    <span id="clone-dashboard" class="pull-left">
              <a href="#" class="opener glyphicon glyphicon-plus clone-dashboard" data-toggle="tooltip" data-original-title="Clone dashboard" data-placement="left" data-id="clone-dashboard"></a>
		    </span>		
		    
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
		
	    <!-- Global geo filter -->
	    <div class="filter-block">	    
	      <div class="row-holder">
	        <label for="filter-geo"><gdb:localize key="filter.geo"/></label>
	      </div>	    
	      <div class="geo">
		    <input class="gdb-attr-filter filter-geo" id="filter-geo" type="text" placeholder="Geo entity"></input>
		    <input id="filter-geo-hidden" type="hidden"></input>		    
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
	                   		<%-- This <c:when> check is only needed until ontology and text attributes are supported in layer aggregation --%>
<%--                            	<c:choose> --%>
<%--                             	<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDouble' || attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeInteger' || attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDate'}"> --%>
                                	<!-- When attribute any type other than Term or Text add the map it button -->
                                    <a href="#" class="opener attributeLayer" data-toggle="tooltip" data-original-title="New map layer" data-placement="left" data-id="${attr.mdAttributeId}">
                                    	<!-- <span data-toggle="modal" data-target="#modal01">map it</span> -->  <!-- This code calls modal.show() on this element when its clicked on. We can't have it doing that because the modal needs to be shown after a controller request returns. -->
                                        <span><gdb:localize var="map_it" key="dashboardViewer.mapIt"/>${map_it}</span>
                                    </a>
<%--                                     	</c:when> --%>
<%--                                 	<c:otherwise> --%>
<%--                             		</c:otherwise> --%>
<%--                         	</c:choose> --%>
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
								<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDouble' || attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeInteger'}">
									<!-- Number attribute -->
		                            <div class="select-holder">
		                              <select id="filter-opts-${attr.mdAttributeId}" class="filter-select">
		                                <option value="gt">&gt;</option>
		                                <option value="ge">&gt;=</option>
		                                <option value="lt">&lt;</option>
		                                <option value="le">&lt;=</option>
		                              </select>
		                            </div>
		                            <div class="text">
		                              <label for=filter-number-${attr.mdAttributeId} class="none"><gdb:localize key="dashboardViewer.number"/></label>
		                              <c:if test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDouble'}">
		                                <input class="gdb-attr-filter filter-number numbers-only" id="filter-number-${attr.mdAttributeId}" type="text" placeholder="Number"></input>
		                              </c:if>
		                              <c:if test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeInteger'}">
		                                <input class="gdb-attr-filter filter-number integers-only" id="filter-number-${attr.mdAttributeId}" type="text" placeholder="Number"></input>
		                              </c:if>
		                            </div>
								</c:when>
								<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDate'}">
									<!-- Date attribute -->
									<div class="data-block">
										<div class="col">
											<label for="filter-from-${attr.mdAttributeId}"><gdb:localize key="dashboardViewer.dates.from" /></label>
											<span class="data-text"> 
												<input class="checkin gdb-attr-filter filter-date" id="filter-from-${attr.mdAttributeId}" type="text" placeholder="" />
												<a href="#" class="datapicker-opener"></a>
											</span>
										</div>
										<div class="col">
											<label for="filter-to-${attr.mdAttributeId}"><gdb:localize key="dashboardViewer.dates.to" /></label>
											<span class="data-text"> 
												<input class="checkout gdb-attr-filter filter-date" id="filter-to-${attr.mdAttributeId}" type="text" placeholder="" />
												<a href="#" class="datapicker-opener"></a>
											</span>
										</div>
									</div>
								</c:when>
								<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeCharacter'}">
									<!-- Character attribute -->
		                            <div class="select-holder">
		                              <select id="filter-opts-${attr.mdAttributeId}" class="filter-select">
		                                <option value="eq">=</option>
		                                <option value="neq">!=</option>
		                              </select>
		                            </div>
		                            <div class="text">
		                              <label for=filter-char-${attr.mdAttributeId} class="none"><gdb:localize key="dashboardViewer.text"/></label>
		                              <input class="gdb-attr-filter filter-char" id="filter-char-${attr.mdAttributeId}" type="text" placeholder="Text"></input>
		                            </div>
								</c:when>
								<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeTerm'}">
									<!-- Term attribute -->
		                            <div class="text">
		                              <label for=filter-term-${attr.mdAttributeId} class="none"><gdb:localize key="dashboardViewer.text"/></label>
		                              <input class="gdb-attr-filter filter-term" id="filter-term-${attr.mdAttributeId}" type="text" placeholder="Term"></input>
		                              <input id="filter-hidden-${attr.mdAttributeId}" type="hidden"></input>
		                            </div>
								</c:when>
								<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeBoolean'}">
									<!-- Boolean attribute -->
		                            <div class="checks-frame">
		                              <div>
		                                <input class="gdb-attr-filter filter-boolean" id="${attr.mdAttributeId}-true" name="filter-${attr.mdAttributeId}" type="radio" value="true">
		                                <label for="${attr.mdAttributeId}-true"><gdb:localize key="filter.true" /></label>
		                              </div>
		                              <div>
  		                                <input class="gdb-attr-filter filter-boolean" id="${attr.mdAttributeId}-false" name="filter-${attr.mdAttributeId}" type="radio" value="false">
		                                <label for="${attr.mdAttributeId}-false"><gdb:localize key="filter.false" /></label>
		                              </div>
		                            </div>
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
  <article id="reporticng-container" class="reporticng-container report-panel-closed">
  	<h4 id="reporting-toggle-button"><gdb:localize key="dashboardViewer.chartPanel"/></h4>
    <div id="report-content"></div>
  </article>
  
  <!-- allow a user to go to the top of the page -->
  <div class="skip">
    <a href="#wrapper"><gdb:localize key="dashboardViewer.backToTop"/></a>
  </div>
  
</body>

<!-- Dialog for cloning a dashboard  -->
<div id="clone-container"></div>  

</html>
