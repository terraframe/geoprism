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
<%-- 	<jwr:style src="/bundles/main.css" useRandomParam="false" /> --%>
<%-- 	<jwr:style src="/bundles/widget.css" useRandomParam="false"/>   --%>
	    
	<!-- JS -->
<%-- 	<jwr:script src="/bundles/runway.js" useRandomParam="false"/>  --%>
<%-- 	<jwr:script src="/bundles/main.js" useRandomParam="false"/>   --%>
<%-- 	<jwr:script src="/bundles/widget.js" useRandomParam="false"/>	 --%>
	<%-- 	<jwr:script src="/bundles/localization.js" useRandomParam="false"/> --%>
	  
	  <!-- User account Javascript -->
	<jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
	<jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
	<jwr:script src="/bundles/account.js" useRandomParam="false"/>  
  
  <style>
		body {
		    background-color: black;
		    margin: 0;
		}
		
		html, body, #container {
 		    height: 100%; 
			overflow: hidden;
		}
		
		h1 {
		    color: maroon;
		    margin-left: 40px;
		} 
		
		#logo{
	    	margin-left: 50px;
	    	width: 250px;
	    	position: absolute;
  			top: 50%;
  			transform: translateY(-50%);
		}
		
		#geodash-landing-top-div{
	    	border-bottom: grey solid 3px;
			/* height: 30%; */
			height: 200px;
			position: relative;
			    background-color: #333;
		}
		
		#geodash-landing-bottom-div{
			overflow: hidden;
		}
			
		#geodash-landing-footer{
			height: 100px;
			background-color: #019edc;
			bottom: 0px;
    		position: absolute;
    		width: 100%;
    		opacity: 0.6;
    		filter: alpha(opacity=60); /* For IE8 and earlier */
		}
			
		#geodash-landing-footer h4{
			color: white;
			text-align: center;
			margin-top: 42px;
		}
		
		#background-img{
 	    	width: 100%; 
 	    	margin-top: 100px;
		}
		
		#mask{
			background-color: white;
			width: 100%;
    		height: 100%;
    		position: absolute;
			opacity: 0.70;
    		filter: alpha(opacity=70); /* For IE8 and earlier */
		}
		
		#option-container{
    		top: 50%;
    		margin: 0 auto;
    		margin-left: -401px;
    		left: 50%;
    		position: absolute;
    		width: 650px;
		}
		
		.nav-option{
    		margin-left: 50px;
    		float: left;
		}
		
		.nav-icon-img{
	    	height: 150px;
	    	border: solid white 5px;
    		border-radius: 10px;
    		cursor: pointer;
    		background-color: white;
		}
		
		.nav-icon-img:hover{
	    	border: solid #019edc 5px;
		}
		
		#dashboard-link{
			cursor: pointer;
		}
		
		.nav-icon-img-label{
			text-align: center;
			margin-top: 10px;
			max-width: 250px;
		}
		
		#header{
	    	padding: 10px;
		}
		
		.user-command-link{
			color: white;
			padding: 5px;
    		font-size: 15px;
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
	
		<div id="geodash-landing-top-div">
			<header id="header">
				<img id="logo" src="<%=request.getContextPath()%>/com/runwaysdk/geodashboard/images/splash_logo.png" alt="logo"/>
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
 			</header>
		</div>    
		<div id="geodash-landing-bottom-div">
			<div id="mask"></div>
			<div id="option-container">
				<div id="dashboard-link" class="nav-option">
					<img class="nav-icon-img" src="<%=request.getContextPath()%>/com/runwaysdk/geodashboard/images/dashboard_icon.png" alt="Navigation" />
					<h4 class="nav-icon-img-label"><gdb:localize key="geodashboardLanding.geodashboards"/></h4>
				</div>
				<div class="nav-option">
					<img id="geodashboard-admin" class="nav-icon-img" src="<%=request.getContextPath()%>/com/runwaysdk/geodashboard/images/admin_icon.png" alt="Navigation" />
					<h4 class="nav-icon-img-label"><gdb:localize key="geodashboardLanding.administration"/></h4>
				</div>
			</div>
			<img id="background-img" src="<%=request.getContextPath()%>/com/runwaysdk/geodashboard/images/globe_thematic.png" alt="background" />
			
			<div id="geodash-landing-footer">
				<h4><gdb:localize key="geodashboardLanding.footerMessage"/></h4>
			</div>
		</div>
	
  </div>

</body>
</html>

  <script type="text/javascript">	   
		$(document).ready(function(){
				$("#dashboard-link").click(function(){
					window.open(window.location.origin +"${pageContext.request.contextPath}/dashboards", "_self");
				});	

				$("#geodashboard-admin").click(function(){
					window.open(window.location.origin +"${pageContext.request.contextPath}", "_self");
				});	

				  //responsive code begin
			      //you can remove responsive code if you don't want the slider scales while window resizes
			      function ScaleBackgroundImg() {
			          var bodyWidth = document.body.clientWidth;
			          var elWidth = $("#option-container").width();
			          var adjusted = elWidth / 2;
			          
			          if(bodyWidth){
		              	  $("#option-container").css('margin-left', '-'+adjusted+'px')
			          }	  
			          else{
			              window.setTimeout(ScaleSlider, 30);
			          }
			      }
				  ScaleBackgroundImg();

			      $(window).bind("load", ScaleBackgroundImg);
			      $(window).bind("resize", ScaleBackgroundImg);
			      $(window).bind("orientationchange", ScaleBackgroundImg);


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
		})
  </script>
  
  
