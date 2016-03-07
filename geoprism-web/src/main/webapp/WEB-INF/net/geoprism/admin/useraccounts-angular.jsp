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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<%@ page import="java.io.*" %>
<%@ page import="com.runwaysdk.controller.JSPFetcher"%>
<%@ page import="java.util.regex.Matcher"%>
<%@ page import="java.util.regex.Pattern" %>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.constants.DeployProperties" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<!DOCTYPE html>
<html id="innerFrameHtml" ng-app="myApp">

  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
	<meta http-equiv="pragma" content="no-cache" />

	<title><gdb:localize key="useraccounts.title"/></title>
	
	<!-- Tell Runway what the application context path is. -->
	<script>
	  window.com = window.com || {};
	  window.com.runwaysdk = window.com.runwaysdk || {};
	  window.com.runwaysdk.__applicationContextPath = "<%=request.getContextPath()%>";
	</script>	
	
	<!-- CSS imports -->
	<jwr:style src="/bundles/main.css" useRandomParam="false" />
	<jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
	
	<!-- Default imports -->
	<jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
	<jwr:script src="/bundles/main.js" useRandomParam="false"/>  
	<jwr:script src="/bundles/widget.js" useRandomParam="false"/>	
	<jwr:script src="/bundles/localization.js" useRandomParam="false"/>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/net/geoprism/Localized.js.jsp"></script>
		  
	<!-- include HTML5 IE enabling script for IE -->
	<!--[if IE 8]><script type="text/javascript" src="./../../../../../ie.js"></script><![endif]-->
	
	<script>
	  // Tell our parent to disable busy CSS when we're done loading	  
	  $(window).load(function() {
	    // executes when complete page is fully loaded, including all frames, objects and images
	    if (!window.location.origin) {
	      window.location.origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port: '');
	    }
	    window.parent.postMessage("iFrameLoadCompleted", window.location.origin);
	  });
	</script>
	
	<!-- User account CSS -->
	<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
	<jwr:style src="/net/geoprism/userstable/UsersTable.css" useRandomParam="false"/>  

	<!-- User account Javascript -->
	<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.7/angular.min.js"></script>	
	<jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
	<jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
	<jwr:script src="/bundles/account.js" useRandomParam="false"/>
	<script type="text/javascript">${js}</script>

  </head>
  <body>
    <div ng-controller="UserCtrl">
      <user-form dto="dto" refresh="refresh()"></user-form>
      <hr>
      
      <table class="table table-striped table-hover">
        <thead>
          <tr>
            <th></th>
            <th></th>
            <th>Username</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Phone number</th>
            <th>Email</th>
          </tr>
        </thead>
        <tbody>
          <tr ng-repeat="user in users">
            <td><a href="#" class="fa fa-times ico-remove table-delete" ng-click="remove($index)"></a></td>
            <td><a href="#" class="fa fa-pencil ico-edit table-edit" ng-click="edit($index)"></a></td>
            <td>{{user.username}}</td>
            <td>{{user.firstName}}</td>
            <td>{{user.lastName}}</td>
            <td>{{user.phoneNumber}}</td>
            <td>{{user.email}}</td>
          </tr>
        </tbody>
      </table>  
    </div>
  </body>
</html>