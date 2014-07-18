<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
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
  <title><gdb:localize key="error.title" /></title>
  <link rel="stylesheet" href="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/login/login.css">
  <!--[if lt IE 9]><script src="//html5shim.googlecode.com/svn/trunk/html5.js"></script><![endif]-->
</head>
<body>
  <header id="header">
    <h1><gdb:localize key="error.header" /></h1>
  </header>
    
  <div name = "main-content" style="color: #F6F3F3;text-align: center;">
    
  <c:if test="${exception != null}">
      <gdb:localize key="error.message" />
  
      <hr>
      
      ${exception.localizedMessage}  
    </c:if>
    
    <c:if test="${exception == null}">
      <gdb:localize key="error.generic" />
    </c:if>
  </div>
  
</body>
</html>
