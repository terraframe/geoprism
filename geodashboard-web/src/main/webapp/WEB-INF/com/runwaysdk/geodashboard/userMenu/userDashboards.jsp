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
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title><gdb:localize key="login.title" /></title>
  
<!-- User account CSS -->
<jwr:style src="/bundles/main.css" useRandomParam="false" />  
<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
<jwr:style src="/com/runwaysdk/geodashboard/userstable/UsersTable.css" useRandomParam="false"/>  

<!-- CSS -->
<%-- <jwr:style src="/bundles/main.css" useRandomParam="false" /> --%>
<%-- <jwr:style src="/bundles/widget.css" useRandomParam="false"/>   --%>

<!-- JS -->
<%-- <jwr:script src="/bundles/runway.js" useRandomParam="false"/>  --%>
<%-- <jwr:script src="/bundles/main.js" useRandomParam="false"/>   --%>
<%-- <jwr:script src="/bundles/widget.js" useRandomParam="false"/>	 --%>
<%-- 	<jwr:script src="/bundles/localization.js" useRandomParam="false"/> --%>

<!-- User account Javascript -->
<jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
<jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
<jwr:script src="/bundles/account.js" useRandomParam="false"/>  
  
  
<script type="text/javascript">${js}</script>
  
  <style>
		body {
		    background-color: #333;
		    margin: 0;
		}
		
		html, body, #container {
 		    height: 100%; 
			overflow: hidden;
			overflow-y: auto;
		}
		
		h1 {
		    color: maroon;
		    margin-left: 40px;
		} 
		
		#header{
	    	padding: 10px;
		}
		
		.user-command-link{
			color: white;
			padding: 5px;
    		font-size: 15px;
		}
		
		.heading{
		    font-size: 35px;
    		color: white;
    		padding: 20px;
		}
		
		.error-message{
			color: red;
			text-align: center;
		}
		
	</style>
	
</head>
<body>

  <c:if test="${not empty param.errorMessage}">
    <div class="error-message">
      <p>${param.errorMessage}</p>
    </div>
  </c:if>
  
  
  <div id="container">
<!--   <div class="row"> -->
  	<header id="header">
  	    <p class="text-right">
  	        <c:choose>
			  <c:when test="${isAdmin}">
		      		<a class="user-command-link" href="/" class="link-active"><gdb:localize key="userDashboards.admin"/></a>
					<i class="user-command-link"> | </i>
		      </c:when>
		      <c:otherwise>
		      </c:otherwise>
	        </c:choose>
	        <a id="account-btn" class="user-command-link" href="#" class="link-active"><gdb:localize key="userDashboards.account"/></a>
			<i class="user-command-link"> | </i>
 			<a class="user-command-link" href="/session/logout"><gdb:localize key="userDashboards.logout"/></a>
 		</p>
 		<div class="heading text-center"><gdb:localize key="userDashboards.heading"/></div>
	</header>
<!--   </div> -->
  	<div class="row">
  	</div>
        <div class="col-md-3"></div>
        <div class="col-md-6">
          	<c:forEach items="${dashboards}" var="dashboard">
				<!-- <div class="row"> -->
				  <div class="col-sm-6 col-md-4">
				    <div class="thumbnail text-center">
				      <img src="<%=request.getContextPath()%>/com/runwaysdk/geodashboard/images/dashboard_icon.png" alt="Dashboard">
				      <div class="caption">
				        <h3>${dashboard.displayLabel.value}</h3>
						<!-- <p>Dashboard body</p> -->
				        <p><a href="DashboardViewer?dashboard=${dashboard.id}" class="btn btn-primary" role="button"><gdb:localize key="userDashboards.openButton"/></a></p>
				      </div>
				    </div>
				  </div>
				<!-- </div> -->
  			</c:forEach>
        </div>
        <div class="col-md-3"></div>
    </div>
  </div>

</body>
</html>

  <script type="text/javascript">	   
		$(document).ready(function(){

			com.runwaysdk.ui.Manager.setFactory("JQuery");
			
			$("#account-btn").on("click", function(){

				var dialog = com.runwaysdk.ui.Manager.getFactory().newDialog(com.runwaysdk.Localize.get("accountSettings", "Account Settings"), {modal: true, width: 600});
				dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){
			          dialog.close();
			        }, null, {primary: true});
				dialog.setStyle("z-index", 2001);
	        	dialog.render();    
	        	
			  var ut = new com.runwaysdk.ui.userstable.UserForm();  
			  ut.render("#"+dialog.getContentEl().getId());
			})
		});
  </script>
  
  
