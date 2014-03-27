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



<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>${page_title}</title>
 
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>
  <script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/log4js.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/errorcatch.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/Util.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ClassFramework.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/Structure.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_Core.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_DTO.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_GIS.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/RunwaySDK_Inspector.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/RunwaySDK_UI.js"></script>
	
	<script type="text/javascript" src="<% out.print(webappRoot); %>leaflet/leaflet.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>leaflet_plugins/leaflet-wms-plugin/layer/tile/Google.js"></script>
	<script type="text/javascript" src="<% out.print(webappRoot); %>leaflet_plugins/mouse-position-master/src/L.Control.MousePosition.js"></script>
	
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/Geodashboard.js"></script>
	
	<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/map.js"></script> 
  
  <!-- set the encoding of your site -->
  <!-- include the site stylesheet -->
  
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>leaflet/leaflet.css">
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>leaflet_plugins/mouse-position-master/src/L.Control.MousePosition.css">
  
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>bootstrap/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="<% out.print(webappRoot); %>jquery/ui/themes/lightness.css">
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/css/all.css">
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/css/map.css">
  <!-- include jQuery library -->
  <!--
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
  -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/jquery-1.8.3.min.js"></script>
  <!-- Bootstrap must be loaded before JQuery-UI or else jquery-ui gets screwy -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>bootstrap/bootstrap.min.js"></script>
  <!-- include custom JavaScript -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>psd2html.jcf.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/jquery.datepicker.js"></script>
  <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/ui/js/jquery-ui.min.js"></script>
  <link rel="stylesheet" href="<% out.print(webappRoot); %>jquery/ui/themes/jquery-ui.min.css" ></link>
  <!-- include HTML5 IE enabling script for IE -->
  <!--[if IE 8]><script type="text/javascript" src="./../../../../../ie.js"></script><![endif]-->
</head>




  

      
