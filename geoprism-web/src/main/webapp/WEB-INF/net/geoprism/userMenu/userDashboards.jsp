<%--

    Copyright (c) 2022 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>


<head>
  <gdb:localize var="page_title" key="login.header"/>
  
  <link rel="icon" href="${pageContext.request.contextPath}/logo/view?id=logo"/>
  
  <script>
    window.com = window.com || {};
    window.com.runwaysdk = window.com.runwaysdk || {};
    window.com.runwaysdk.__applicationContextPath = "<%=request.getContextPath()%>";
  </script>  
  
  <!-- User account CSS -->
  <jwr:style src="/bundles/main.css" useRandomParam="false" />
  <jwr:style src="/bundles/widget.css" useRandomParam="false"/>    
  <jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
  <jwr:style src="/bundles/user-dashboards.css" useRandomParam="false"/> 
  
  <!-- User account Javascript -->  
  <jwr:script src="/bundles/runway.js" useRandomParam="false"/> 
  <jwr:script src="/bundles/main.js" useRandomParam="false"/>  
  <jwr:script src="/bundles/widget.js" useRandomParam="false"/>	
  <jwr:script src="/bundles/localization.js" useRandomParam="false"/>
  
<!-- 
  <jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
  <jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
  <jwr:script src="/bundles/account.js" useRandomParam="false"/> 
 -->  
	
  <script type="text/javascript" src="${pageContext.request.contextPath}/net/geoprism/Localized.js.jsp"></script>
  
  <script type="text/javascript">     
    $(document).ready(function(){      
      com.runwaysdk.ui.Manager.setFactory("JQuery");
    });
  </script>
  
  <jwr:script src="/bundles/builder.js" useRandomParam="false"/>   
</head>
<body ng-app="dashboard-menu" ng-cloak>

  <c:if test="${not empty param.errorMessage}">
    <div class="error-message">
      <p>${param.errorMessage}</p>
    </div>
  </c:if>  
  
  <div id="container" ng-controller="DashboardMenuController as ctrl" >
    <header id="header">
    	<div id="header-link-container" class="text-right">
	      <a href="${pageContext.request.contextPath}/prism/home" title="<gdb:localize key="userDashboards.menuTooltip"/>">
	        <img id="logo-icon" class="img-responsive" src="${pageContext.request.contextPath}/logo/view?id=logo" alt="logo"/>
	      </a>
	      <p id="user-link-container" class="text-right">
	        <a class="user-command-link" href="${pageContext.request.contextPath}/session/logout"><gdb:localize key="userDashboards.logout"/></a>
	      </p>
	    </div>
      <div class="heading text-center"><gdb:localize key="userDashboards.heading"/></div>
    </header>
    
    <div class="row"></div>
    <div class="col-md-2"></div>
    <div class="col-md-8">
    	<div ng-repeat="id in ctrl.ids" ng-init="dashboard = ctrl.dashboards[id]" ng-cloak>
    		<div ng-if="($index ) % 3 === 0" class="row">
		        <!-- CREATE DASHBOARD CARD #1 
		        	 Why 3 semi-redundant blocks you might ask? To wrap groups of 3 in a bootstrap ROW. -->
		        <div ng-if="ctrl.dashboards[ctrl.ids[$index]]" class="col-sm-6 col-md-4">
		          <div  class="thumbnail text-center">
		            <a ng-href="${pageContext.request.contextPath}/dashboard-map/view?dashboard={{ctrl.dashboards[ctrl.ids[$index]].dashboardId}}" class="" >
		              <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
		              <img ng-src="${pageContext.request.contextPath}/nav/dashboard-thumbnail?dashboardId={{ctrl.dashboards[ctrl.ids[$index]].dashboardId}}" 
		              		onerror="if (this.src != '${pageContext.request.contextPath}/net/geoprism/images/dashboard_icon_small.png') this.src = '${pageContext.request.contextPath}/net/geoprism/images/dashboard_icon_small.png';" 
		              		alt="Dashboard">
		              
		              <div class="caption">
		                <h3>{{ctrl.dashboards[ctrl.ids[$index]].label}}</h3>
		                <p>{{ctrl.dashboards[ctrl.ids[$index]].description}}</p>
						<div ng-if="ctrl.editDashboard" class="dashboard-card-ico-button-container">     
