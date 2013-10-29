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
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <title>${page_title}</title>
 
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%
ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>
  
  <script type="text/javascript" src="com/runwaysdk/log4js.js"></script>
  <script type="text/javascript" src="com/runwaysdk/RunwaySDK_Core.js"></script>
  <script type="text/javascript" src="com/runwaysdk/ui/RunwaySDK_UI.js"></script>
  <script type="text/javascript" src="com/runwaysdk/RunwaySDK_DTO.js"></script>
  <script type="text/javascript" src="com/runwaysdk/RunwaySDK_GIS.js"></script>
  <script type="text/javascript" src="com/runwaysdk/RunwaySDK_Inspector.js"></script>
  
  
  <script type="text/javascript" src="js/main.js"></script>
</head>

<body>
<div id="main_menu"></div>
<div id="content">

<%@include file="inlineError.jsp" %>
