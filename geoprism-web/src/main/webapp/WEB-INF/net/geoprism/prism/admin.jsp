<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<!DOCTYPE html>
<html>
<head>
  <base href="${pageContext.request.contextPath}/${base}">
  <link rel="icon" href="${pageContext.request.contextPath}/logo/view?id=logo"/>
  
  <title><gdb:localize key="login.header"/></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <!-- CSS imports -->
  <jwr:style src="/bundles/main.css" useRandomParam="false" />
  <jwr:style src="/bundles/administration.css" useRandomParam="false" />
  
  <!-- Tree CSS -->
  <jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
  <jwr:style src="/bundles/termtree.css" useRandomParam="false"/>  
  <jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
  
  
  <!-- Default imports -->  
  <jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
  <jwr:script src="/bundles/main.js" useRandomParam="false"/>  
  <jwr:script src="/bundles/widget.js" useRandomParam="false"/> 
  
  <jwr:script src="/bundles/ng2-main.js" useRandomParam="false"/> 

  <!-- Tree Javascript -->
  <jwr:script src="/bundles/termtree.js" useRandomParam="false"/>
  <jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
  <jwr:script src="/bundles/geoentity.js" useRandomParam="false"/>
  <jwr:script src="/bundles/universal.js" useRandomParam="false"/>
  <jwr:script src="/bundles/ontology.js" useRandomParam="false"/>
  <jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
  <jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
  <jwr:script src="/bundles/databrowser.js" useRandomParam="false"/>
    
    
  <script type="text/javascript" src="${pageContext.request.contextPath}/net/geoprism/Localized.js.jsp"></script>  
  
  
  <script>
    window.acp = "<%=request.getContextPath()%>";  
    window.appname = "${appname}";  
    
    window.com = window.com || {};
    window.com.runwaysdk = window.com.runwaysdk || {};
    window.com.runwaysdk.__applicationContextPath = "<%=request.getContextPath()%>";
    
  </script>
  
  <!-- IE required polyfills, in this exact order -->
  <script type="text/javascript" src="$local.host$/dist/polyfills.js"></script>  
  <script type="text/javascript" src="$local.host$/dist/vendor.js"></script>  
  
  <script type="text/javascript">${js}</script>
  
  <script type="text/javascript">
    com.runwaysdk.ui.DOMFacade.execOnPageLoad(function(){
      com.runwaysdk.ui.Manager.setFactory("JQuery");
    });
  </script>
  
</head>

<body>
  <my-app>
  
    <!-- TEMP CONTENT WHILE THE PAGE IS LOADING -->

    <div id="innerFrameHtml">
      <div id="main" class="new-admin-design-main">
        <div class="text-center">
          <i class="fa fa-spinner fa-spin fa-3x fa-fw"></i>
          <div><gdb:localize key="prism.loading"/></div>
        </div>
      </div>
    </div>  
  </my-app>
  <script type="text/javascript" src="$local.host$/dist/app.js"></script>  
  
  
</body>

</html>
