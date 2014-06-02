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

<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.runwaysdk.constants.DeployProperties"%>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardMapDTO" %>
<%@page import="com.runwaysdk.geodashboard.gis.persist.DashboardMap" %>

<%
  String webappRoot = request.getContextPath() + "/";
%>

<%
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>

<script type="text/javascript">
<%// use a try catch before printing out the definitions, otherwise, if an
  // error occurs here, javascript spills onto the actual page (ugly!)
  try
  {
    String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] {
      DashboardMapDTO.CLASS, DashboardLayerController.CLASS
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

<script type="text/javascript">

$(document).ready(function(){
  
  // Render the map
  var map = new GDB.gis.DynamicMap("mapDivId", '${mapId}');
  
  map.render();
  
});

</script>

    <form action="#" class="control-form">
      <fieldset>
        <legend class="none">controls form</legend>
        <button class="none">save</button>
        
        <!-- This will eventually need to be collapsible -->
        <article class="info-box">
          <h3>Map Layers</h3>
          <div id="overlayLayerContainer" class="holder"></div>
        </article>
        
          
        <article class="accordion info-box" id="base-map-container">
            <div class="accordion-group sales-accortion">
              <div class="accordion-heading">
                <a class="map-layers-opener opener collapsed" data-toggle="collapse" data-parent="#base-map-container" href="#collapse-base-maps"> Base Maps </a>
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
    <!-- contain aside of the page -->
    <aside class="aside animated slideInRight">
      <div class="nav-bar">
        <a href="#" class="opener-drop" data-toggle="tooltip" data-placement="bottom" title="Menu">opener</a>
        <div class="sales-menu dropdown">
          
          <c:forEach items="${dashboards}" var="dashboard" varStatus="status">
            <c:choose>
              <c:when test="${status.index == 0}">
              
                <a href="#" class="link-opener dropdown-toggle" id="sales-dropdown" data-toggle="dropdown" data-id="${dashboard.id}">${dashboard.displayLabel.value}</a>
                <ul class="dropdown-menu" role="menu" aria-labelledby="sales-dropdown">
              
              </c:when>
              <c:otherwise>
                <li><a>${dashboard.displayLabel.value}</a></li>
              </c:otherwise>
            </c:choose>
          </c:forEach>
        
          </ul>
        
        </div>
      </div>
        <button class="none">submit</button>
        <div class="choice-form">
          <div class="row-holder">
            <input type="text" autocomplete="off" id="geocode" name="geocode" class="jcf-unselectable select-choice-select select-area select-focus" style="width: 267px;" />
          </div>
          <!-- datapicker block -->
          <div class="data-block">
            <div class="col">
              <label for="from-field">From</label>
              <span class="text">
                <input class="checkin" id="from-field" type="text" placeholder="">
                <a href="#" class="datapicker-opener">datapicker</a>
              </span>
            </div>
            <div class="col">
              <label for="to-field">To</label>
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
										  data-original-title="New map layer" data-placement="left" data-id="${attr.id}">
										  <span data-toggle="modal" data-target="#modal01">opener</span>
										</a>
									</h4>
                  <!-- slide block -->
                  <div id="collapse00${attrStatus.index}" class="panel-collapse collapse">
                    <div class="panel-body">
                      <div class="filter-block">
                        <div class="row-holder">
                          <label for="f${attrStatus.index}">Filter</label>
                        </div>
                        <div class="row-holder">
                          <!-- Number attribute -->
                          <div class="select-holder">
                            <select id="f${attrStatus.index}" class="filter-select">
                              <option>&gt;</option>
                              <option>&gt;=</option>
                              <option>&lt;</option>
                              <option>&lt;=</option>
                            </select>
                          </div>
                          <div class="text">
                            <label for="f${attrStatus.index}" class="none">number</label>
                            <input id="f${attrStatus.index}" type="text" placeholder="Number">
                          </div>
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
          

        
        </div>
    </aside>
  <!-- contain slide navigation of the page -->
  <!-- 
  <div class="slide-navigation animated slideInRight">
    <aside id="sidebar">
      <div class="widget">
        <h3>Luke Skywalker</h3>
        <ul class="links-list">
          <li><a href="#">Log out</a></li>
          <li><a href="#">Account</a></li>
          <li><a href="#" class="link-viewer">Dashboard Viewer</a></li>
        </ul>
      </div>
      <div class="widget">
        <h3 class="marked">Manage Dashboards</h3>
        <ul class="links-list">
          <li><a href="#">Annual Imunization Data</a></li>
          <li class="active"><a href="#">Q4 Sales Engagment</a></li>
          <li><a href="#">Sanitation Products by Region</a></li>
          <li><a href="#">Q4 Sales Leads</a></li>
          <li><a data-toggle="collapse" href="#collapse3">Regional Marketing Programs <span class="hidden">collapse3</span></a>
            <ul id="collapse3" class="panel-collapse collapse">
              <li><a href="#">Q4 Sales Leads</a></li>
              <li><a href="#">Regional Marketing Programs</a></li>
              <li><a href="#">New Dashboard</a></li>
            </ul>
          </li>
          <li><a href="#">New Dashboard</a></li>
        </ul>
      </div>
      <nav class="aside-nav">
        <ul>
          <li><a data-toggle="collapse" href="#collapse4">Acount Management</a>
            <div id="collapse4" class="panel-collapse collapse">
              <ul>
                <li><a href="#">User Accounts</a></li>
                <li><a href="#">Roles</a></li>
              </ul>
            </div>
          </li>
          <li><a data-toggle="collapse" href="#collapse5">Datatype Management</a>
            <div id="collapse5" class="panel-collapse collapse">
              <ul>
                <li><a href="#">Term Ontology Administration</a></li>
                <li><a href="#">Data Browser</a></li>
              </ul>
            </div>
          </li>
          <li><a href="#">Sales Force Import Manager</a></li>
          <li><a data-toggle="collapse" href="#collapse6">GIS Management</a>
            <div id="collapse6" class="panel-collapse collapse">
              <ul>
                <li><a href="#">Universal Management</a></li>
                <li><a href="#">Geoentity Maintanence</a></li>
              </ul>
            </div>
          </li>
        </ul>
      </nav>
    </aside>
  </div>
  -->
  <!-- modal -->
  <div class="modal fade" id="modal01" tabindex="-1" role="dialog" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <!-- Filled in by ajax call to create/edit layer -->
  </div>
  
  
  <!-- map container -->
  <div class="bg-stretch">
    <div id="mapDivId" class="dynamicMap"></div>
  </div>
  <!-- reporting container -->
  <article class="reporticng-container">
    <div class="box"><img src="com/runwaysdk/geodashboard/images/img02.jpg" width="250" height="250" alt="image description"></div>
    <div class="box"><img src="com/runwaysdk/geodashboard/images/img02.jpg" width="250" height="250" alt="image description"></div>
    <div class="box"><img src="com/runwaysdk/geodashboard/images/img02.jpg" width="250" height="250" alt="image description"></div>
    <div class="box"><img src="com/runwaysdk/geodashboard/images/img02.jpg" width="250" height="250" alt="image description"></div>
  </article>
  <!-- allow a user to go to the top of the page -->
  <div class="skip">
    <a href="#wrapper">Back to top</a>
  </div>
</body>
</html>