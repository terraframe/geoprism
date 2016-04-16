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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<gdb:localize var="page_title" key="splash.pagetitle" />

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="cache-control" content="max-age=0" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
  <meta http-equiv="pragma" content="no-cache" />

  <title>${page_title}</title>
  
  <jwr:style src="/bundles/main.css" useRandomParam="false" />  
  <jwr:script src="/bundles/main.js" useRandomParam="false"/>  
  
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
				$("#main").html('<iframe id="main-content-frame" seamless sandbox="allow-same-origin allow-top-navigation allow-scripts allow-popups allow-forms" src="${pageContext.request.contextPath}/admin/main"></iframe>');			
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

<jsp:include page="./WEB-INF/templates/header.jsp"></jsp:include>
<jsp:include page="./WEB-INF/templates/footer.jsp"></jsp:include>

