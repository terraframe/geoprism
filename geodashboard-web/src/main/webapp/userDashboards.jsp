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
  
<%--     <jwr:style src="/bundles/main.css" useRandomParam="false" />   --%>
  <jwr:script src="/bundles/main.js" useRandomParam="false"/>  
<%--   <jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/> --%>
  
<!--   <script type="text/javascript">${js}</script> -->
  
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
		
		
		/* unvisited link */
		a:link {
		    color: grey;
		    text-decoration: none;
		    font-size: 18px;
		}
		
		/* visited link */
		a:visited {
		    color: grey;
		}
		
		/* mouse over link */
		a:hover {
		    color: darkgrey;
		}
		
		/* selected link */
		a:active {
		    color: darkgrey;
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
    		border-radius: 23px;
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
	
<!-- 		<div id="geodash-landing-top-div"> -->
<!-- 	    	<header id="header"></header> -->
<!-- 			<img id="logo" src="com/runwaysdk/geodashboard/images/terraframe_logo_h.png" alt="logo" /> -->
<!-- 		</div>     -->
<!-- 		<div id="geodash-landing-bottom-div"> -->
<!-- 			<div id="mask"></div> -->
<!-- 			<div id="option-container"> -->
<!-- 				<div id="dashboard-link" class="nav-option"> -->
<!-- 					<img class="nav-icon-img" src="com/runwaysdk/geodashboard/images/dashboard_icon.png" alt="Navigation" /> -->
<!-- 					<h5 class="nav-icon-img-label">GEODASHBOARDS</h5> -->
<!-- 				</div> -->
<!-- 				<div class="nav-option"> -->
<!-- 					<img id="geodashboard-admin" class="nav-icon-img" src="com/runwaysdk/geodashboard/images/dashboard_icon.png" alt="Navigation" /> -->
<!-- 					<h5 class="nav-icon-img-label">ADMINISTRATION</h5> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<img id="background-img" src="com/runwaysdk/geodashboard/images/globe_thematic.png" alt="background" /> -->
			
<!-- 			<div id="geodash-landing-footer"> -->
<!-- 				<h4>"COMBINE DATA FROM MULTIPLE SOURCES TO LEARN THE REAL MEANING BEHIND THE BIG PICTURE"</h4> -->
<!-- 			</div> -->
<!-- 		</div> -->
	
  </div>

</body>
</html>

  <script type="text/javascript">	   
		$(document).ready(function(){
				$("#dashboard-link").click(function(){
					window.open(window.location.origin +"${pageContext.request.contextPath}/DashboardViewer", "_self");
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
		})
  </script>
  
  
