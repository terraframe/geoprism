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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ page import="com.runwaysdk.constants.DeployProperties" %>

<gdb:localize var="page_title" key="splash.pagetitle" />

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="cache-control" content="max-age=0" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
  <meta http-equiv="pragma" content="no-cache" />

  <title>${page_title}</title>
 
  <%@page import="com.runwaysdk.constants.ClientConstants"%>
  <%@page import="com.runwaysdk.constants.ClientRequestIF"%>
  <%@page import="com.runwaysdk.web.json.JSONController"%>

  <%@page import="com.runwaysdk.constants.DeployProperties" %>
  <%
    String webappRoot = request.getContextPath() + "/";
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
  
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>bootstrap/bootstrap.min.css">
  <link rel="stylesheet" type="text/css" href="<% out.print(webappRoot); %>jquery/ui/themes/lightness.css">
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/css/all.css">
  <link media="all" rel="stylesheet" href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/css/additions.css">

  <!-- jQuery -->
  <!-- <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script> -->
  <!-- <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/jquery-1.8.3.min.js"></script> -->
  <!-- <script src="https://code.jquery.com/jquery-1.9.0.js"></script> -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/jquery-1.9.0.min.js"></script>
  
  <!-- Bootstrap must be loaded before JQuery-UI or else jquery-ui gets screwy -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>bootstrap/bootstrap.min.js"></script>
  
  <!-- Custom JavaScript -->
  <script type="text/javascript" src="<% out.print(webappRoot); %>psd2html.jcf.js"></script>
<%--   <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/jquery.datepicker.js"></script> --%>
  
  <!-- JQuery UI -->
   <script type="text/javascript" src="<% out.print(webappRoot); %>jquery/ui/js/jquery-ui.min.js"></script>
   <link rel="stylesheet" href="<% out.print(webappRoot); %>jquery/ui/themes/jquery-ui.min.css" ></link>
   
   <!-- Localization -->	
   <script type="text/javascript" src="${pageContext.request.contextPath}/cldrjs-0.4.0/dist/cldr.js"></script>
   <script type="text/javascript" src="${pageContext.request.contextPath}/cldrjs-0.4.0/dist/cldr/event.js"></script>
   <script type="text/javascript" src="${pageContext.request.contextPath}/cldrjs-0.4.0/dist/cldr/supplemental.js"></script>
   
   <script type="text/javascript" src="${pageContext.request.contextPath}/globalize-1.0.0-alpha.17/dist/globalize.js"></script>
   <script type="text/javascript" src="${pageContext.request.contextPath}/globalize-1.0.0-alpha.17/dist/globalize/number.js"></script>
   <script type="text/javascript" src="${pageContext.request.contextPath}/globalize-1.0.0-alpha.17/dist/globalize/currency.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/globalize-1.0.0-alpha.17/dist/globalize/date.js"></script>
   
   <script type="text/javascript" src="${pageContext.request.contextPath}/jquery/ui/js/jquery-ui-i18n.min.js"></script>	
   <script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/Localized.js.jsp"></script>
	  
  <!-- include HTML5 IE enabling script for IE -->
  <!--[if IE 8]><script type="text/javascript" src="./../../../../../ie.js"></script><![endif]-->
  
  
  <script type="text/javascript">	   
		////Accounting for browser memory when page is refreshed
		$(document).ready(function(){		
			if (window.location.hash.length > 0) {			
				//// Add iframe with hash src based on browser memory hash
				$("#main").html('<iframe id="main-content-frame" seamless sandbox="allow-same-origin allow-top-navigation allow-scripts allow-popups allow-forms" src='+window.location.hash.substring(1)+'></iframe>');
			}
			else {				
				//// Add main page if no hash exists
				$("#main").html('<iframe id="main-content-frame" seamless sandbox="allow-same-origin allow-top-navigation allow-scripts allow-popups allow-forms" src="<%out.print(webappRoot);%>com/runwaysdk/geodashboard/jsp/mainContent.jsp"></iframe>');			
			}
		
			$(window).on('hashchange', function(e) {				
		 		//// Update main content src attribute based on hash change (initiated by click on sidebar <a> tag)
		  		$("#main-content-frame").attr("src", e.target.location.hash.substring(1)); 		
			});		
		});			
				
		// Added to handle forwarding of login page to parent from iframe after timeout
		window.addEventListener('message', function(e) {
			// handling IE
			if (!window.location.origin) {
				window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
			}
			if (e.origin === window.location.origin) {
				if (e.data === "iFrameLoadCompleted") {
					$(".gdb-maincontent-busy").each(function() {
						$(this).remove();
					});
				} else {
					window.location = e.data;
				}
			}
		}, false);	
  </script>
  
</head>

<jsp:include page="../../../../WEB-INF/templates/header.jsp"></jsp:include>
<jsp:include page="../../../../WEB-INF/templates/footer.jsp"></jsp:include>

