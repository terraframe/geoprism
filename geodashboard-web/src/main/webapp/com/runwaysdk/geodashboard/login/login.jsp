<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/mdssLib.tld" prefix="mdss"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>


<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!--> <html lang="en"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>GeoDashboard Login</title>
  <link rel="stylesheet" href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/login/login.css">
  <!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
</head>
<body>
  <header id="header">
    <h1>GeoDashboard</h1>
  </header>

  <a class="register" href="index.html">Not a member? Register now</a>

  <form method="post" action="session/login" class="login">
    
    <c:if test="${bad_password}">
      <div class="alert alertbox">
		    <p>${exception.localizedMessage}</p>
		  </div>
		</c:if>
		
		<p>
		  <label> <mdss:localize key="username" />: </label>
		  <mjl:input param="username" type="text" id="login" />
		</p>
		<p>
		  <label> <mdss:localize key="password" />: </label>
		  <mjl:input param="password" type="password" id="password" value="" />
		</p>
  
    <!--
    <p>
      <label for="login">Email:</label>
      <input type="text" name="login" id="login">
    </p>

    <p>
      <label for="password">Password:</label>
      <input type="password" name="password" id="password">
    </p>
    -->

    <p class="login-submit">
      <button type="submit" class="login-button">Login</button>
    </p>

    <div>
      <a class="forgot-password" href="index.html">Forgot your password?</a>
      <label class="remember-me">&nbsp;Remember me<input name="rememberme" id="rememberme" type="checkbox" checked="checked" value="forever"/></label>
    </div>
  </form>
</body>
</html>
