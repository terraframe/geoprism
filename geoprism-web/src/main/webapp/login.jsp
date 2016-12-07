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

<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<%--   <title><gdb:localize key="login.title" /></title> --%>

  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/3rd-party/font-awesome-font-icons/font-awesome-4.5.0/css/font-awesome.min.css">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/net/geoprism/css/login.css">
  
  <title><gdb:localize key="login.title" /> </title>	
  
  <link rel="icon" href="${pageContext.request.contextPath}/net/geoprism/images/splash_logo_icon.png">
</head>
<body>
  
  <div id="container">
		<div id="geoprism-landing-top-div">
	    	<header id="header">
				  <form id="login-form" method="post" action="login" class="login">
				  
    					<div class="error-message">
      						<p>${errorMessage}</p>
    					</div>
  
				  	  <div id="login-input-container">
				  	  	<div>
						  <input name="username" type="text" placeholder="<gdb:localize key="login.username"/>" id="login" />
						  <input name="password" type="password" placeholder="<gdb:localize key="login.password"/>" id="password" value="" />
						</div>
					    <p class="login-submit">
					      <button type="submit" class="login-button"><i class="fa fa-sign-in"></i><gdb:localize key="login.button" /></button>
					    </p>
					 </div>				
				  </form>
				  <c:forEach items="${servers}" var="server">
				    <a href="${server.url}">Log in with ${server.displayLabel.value}</a>
				  </c:forEach>
	    	</header>
	    	

			<img id="logo" src="<%= request.getAttribute("banner") %>" alt="logo" />
		</div>    
		<div id="geoprism-landing-bottom-div">
		    <img id="logo_gp" src="${pageContext.request.contextPath}/net/geoprism/images/geoprism_banner.png" alt="logo" />
		    <img id="background-img" src="${pageContext.request.contextPath}/net/geoprism/images/theme_background.png" alt="background" />
		
			<div id="geoprism-landing-footer">
				<h4><gdb:localize key="login.footerMessage"/></h4>
			</div>
		</div>
	
	</div>
</body>
</html>


