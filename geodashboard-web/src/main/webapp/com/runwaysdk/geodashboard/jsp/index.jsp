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

<jsp:include page="../../../../WEB-INF/templates/header.jsp"></jsp:include>


<% String webappRoot = "/" + DeployProperties.getAppName() + "/"; %> 

<!-- Main content --> 
<iframe id="main-content-frame" scrolling="no" seamless sandbox="allow-same-origin allow-scripts allow-popups allow-forms" src="<%out.print(webappRoot);%>com/runwaysdk/geodashboard/jsp/mainContent.jsp"></iframe>


<script type="text/javascript" >

	////Accounting for browser memory when page is refreshed
	$(document).ready(function(){		
		if (window.location.hash !== '') {
			
	 		//// Update main content src attribute based on hash change (initiated by click on sidebar <a> tag)
	  		$("#main-content-frame").attr("src", window.location.hash.substring(1)); 		
		}
	
		$(window).on('hashchange', function(e) {
			
	 		//// Update main content src attribute based on hash change (initiated by click on sidebar <a> tag)
	  		$("#main-content-frame").attr("src", e.target.location.hash.substring(1)); 		
		});
	});
		
</script>

<jsp:include page="../../../../WEB-INF/templates/footer.jsp"></jsp:include>

