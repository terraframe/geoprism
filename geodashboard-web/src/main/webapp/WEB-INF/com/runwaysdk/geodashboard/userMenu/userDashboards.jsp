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


<head>
  <gdb:localize var="page_title" key="login.header"/>
  
  <!-- User account CSS -->
  <jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
  <jwr:style src="/com/runwaysdk/geodashboard/userstable/UsersTable.css" useRandomParam="false"/>  
  <jwr:style src="/font-awesome-font-icons/font-awesome-4.3.0/css/font-awesome.min.css" useRandomParam="false"/> 
  
  <!-- User account Javascript -->
  <jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
  <jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
  <jwr:script src="/bundles/account.js" useRandomParam="false"/> 
  <jwr:script src="/bundles/builder.js" useRandomParam="false"/>

  <script type="text/javascript">     
    $(document).ready(function(){      
      com.runwaysdk.ui.Manager.setFactory("JQuery");
    });
  </script>
    
  <script type="text/javascript">${js}</script>
  
  <style>
    body {
        background-color: #333;
        margin: 0;
        min-width: 100%;
        background-image: url(../../../../../com/runwaysdk/geodashboard/images/earth-profile.jpg);
      background-size: cover;
    }
    
    html, body, #container {
         height: 100%; 
      overflow: hidden;
      overflow-y: auto;
      background-color: rgba(51,51,51,.8);
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
    
    .thumbnail{
      background-color: #eee;
    }
    .thumbnail:hover{
      background-color: #fff;
    }
    
    .thumbnail:hover a img{
      opacity: 1;
        filter: alpha(opacity=100); 
    }
    
      .thumbnail:hover a i{
      opacity: 1;
        filter: alpha(opacity=100); 
    }
    
    .thumbnail:hover a .frame-box{
      opacity: 1;
        filter: alpha(opacity=100); 
    }
    
    img{
      width: 100%;
      border-radius: 4px;
      opacity: 0.6;
        filter: alpha(opacity=60); 
        transition: opacity .25s ease-in-out;
        -moz-transition: opacity .2s ease-in-out;
        -webkit-transition: opacity .2s ease-in-out;
    }
    
    /* font-awesome icon */
    i.fa.fa-plus{
      font-size: 70px;
        padding: 40px;
        font-weight: normal;
        color: darkgrey;
        opacity: 0.6;
        filter: alpha(opacity=60); 
        transition: opacity .25s ease-in-out;
        -moz-transition: opacity .2s ease-in-out;
        -webkit-transition: opacity .2s ease-in-out;
    }
    
    .frame-box {
      position:relative;
      opacity: 0.6;
        filter: alpha(opacity=60); 
        transition: opacity .25s ease-in-out;
        -moz-transition: opacity .2s ease-in-out;
        -webkit-transition: opacity .2s ease-in-out;
    }
    .frame-box:before, .frame-box:after, .frame-box>:first-child:before, .frame-box>:first-child:after {
        position:absolute; content:' ';
        width:35px; height: 35px;
        border-color:darkgrey; 
        border-style:solid; 
        border-radius: 4px;
    }
    .frame-box:before {top:0;left:0;border-width: 6px 0 0 6px}
    .frame-box:after {top:0;right:0;border-width: 6px 6px 0 0}
    .frame-box>:first-child:before {bottom:0;right:0;border-width: 0 6px 6px 0}
    .frame-box>:first-child:after {bottom:0;left:0;border-width: 0 0 6px 6px}    
  </style>
  
  <jwr:script src="/bundles/builder.js" useRandomParam="false"/>   
  
  
  
</head>
<body ng-app="dashboard-menu">

  <c:if test="${not empty param.errorMessage}">
    <div class="error-message">
      <p>${param.errorMessage}</p>
    </div>
  </c:if>  
  
  <div id="container" ng-controller="DashboardMenuController as ctrl"  ng-cloak>
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
       
        <a id="account-btn" ng-click="ctrl.account()" class="user-command-link" href="#" class="link-active"><gdb:localize key="userDashboards.account"/></a>
        <i class="user-command-link"> | </i>
        <a class="user-command-link" href="/session/logout"><gdb:localize key="userDashboards.logout"/></a>
      </p>
      <div class="heading text-center"><gdb:localize key="userDashboards.heading"/></div>
    </header>
    
    <div class="row"></div>
    <div class="col-md-3"></div>
    <div class="col-md-6">
      <div class="row">
        <div ng-if="ctrl.editDashboard" class="col-sm-6 col-md-4" ng-click="ctrl.newDashboard()">
          <a href="#" class="new-dashboard-btn" >
            <div class="thumbnail text-center">
              <div class="frame-box">
                <div class="inner-frame-box">
                  <i class="fa fa-plus"></i>
                </div>
              </div>
              <div class="caption">
                <h3><gdb:localize key="userDashboards.newDashboardTitle"/></h3>
              </div>
            </div>
          </a>
        </div>                         
      
        <div class="col-sm-6 col-md-4" ng-repeat="id in ctrl.ids" ng-init="dashboard = ctrl.dashboards[id]">
          <div class="thumbnail text-center">
            <a ng-href="DashboardViewer?dashboard={{dashboard.dashboardId}}" class="" >
              
              <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
              <img ng-src="/mapthumb/getDashboardMapThumbnail?dashboardId={{dashboard.dashboardId}}" onerror="if (this.src != 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png') this.src = 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png';" alt="Dashboard">
              
              <div class="caption">
                <h3>{{dashboard.label}}</h3>
              </div>
            </a>              
          </div>
          <div ng-if="ctrl.editDashboard" style="color:white;">          
            <a href="#" ng-click="ctrl.edit(dashboard.dashboardId)" style="color:white;"><gdb:localize key="dashboard.edit.label"/></a> |
            <a href="#" ng-click="ctrl.remove(dashboard.dashboardId)" style="color:white;"><gdb:localize key="com.runwaysdk.ui.userstable.DashboardTable.delete"/></a>
          </div>
        </div>
      </div>
    </div>      
    <div class="col-md-3"></div>
       
    <builder-dialog ng-if="ctrl.show != null" show="ctrl.show" callback="ctrl"></builder-dialog>
  </div>
</body>