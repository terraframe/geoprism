<%--

    Copyright (c) 2013 TerraFrame, Inc. All rights reserved.

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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@page import="com.runwaysdk.constants.DeployProperties" %>


<gdb:localize var="page_title" key="splash.pagetitle" />

<jsp:include page="../../../../WEB-INF/templates/header.jsp"></jsp:include>


<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>

<gdb:localize var="logoalt" key="splash.logoalt" />
<img src="<%out.print(webappRoot);%>com/runwaysdk/geodashboard/images/splash_logo.png" alt="${logoalt}">

<br/>
<br/>

<header id="header">
  <h1><gdb:localize key="splash.header" /></h1>
</header>

<p><gdb:localize key="splash.powered" /></p>

<script type="text/javascript">
com.runwaysdk.ui.Manager.setFactory("JQuery");
$(document).ready(function(){
  
  $('#blockMe').on('click', function(e){
    
    var el = e.target;
    
    

    
    var request = new GDB.BlockingClientRequest({
      onSuccess : function(){
        
      }
    });
    request.onSend();
    
    setTimeout(function(){
      request.onComplete();
      el.innerHTML += "\n<br />CLICKED";
    }, 5000);
  });
  
});
</script>

<div id="blockMe" style="width:400px; height:350px; border:1px solid black; background-color:ccc">
  <b>BLOCK ME</b>
</div>

<jsp:include page="../../../../WEB-INF/templates/footer.jsp"></jsp:include>
