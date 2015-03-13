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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<gdb:localize var="page_title" key="dashboardViewer.title"/>

<!-- Dynamic map CSS -->
<jwr:style src="/com/runwaysdk/geodashboard/report/ReportTable.css" useRandomParam="false"/>  
<jwr:style src="/com/runwaysdk/geodashboard/ontology/TermTree.css" useRandomParam="false"/>  
<jwr:style src="/bundles/dynamic-map.css" useRandomParam="false" />

<!-- Dynamic map Javascript -->
<jwr:script src="/bundles/dynamic-map.js" useRandomParam="false"/> 
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
<jwr:script src="/bundles/ontology.js" useRandomParam="false"/>

<script type="text/javascript">${js}</script>

<!-- Google maps API -->
<script src="https://maps.google.com/maps/api/js?v=3&amp;sensor=false"></script>

<script type="text/javascript">

$(document).ready(function(){
  
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  // Render the map
  var map = new GDB.gis.DynamicMap({    
    mapDivId: "mapDivId",    
    mapId: '${mapId}',
    dashboardId : '${dashboardId}',    
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

    <form action="#" class="control-form" id="control-form">
      <fieldset>
        <legend class="none"><gdb:localize key="dashboardViewer.controlForm"/></legend>
        <button class="none"><gdb:localize key="dashboardViewer.save"/></button>
        
		<!-- Overlay Layers Panel -->
        <article class="accordion info-box" id="overlay-container">
            <div class="accordion-group sales-accortion">
              <div class="accordion-heading">
                <a class="map-layers-opener opener" id="overlay-opener-button" data-toggle="collapse" data-parent="#overlay-container" href="#collapse-overlay"><gdb:localize key="dashboardViewer.mapLayers"/></a>
              </div>
              <div id="collapse-overlay" class="accordion-body" style="height: 0px;">
                <div class="accordion-inner holder" id="overlayLayerContainer"></div>
              </div>
            </div>
        </article>

		<!-- Base Layers Panel -->     
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
      </fieldset>
    </form>
    
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
	        <gdb:localize key="dashboard.entity.label" var="dashboardEntityLabel"/>
	        
		    <input class="gdb-attr-filter filter-geo" id="filter-geo" type="text" placeholder="${dashboardEntityLabel}"></input>
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
                           	<c:choose>
                            	<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDouble' || attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeInteger'}">
                                	<!-- When attribute any type other than Term or Text add the map it button -->
                                    <a href="#" class="opener attributeLayer" data-toggle="tooltip" data-original-title="New map layer" data-placement="left" data-id="${attr.mdAttributeId}">
                                    	<!-- <span data-toggle="modal" data-target="#modal01">map it</span> -->  <!-- This code calls modal.show() on this element when its clicked on. We can't have it doing that because the modal needs to be shown after a controller request returns. -->
                                        <span><gdb:localize var="map_it" key="dashboardViewer.mapIt"/>${map_it}</span>
                                    </a>
                                </c:when>
                                <c:otherwise>
                            	</c:otherwise>
                        	</c:choose>
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
		                              <gdb:localize key="dashboard.number.label" var="dashboardNumberLabel"/>
		                              
		                              <c:if test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeDouble'}">
		                                <input class="gdb-attr-filter filter-number numbers-only" id="filter-number-${attr.mdAttributeId}" type="text" placeholder="${dashboardNumberLabel}"></input>
		                              </c:if>
		                              <c:if test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeInteger'}">
		                                <input class="gdb-attr-filter filter-number integers-only" id="filter-number-${attr.mdAttributeId}" type="text" placeholder="${dashboardNumberLabel}"></input>
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
		                              <gdb:localize key="dashboard.text.label" var="dashboardTextLabel"/>
		                              
		                              <label for=filter-char-${attr.mdAttributeId} class="none"><gdb:localize key="dashboardViewer.text"/></label>
		                              <input class="gdb-attr-filter filter-char" id="filter-char-${attr.mdAttributeId}" type="text" placeholder="${dashboardTextLabel}"></input>
		                            </div>
								</c:when>
								<c:when test="${attr.attributeType == 'com.runwaysdk.system.metadata.MdAttributeTerm'}">
									<!-- Term attribute -->
                                    <div class="gdb-attr-filter filter-term" id="${attr.mdAttributeId}"></div>
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
<!-- 
  	<h4 id="reporting-toggle-button"><gdb:localize key="dashboardViewer.chartPanel"/></h4>
 -->  
    <div id="report-toolbar">
      <span><a href="#" id="report-max"><gdb:localize key="dashboardViewer.max"/></a>
      <a href="#" id="report-split"><gdb:localize key="dashboardViewer.split"/></a>
      <a href="#" id="report-min"><gdb:localize key="dashboardViewer.min"/></a>
      <a href="#" id="report-upload"><gdb:localize key="dashboardViewer.upload"/></a>
      <a href="#" class="report-export" data-format="docx"><gdb:localize key="report.docx"/></a>
      <a href="#" class="report-export" data-format="xlsx"><gdb:localize key="report.xlsx"/></a>
      <a href="#" class="report-export" data-format="pdf"><gdb:localize key="report.pdf"/></a>
    </div>
    <div id="report-viewport">    
      <div id="report-content">
      </div>   
    </div>
  </article>
  
  <!-- allow a user to go to the top of the page -->
  <div class="skip">
    <a href="#wrapper"><gdb:localize key="dashboardViewer.backToTop"/></a>
  </div>
  
</body>

<!-- Dialog for cloning a dashboard  -->
<div id="clone-container"></div>  

</html>