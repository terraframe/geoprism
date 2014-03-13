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
<%@ taglib uri="/WEB-INF/tlds/mdssLib.tld" prefix="mdss"%>
<%@page import="com.runwaysdk.constants.DeployProperties" %>

<c:set var="page_title" scope="request" value="mi.Q"/>

<jsp:include page="../../../../WEB-INF/templates/header.jsp"></jsp:include>


<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>

<img src="<%out.print(webappRoot);%>com/runwaysdk/geodashboard/images/iDE_logo.gif" alt="iDE Logo">

<br/>
<br/>

<header id="header">
  <h1><mdss:localize key="mi.Q Dashboard" /></h1>
</header>

<p><mdss:localize key="Powered by Runway SDK™" /></p>

<jsp:include page="../../../../WEB-INF/templates/footer.jsp"></jsp:include>
