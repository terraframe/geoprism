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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.io.*" %>
<%@page import="com.runwaysdk.controller.JSPFetcher"%>

<!-- Set the defualt header and footer, this can be overidden in the component jsp  -->
<c:set var="header_jsp" value="/WEB-INF/templates/header.jsp"  scope="request"/>
<c:set var="footer_jsp" value="/WEB-INF/templates/footer.jsp"  scope="request"/>

<%
  // This code must execute before the header is included because the inner HTML may overwrite 
  // the default header and footer includes.
  
  String innerJsp = (String) request.getAttribute(JSPFetcher.INNER_JSP);
  JSPFetcher fetcher = new JSPFetcher(innerJsp, request, response);
  String innerHTML = fetcher.getString();
%>

<!-- Render the component inside a pageContent div -->
<jsp:include page="${header_jsp}"  flush="false"  />

<div class="pageContent">
  <div class="pageTitle">${page_title}</div>
  <%= innerHTML %>
</div>

<jsp:include page="${footer_jsp}"  flush="false" />
