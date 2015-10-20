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
    
    img{
      width: 100%;
      border-radius: 4px;
      opacity: 0.6;
        filter: alpha(opacity=60); 
        transition: opacity .25s ease-in-out;
        -moz-transition: opacity .2s ease-in-out;
        -webkit-transition: opacity .2s ease-in-out;
    }
    
    i.glyphicon.glyphicon-plus{
      font-size: 130px;
        font-weight: bold;
        color: darkgrey;
    }
    
    i.fa.fa-plus-square-o{
       font-size: 149px;
        font-weight: bold;
        color: darkgrey;
    }
    
    .frame-box {position:relative}
    .frame-box:before, .frame-box:after, .frame-box>:first-child:before, .frame-box>:first-child:after {
        position:absolute; content:' ';
        width:35px; height: 35px;
        border-color:darkgrey; 
        border-style:solid; 
    }
    .frame-box:before {top:0;left:0;border-width: 4px 0 0 4px}
    .frame-box:after {top:0;right:0;border-width: 4px 4px 0 0}
    .frame-box>:first-child:before {bottom:0;right:0;border-width: 0 4px 4px 0}
    .frame-box>:first-child:after {bottom:0;left:0;border-width: 0 0 4px 4px}
  </style>
  
</head>
<body>

  <c:if test="${not empty param.errorMessage}">
    <div class="error-message">
      <p>${param.errorMessage}</p>
    </div>
  </c:if>
  
  
  <div id="container">
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
    <div class="row"></div>
       <div class="col-md-3"></div>
       <div class="col-md-6">
       
           <div class="row"> <!-- start of the 1st row -->
       
           <c:forEach items="${dashboards}" var="dashboard" varStatus="counter">
        <div class="col-sm-6 col-md-4">
          <div class="thumbnail text-center">
            <a href="DashboardViewer?dashboard=${dashboard.id}" class="" >
            <!-- NOTE: the onerror method that sets the default icon if now saved dashboard exists -->
              <img src="/mapthumb/getDashboardMapThumbnail?dashboardId=${dashboard.id}" onerror="if (this.src != 'com/runwaysdk/geodashboard/images/dashboard_icon.png') this.src = 'com/runwaysdk/geodashboard/images/dashboard_icon.png';" alt="Dashboard">
              <div class="caption">
                <h3>${dashboard.displayLabel.value}</h3>
<!--           <p>"SOME BODY TEXT"</p> -->
              </div>
            </a>
          </div>
        </div>
        
        <c:choose>
          <c:when test="${counter.last}">
              <c:if test="${counter.count % 3 == 0}"> 
                </div>  <!-- close the last row -->
                  <div class="row">  <!-- start a new row if the last row has all three thumbnails -->
                  </c:if>
                
             <div class="col-sm-6 col-md-4">
              <div class="thumbnail text-center">
                <a href="#" class="new-dashboard-btn" >
<!--                   <i class="glyphicon glyphicon-plus"></i> -->
                    <div class="frame-box">
                      <div class="inner-frame-box">
                        <i class="glyphicon glyphicon-plus"></i>
                      </div>
                    </div>
                    <div class="caption">
                      <h3><gdb:localize key="userDashboards.newDashboardTitle"/></h3>
                    </div>
                  </a>
              </div>
            </div>
            
           </div> <!-- close the last row -->
          </c:when>
          <c:when test="${counter.count % 3 == 0}">
            </div> <!-- close the row after three thumbnails -->
            <div class="row"> <!-- now start the next row -->
          </c:when>
             </c:choose>
         </c:forEach>
         
       </div> <!-- close the middle column -->
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
          });
      
          $('.new-dashboard-btn').on("click", function(){
            var dashboard = new com.runwaysdk.geodashboard.Dashboard();
            var title = com.runwaysdk.Localize.get("com.runwaysdk.ui.userstable.DashboardTable.dialogNewDashboardTitle", "New Dashboard");
            var listener = {
              handleEvent : function(event) {
                if(event.getEventType() === com.runwaysdk.ui.dashboardstable.DashboardFormBuilderEvent.APPLY_SUCCESS) {
                  var dashboard = event.getDashboard();
                        
                  window.location.href = 'DashboardViewer?dashboard=' + dashboard.getId();
                }
              }
            };
          
            new com.runwaysdk.ui.userstable.DashboardFormDialog(dashboard, listener).render(title);        
          });
    });
  </script>
  
  
