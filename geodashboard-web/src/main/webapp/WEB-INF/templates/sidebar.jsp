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

<%@page import="java.util.List" %>
<%@page import="com.runwaysdk.web.WebClientSession" %>
<%@page import="com.runwaysdk.constants.ClientConstants" %>
<%@page import="com.runwaysdk.geodashboard.sidebar.XMLMenuProvider" %>
<%@page import="com.runwaysdk.geodashboard.sidebar.MenuItem" %>
<%@page import="com.runwaysdk.geodashboard.sidebar.ActivePageWriter" %>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";

  ActivePageWriter writer = new ActivePageWriter(request, out);
%>

<!-- BEGIN Generated Bootstrap Sidebar Menu BEGIN -->
<aside id="sidebar">
	<!-- Account Info -->
	<div class="widget">
	  <h3><% 
	  try {
	    out.print(((WebClientSession)session.getAttribute(ClientConstants.CLIENTSESSION)).getRequest().getSessionUser().getValue("username"));
	  }
	  catch (Throwable t) {
	    out.print("Anonymous");
	  }
	  %></h3>
	  <ul class="links-list">
	    <% writer.writeLiA("Log out", "session/logout"); %>
	    <% writer.writeLiA("Account", "admin/account"); %>
	    <% writer.writeLiA("Dashboard Viewer", "DashboardViewer", "link-viewer"); %>
	  </ul>
	</div>
	<!-- Dashboards -->
	<!--
	<div class="widget">
	  <h3 class="marked">Manage Dashboards</h3>
	  <ul class="links-list">
	    <% writer.writeLiA("Annual Imunization Data", "#"); %>
	    <% writer.writeLiA("Q4 Sales Engagement", "q4sales"); %>
	    <li><a data-toggle="collapse" href="#collapse3">New Dashboard <span class="hidden">collapse3</span></a>
	      <ul id="collapse3" class="panel-collapse collapse">
	        <% writer.writeLiA("Q4 Sales Leads", "#"); %>
	        <% writer.writeLiA("Regional Marketing Programs", "#"); %>
	        <% writer.writeLiA("New Dashboard", "#"); %>
	      </ul>
	    </li>
	  </ul>
	</div>
	-->
	<!-- Generated from MenuItems List -->
	<nav class="aside-nav">
    <ul>
      <% 
      List<MenuItem> items = new XMLMenuProvider().getMenu();
      
      int num = 100;
      
      for (MenuItem item : items) {
        if (item.hasChildren()) {
          out.print("<li><a data-toggle=\"collapse\" href=\"#collapse" + num + "\">" + item.getName() + "</a>");
          out.print("<div id=\"collapse" + num + "\" class=\"panel-collapse ");
          
          if (item.handlesUri(request.getRequestURI(), request.getContextPath())) {
            out.print("in");
          }
          else {
            out.print("collapse");
          }
          out.print("\">");
          
          out.print("<ul>");
          
          List<MenuItem> children = item.getChildren();
          for (MenuItem child : children) {
            writer.writeLiA(child.getName(), child.getURL());
          }
          
          out.print("</ul>");
          out.print("</div>");
          out.print("</li>");
          
        }
        else {
          writer.writeLiA(item.getName(), item.getURL());
        }
        
        num++;
      }
      %>
    </ul>
  </nav>
  
  <!-- element with tooltip -->
  <a class="btn-tooltip" data-placement="top" data-toggle="tooltip" data-original-title="New map layer" href="#">tooltip</a>
</aside>
<!-- END Generated Bootstrap Sidebar Menu END -->

