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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*" %>
<%@ page import="com.runwaysdk.controller.JSPFetcher"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern" %>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.constants.DeployProperties" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<!-- Tell Runway what the application context path is. -->
<script>
window.com = window.com || {};
window.com.runwaysdk = window.com.runwaysdk || {};
window.com.runwaysdk.__applicationContextPath = "<%=request.getContextPath()%>";
</script>

<%
  // This code must execute before the header is included because the inner HTML may overwrite 
  // the default header and footer includes.
  
  String innerJsp = (String) request.getAttribute(JSPFetcher.INNER_JSP);
  JSPFetcher fetcher = new JSPFetcher(innerJsp, request, response);
  String innerHTML = fetcher.getString();
%>

<!DOCTYPE html>
<html id="innerFrameHtml">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
	<meta http-equiv="pragma" content="no-cache" />

	<title>${page_title}</title>
	
	<!-- CSS imports -->
	<jwr:style src="/bundles/main.css" useRandomParam="false" />
	<jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
	
	<!-- Default imports -->
	<jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
	<jwr:script src="/bundles/main.js" useRandomParam="false"/>  
	<jwr:script src="/bundles/widget.js" useRandomParam="false"/>	
	<jwr:script src="/bundles/localization.js" useRandomParam="false"/>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/Localized.js.jsp"></script>
		  
	<!-- include HTML5 IE enabling script for IE -->
	<!--[if IE 8]><script type="text/javascript" src="./../../../../../ie.js"></script><![endif]-->
	
	<script>
	  // Tell our parent to disable busy CSS when we're done loading	  
	  $(window).load(function() {
	    // executes when complete page is fully loaded, including all frames, objects and images
	    if (!window.location.origin) {
	      window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '');
	    }
	    window.parent.postMessage("iFrameLoadCompleted", window.location.origin);
	  });
	</script>
</head>

<body class="iFrameBody">

  <div class="pageContent">
    <header id="header">
      <h1>${page_title}</h1>
    </header>
    <%= innerHTML %>
  </div>

</body>


