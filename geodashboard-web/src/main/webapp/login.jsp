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
<%--   <title><gdb:localize key="login.title" /></title> --%>
  
	<style>
		body {
		    background-color: #333;
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
		
		#geoprism-landing-top-div{
	    	border-bottom: grey solid 3px;
			/* height: 30%; */
			height: 200px;
			position: relative;
		}
		
		#geoprism-landing-bottom-div{
			overflow: hidden;
		}
		
		#login-btn{
			right: 10px;
			top: 5px;
    		position: absolute;
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
		
		#geoprism-landing-footer{
			height: 100px;
			background-color: #019edc;
			bottom: 0px;
    		position: absolute;
    		width: 100%;
    		opacity: 0.6;
    		filter: alpha(opacity=60); /* For IE8 and earlier */
		}
		
		#login-form{
			float: right;
			width: 250px;
		}
		
		#login-form input{
			width: 150px;
			float: left;
    		margin-top: 10px;
    		background-color: #9F9D9D;
    		border: none;
    		padding: 3px;
		}
		
		.login-button{
			text-decoration: none;
    		border: none;
    		background-color: transparent;
    		color: white;
    		cursor: pointer;
    		font-size: 16px;
		}
		
		.login-button:hover{
			color: grey;
		}
		
		.login-submit{
    		margin: 0 0 0 0;
    		position: absolute;
    		right: 30px;
   			top: 43px;
		}
		
		#geoprism-landing-footer h4{
			color: white;
			text-align: center;
			margin-top: 42px;
		}
		
		#background-img{
 	    	width: 100%; 
 	    	margin-top: 100px;
		}
		
		
		.error-message{
			color: red;
			text-align: center;
		}
		
	</style>
	<title><gdb:localize key="login.title" /> </title>	
</head>
<body>
  
  <c:if test="${not empty param.errorMessage}">
    <div class="error-message">
      <p>${param.errorMessage}</p>
    </div>
  </c:if>
  
  
  <div id="container">
	
		<div id="geoprism-landing-top-div">
	    	<header id="header">
				  <form id="login-form" method="post" action="session/login" class="login">
				  	  <div>
						<p>
<%-- 					  <label> <gdb:localize key="login.username" />: </label> --%>
						  <mjl:input param="username" type="text" id="login" />
						</p>
						<p>
<%-- 					  <label> <gdb:localize key="login.password" />: </label> --%>
						  <mjl:input param="password" type="password" id="password" value="" />
						</p>
					
					    <p class="login-submit">
					      <button type="submit" class="login-button"><gdb:localize key="login.button" /></button>
					    </p>
					 </div>
				
				<!--     TODO: Forgot password and remember me functionality.
				<!--     <div> -->
				<!--       <a class="forgot-password" href="index.html">Forgot your password?</a> -->
				<!--       <label class="remember-me">&nbsp;Remember me<input name="rememberme" id="rememberme" type="checkbox" checked="checked" value="forever"/></label> -->
				<!--     </div> -->
				
				<!--     --> 
				  </form>
	    	</header>
			<img id="logo" src="com/runwaysdk/geodashboard/images/splash_logo.png" alt="logo" />
		</div>    
		<div id="geoprism-landing-bottom-div">
		    <img id="background-img" src="com/runwaysdk/geodashboard/images/theme_background.png" alt="background" />
		
			<div id="geoprism-landing-footer">
				<h4><gdb:localize key="login.footerMessage"/></h4>
			</div>
		</div>
	
	</div>
</body>
</html>


