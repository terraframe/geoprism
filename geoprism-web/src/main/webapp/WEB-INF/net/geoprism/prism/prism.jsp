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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<!DOCTYPE html>
<html>
<head>
  <base href="<%=request.getContextPath()%>/prism/management">
  
  <title>Prism Management</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <!-- CSS imports -->
  <jwr:style src="/bundles/main.css" useRandomParam="false" />
  <jwr:style src="/bundles/administration.css" useRandomParam="false" />
  <jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
  <jwr:style src="/bundles/dynamic-map.css" useRandomParam="false"/>  
  
  <!-- Default imports -->
  
  <jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
  <jwr:script src="/bundles/localization.js" useRandomParam="false"/>
    
  <script type="text/javascript" src="${pageContext.request.contextPath}/net/geoprism/Localized.js.jsp"></script>  
  
  <!-- IE required polyfills, in this exact order -->
  <jwr:script src="/bundles/prism.js" useRandomParam="false"/>

  <script>
    window.acp = "<%=request.getContextPath()%>";
  
    System.import(acp + '/system-config.js').then(function () {
        System.import('main');
      }).catch(console.error.bind(console));
  </script>
</head>

<body>
  <my-app>Loading...</my-app>
</body>

</html>
