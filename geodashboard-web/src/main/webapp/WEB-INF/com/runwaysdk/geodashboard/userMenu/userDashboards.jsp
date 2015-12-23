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
    
    #builder-dialog-category-widget-container{
    }
    
    #builder-dialog-category-widget-container .row-holder{
    	padding: 0;
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
    
	a.btn, .btn:active, .btn:focus {
		padding: 2px 4px 2px 4px;
	}
	
	.caption a:hover{
		color: grey;
	}
	
	.caption p{
		color: grey;
	}
	
	.dashboard-card-ico-button-container{
    	padding: 5px 4px 0px 4px;
    	z-index: 100;
    	right: 0;
    	margin-right: 0;
    	border-top: 1px solid lightgrey;
    	background-color: transparent;
	}
	
	.dashboard-card-ico-button-container:hover{
/* 		background-color: rgba(255,255,255,.9); */
	}
	
	.dashboard-thumbnail-ico-group{
		text-align: right;
	}
	
	.dashboard-thumbnail-subtext{
	    float: left;
    	color: grey;
	}
	
	.dashboard-thumbnail-subtext i {
		margin-right: 4px;
	}
	
	/* this part of the card is clickable but i don't want to confuse users by 
	 having the cursor apear like the fucus area links to a separate page' */
	.dashboard-thumbnail-subtext:hover{
		cursor: default;
	}
	
	.focus-area-label{
    	max-width: 50%;
    	white-space: nowrap;
    	overflow: hidden;
    }
    
    .dashboard-focus-area-label-mask{
  		background: linear-gradient(to left, rgba(255,255,255,1), rgba(225, 255, 255, 0));  
    	height: 19px;
    	width: 50%;
    	position: absolute;
    	opacity: 0; /* set to hidden initially when thumbnail is semi-transparent */
    	transition: opacity .25s;
        -webkit-transition: opacity .25s;
    }
    
    .dashboard-focus-area-label-mask-grey{
    	background: linear-gradient(to left, rgba(238,238,238,1), rgba(225, 255, 255, 0));  
    	height: 19px;
    	width: 50%;
    	position: absolute;
    	opacity: 1; /* set visible initially when thumbnail is semi-transparent (i.e. grey) */
    	transition: opacity .25s;
        -webkit-transition: opacity .25s;
    }
    
    .thumbnail:hover .dashboard-focus-area-label-mask{
       	background: linear-gradient(to left, rgba(255,255,255,1), rgba(225, 255, 255, 0));   
		opacity: 1;  
        filter: alpha(opacity=100); 
    }
    
    .thumbnail:hover .dashboard-focus-area-label-mask-grey{
      	opacity: 0;  
        filter: alpha(opacity=0);   
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
    
    .frame-box:hover i,
    .frame-box:hover::before, 
    .frame-box:hover::after, 
    .frame-box:hover>:first-child::before, 
    .frame-box:hover>:first-child::after{
    	color: #00bfff;
    	border-color:#00bfff;
    	opacity: 1;
    }
    
    .fa-pencil:before {
    	margin-right: 5px;
    	color: white;
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
  
  <div id="clone-container"></div>  
  
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
    	<div ng-repeat="id in ctrl.ids" ng-init="dashboard = ctrl.dashboards[id]">
    		<div ng-if="($index ) % 3 === 0" class="row">
		        <!-- CREATE DASHBOARD CARD #1 
		        	 Why 3 semi-redundant blocks you might ask? To wrap groups of 3 in a bootstrap ROW. -->
		        <div ng-if="ctrl.dashboards[ctrl.ids[$index]]" class="col-sm-6 col-md-4">
		          <div  class="thumbnail text-center">
		            <a ng-href="DashboardViewer?dashboard={{ctrl.dashboards[ctrl.ids[$index]].dashboardId}}" class="" >
		              <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
		              <img ng-src="/mapthumb/getDashboardMapThumbnail?dashboardId={{ctrl.dashboards[ctrl.ids[$index]].dashboardId}}" 
		              		onerror="if (this.src != 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png') this.src = 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png';" 
		              		alt="Dashboard">
		              
		              <div class="caption">
		                <h3>{{ctrl.dashboards[ctrl.ids[$index]].label}}</h3>
		                <p>{{ctrl.dashboards[ctrl.ids[$index]].description}}</p>
						<div ng-if="ctrl.editDashboard" class="dashboard-card-ico-button-container">     
		            		<p class="dashboard-thumbnail-subtext focus-area-label" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index]].focusAreasAsString}})"><i class="fa fa-globe"></i>{{ctrl.dashboards[ctrl.ids[$index]].focusAreasAsString}}</p>
		            		<div class="dashboard-focus-area-label-mask-grey" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index]].focusAreasAsString}})"></div>
		            		<div class="dashboard-focus-area-label-mask" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index]].focusAreasAsString}})"></div>
		            		<div class="dashboard-thumbnail-ico-group">
		            			<a href="#" class="fa fa-cog ico-dashboard-options" title="<gdb:localize key="userDashboards.editDashboardTooltip"/>" ng-click="ctrl.edit(ctrl.dashboards[ctrl.ids[$index]].dashboardId)" ></a> 
		            			<a href="#" class="fa fa-plus ico-dashboard" title="<gdb:localize key='dashboardViewer.newDashboardTooltip'/>" ng-click="ctrl.cloneDashboard(ctrl.dashboards[ctrl.ids[$index]].dashboardId)"></a>
		            			<a href="#" class="fa fa-times ico-remove" title="<gdb:localize key="userDashboards.deleteDashboardTooltip"/>" ng-click="ctrl.remove(ctrl.dashboards[ctrl.ids[$index]].dashboardId)" ></a>
		          	  		</div>
		          	  	</div>
		              </div>
		            </a>              
		          </div>
		        </div>
		        
		        <!-- CREATE DASHBOARD CARD #2 
		        	Why 3 semi-redundant blocks you might ask? To wrap groups of 3 in a bootstrap ROW. -->
		        <div ng-if="ctrl.dashboards[ctrl.ids[$index + 1]]" class="col-sm-6 col-md-4">
		          <div  class="thumbnail text-center">
		            <a ng-href="DashboardViewer?dashboard={{ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId}}" class="" >
		              
		              <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
		              <img ng-src="/mapthumb/getDashboardMapThumbnail?dashboardId={{ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId}}" onerror="if (this.src != 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png') this.src = 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png';" alt="Dashboard">
		              
		              <div class="caption">
		                <h3>{{ctrl.dashboards[ctrl.ids[$index + 1]].label}}</h3>
		                <p>{{ctrl.dashboards[ctrl.ids[$index + 1]].description}}</p>
		          		<div ng-if="ctrl.editDashboard" class="dashboard-card-ico-button-container">     
		            		<p class="dashboard-thumbnail-subtext focus-area-label" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index + 1]].focusAreasAsString}})"><i class="fa fa-globe"></i>{{ctrl.dashboards[ctrl.ids[$index + 1]].focusAreasAsString}}</p>
		            		<div class="dashboard-focus-area-label-mask-grey" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index + 1]].focusAreasAsString}})"></div>
		            		<div  class="dashboard-focus-area-label-mask" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index + 1]].focusAreasAsString}})"></div>
		            		<div class="dashboard-thumbnail-ico-group">
		            			<a href="#" class="fa fa-cog ico-dashboard-options" title="<gdb:localize key="userDashboards.editDashboardTooltip"/>" ng-click="ctrl.edit(ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId)" ></a> 
		            			<a href="#" class="fa fa-plus ico-dashboard" title="<gdb:localize key='dashboardViewer.newDashboardTooltip'/>" ng-click="ctrl.cloneDashboard(ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId)"></a>
		            			<a href="#" class="fa fa-times ico-remove" title="<gdb:localize key="userDashboards.deleteDashboardTooltip"/>" ng-click="ctrl.remove(ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId)" ></a>
		          	  		</div>
		          	  	</div>
		              </div>
		            </a>              
		          </div>
		        </div>
		        
		        <!-- CREATE DASHBOARD CARD #3 
		        	Why 3 semi-redundant blocks you might ask? To wrap groups of 3 in a bootstrap ROW.-->
		        <div ng-if="ctrl.dashboards[ctrl.ids[$index + 2]]" class="col-sm-6 col-md-4">
		          <div  class="thumbnail text-center">
		            <a ng-href="DashboardViewer?dashboard={{ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId}}" class="" >
		              
		              <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
		              <img ng-src="/mapthumb/getDashboardMapThumbnail?dashboardId={{ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId}}" onerror="if (this.src != 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png') this.src = 'com/runwaysdk/geodashboard/images/dashboard_icon_small.png';" alt="Dashboard">
		              
		              <div class="caption">
		                <h3>{{ctrl.dashboards[ctrl.ids[$index + 2]].label}}</h3>
		                <p>{{ctrl.dashboards[ctrl.ids[$index + 2]].description}}</p>
		          		<div ng-if="ctrl.editDashboard" class="dashboard-card-ico-button-container">     
		            		<p class="dashboard-thumbnail-subtext focus-area-label" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index + 2]].focusAreasAsString}})"><i class="fa fa-globe"></i>{{ctrl.dashboards[ctrl.ids[$index + 2]].focusAreasAsString}}</p>
		            		<div class="dashboard-focus-area-label-mask-grey" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index + 2]].focusAreasAsString}})"></div>
		            		<div class="dashboard-focus-area-label-mask" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index + 2]].focusAreasAsString}})"></div>
		            		<div class="dashboard-thumbnail-ico-group">
		            			<a href="#" class="fa fa-cog ico-dashboard-options" title="<gdb:localize key="userDashboards.editDashboardTooltip"/>" ng-click="ctrl.edit(ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId)" ></a> 
		            			<a href="#" class="fa fa-plus ico-dashboard" title="<gdb:localize key='dashboardViewer.newDashboardTooltip'/>" ng-click="ctrl.cloneDashboard(ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId)"></a>
		            			<a href="#" class="fa fa-times ico-remove" title="<gdb:localize key="userDashboards.deleteDashboardTooltip"/>" ng-click="ctrl.remove(ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId)" ></a>
		          	  		</div>
		          	  	</div>
		              </div>
		            </a>              
		          </div>
		        </div>
		        
				<!-- CREATE NEW DASHBOARD LINK IN EXISTING ROW -->
		        <div ng-if="ctrl.dashboards[ctrl.ids[$index]].isLastDashboard || 
		        		ctrl.dashboards[ctrl.ids[$index + 1]].isLastDashboard && 
		        		ctrl.editDashboard" 
		        		class="col-sm-6 col-md-4" ng-click="ctrl.newDashboard()">
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
		    </div>
	    </div>
        
        <!-- CREATE NEW LINK IN NEW ROW -->
        <div ng-if="!ctrl.isInExistingRow && ctrl.editDashboard" class="row">
	        <div class="col-sm-6 col-md-4" ng-click="ctrl.newDashboard()">
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
      	</div>
    </div>      
    <div class="col-md-3"></div>
       
    <builder-dialog></builder-dialog>
  </div>
</body>