<!-- 		            		<p class="dashboard-thumbnail-subtext focus-area-label"> -->
<!-- 		            			<i class="fa fa-globe focus-area dashboard-thumnail-ico-ctrl"></i> -->
<!-- 		            			{{ctrl.dashboards[ctrl.ids[$index]].focusAreasAsString}} -->
<!-- 		            		</p> -->
<!-- 		            		<div class="dashboard-focus-area-label-mask-grey"></div> -->
<%-- 		            		<div class="dashboard-focus-area-label-mask" title="<gdb:localize key="userDashboards.dashboardFocusAreaTooltip"/> ({{ctrl.dashboards[ctrl.ids[$index]].focusAreasAsString ? ctrl.dashboards[ctrl.ids[$index]].focusAreasAsString : '<gdb:localize key="userDashboards.noFocusAreaMsg"/>'}})"></div> --%>
		            		<div class="dashboard-thumbnail-ico-group">
		            			<a href="#" class="fa fa-cog ico-dashboard-options dashboard-thumnail-ico-ctrl" title="<gdb:localize key="userDashboards.editDashboardTooltip"/>" ng-click="ctrl.edit(ctrl.dashboards[ctrl.ids[$index]].dashboardId)" ></a> 
		            			<a href="#" class="fa fa-clone ico-dashboard dashboard-thumnail-ico-ctrl" title="<gdb:localize key='dashboardViewer.newDashboardTooltip'/>" ng-click="ctrl.cloneDashboard(ctrl.dashboards[ctrl.ids[$index]].dashboardId)"></a>
		            			<a href="#" class="fa fa-trash-o ico-remove dashboard-thumnail-ico-ctrl" title="<gdb:localize key="userDashboards.deleteDashboardTooltip"/>" ng-click="ctrl.remove(ctrl.dashboards[ctrl.ids[$index]].dashboardId)" ></a>
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
		            <a ng-href="${pageContext.request.contextPath}/dashboard-map/view?dashboard={{ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId}}" class="" >
		              
		              <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
		              <img ng-src="${pageContext.request.contextPath}/nav/dashboard-thumbnail?dashboardId={{ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId}}" onerror="if (this.src != '${pageContext.request.contextPath}/net/geoprism/images/dashboard_icon_small.png') this.src = '${pageContext.request.contextPath}/net/geoprism/images/dashboard_icon_small.png';" alt="Dashboard">
		              
		              <div class="caption">
		                <h3>{{ctrl.dashboards[ctrl.ids[$index + 1]].label}}</h3>
		                <p>{{ctrl.dashboards[ctrl.ids[$index + 1]].description}}</p>
		          		<div ng-if="ctrl.editDashboard" class="dashboard-card-ico-button-container">     
		            		<div class="dashboard-thumbnail-ico-group">
		            			<a href="#" class="fa fa-cog ico-dashboard-options dashboard-thumnail-ico-ctrl" title="<gdb:localize key="userDashboards.editDashboardTooltip"/>" ng-click="ctrl.edit(ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId)" ></a> 
		            			<a href="#" class="fa fa-clone ico-dashboard dashboard-thumnail-ico-ctrl" title="<gdb:localize key='dashboardViewer.newDashboardTooltip'/>" ng-click="ctrl.cloneDashboard(ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId)"></a>
		            			<a href="#" class="fa fa-trash-o ico-remove dashboard-thumnail-ico-ctrl" title="<gdb:localize key="userDashboards.deleteDashboardTooltip"/>" ng-click="ctrl.remove(ctrl.dashboards[ctrl.ids[$index + 1]].dashboardId)" ></a>
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
		            <a ng-href="${pageContext.request.contextPath}/dashboard-map/view?dashboard={{ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId}}" class="" >
		              
		              <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
		              <img ng-src="${pageContext.request.contextPath}/nav/dashboard-thumbnail?dashboardId={{ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId}}" onerror="if (this.src != '${pageContext.request.contextPath}/net/geoprism/images/dashboard_icon_small.png') this.src = '${pageContext.request.contextPath}/net/geoprism/images/dashboard_icon_small.png';" alt="Dashboard">
		              
		              <div class="caption">
		                <h3>{{ctrl.dashboards[ctrl.ids[$index + 2]].label}}</h3>
		                <p>{{ctrl.dashboards[ctrl.ids[$index + 2]].description}}</p>
		          		<div ng-if="ctrl.editDashboard" class="dashboard-card-ico-button-container">     
		            		<div class="dashboard-thumbnail-ico-group">
		            			<a href="#" class="fa fa-cog ico-dashboard-options dashboard-thumnail-ico-ctrl" title="<gdb:localize key="userDashboards.editDashboardTooltip"/>" ng-click="ctrl.edit(ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId)" ></a> 
		            			<a href="#" class="fa fa-clone ico-dashboard dashboard-thumnail-ico-ctrl" title="<gdb:localize key='dashboardViewer.newDashboardTooltip'/>" ng-click="ctrl.cloneDashboard(ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId)"></a>
		            			<a href="#" class="fa fa-trash-o ico-remove dashboard-thumnail-ico-ctrl" title="<gdb:localize key="userDashboards.deleteDashboardTooltip"/>" ng-click="ctrl.remove(ctrl.dashboards[ctrl.ids[$index + 2]].dashboardId)" ></a>
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
    <div class="col-md-2"></div>
       
    <builder-dialog></builder-dialog>
    
    <uploader-dialog></uploader-dialog>
    
    <clone-form></clone-form>
  </div>
</body>