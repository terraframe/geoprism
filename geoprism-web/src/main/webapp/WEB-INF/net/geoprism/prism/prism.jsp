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
  
  <title><gdb:localize key="prism.management.title"/></title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <!-- CSS imports -->
  <jwr:style src="/bundles/main.css" useRandomParam="false" />
  <jwr:style src="/bundles/administration.css" useRandomParam="false" />
  
  <!-- Default imports -->  
  <jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
  <jwr:script src="/bundles/ng2-main.js" useRandomParam="false"/> 
    
  <script type="text/javascript" src="${pageContext.request.contextPath}/net/geoprism/Localized.js.jsp"></script>  
  
  
  <script>
    window.acp = "<%=request.getContextPath()%>";  
    window.appname = "${appname}";  
  </script>
  
  <!-- IE required polyfills, in this exact order -->
  <script type="text/javascript" src="$local.host$/dist/polyfills.js"></script>  
  <script type="text/javascript" src="$local.host$/dist/vendor.js"></script>  

</head>

<body>
  <my-app>
  
    <!-- TEMP CONTENT WHILE THE PAGE IS LOADING -->

    <div id="innerFrameHtml">
      <header>
        <nav class="navbar navbar-default">
          <div class="container">
            <div class="navbar-header">
              <a class="navbar-brand" href="${pageContext.request.contextPath}/menu" title="<gdb:localize key="userMenu.menuTooltip"/>"><img src="${pageContext.request.contextPath}/logo/view?id=logo" /></a>
              <a class="navbar-brand" href="${pageContext.request.contextPath}/prism/management"><gdb:localize key="data.management.title"/></a>
            </div>

          </div>
        </nav>
      </header>
        
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
