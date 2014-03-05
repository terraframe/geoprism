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

<jsp:include page="./templates/header.jsp"></jsp:include>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>

<img src="<%out.print(webappRoot);%>com/runwaysdk/geodashboard/images/terraframe_logo.png" alt="Terraframe Logo">

<br/>
<br/>

<header id="header">
  <h1>Geo Dashboard</h1>
</header>

<p>Powered by Runway SDK™</p>

<jsp:include page="./templates/footer.jsp"></jsp:include>
