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
<jsp:include page="../templates/header.jsp"></jsp:include>

<header id="header">
  <h1>User Accounts</h1>
</header>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>

<script type="text/javascript" src="<% out.print(webappRoot); %>jquery/datatables/js/jquery.dataTables.min.js"></script>
<link rel="stylesheet" href="<% out.print(webappRoot); %>jquery/datatables/css/jquery.dataTables.css" ></link>
<link rel="stylesheet" href="<% out.print(webappRoot); %>jquery/datatables/css/jquery.dataTables_themeroller.css" ></link>

<!-- Runway Factory -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/runway.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/widget/Widget.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/list/List.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/form/Form.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/contextmenu/ContextMenu.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/button/Button.js"></script>

<!-- Generic -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/datasource/DataSourceIF.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/datasource/Events.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/datasource/DataSourceFactory.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/datasource/BaseServerDataSource.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/datasource/ServerDataSource.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/DataTable.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/Column.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/Events.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/Row.js"></script>

<!-- JQuery -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Factory.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/TabPanel.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Dialog.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/datatable/datasource/ServerDataSource.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/datatable/datasource/DataSourceFactory.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/datatable/DataTable.js"></script>

<!-- Runway Generic -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/PollingRequest.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/datatable/datasource/InstanceQueryDataSource.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/datatable/datasource/MdMethodDataSource.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/userstable/UsersTable.js"></script>

<link rel="stylesheet" type="text/css" href="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/default.css" />
<link rel="stylesheet" type="text/css" href="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/generic/datatable/DataTable.css" />

<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%@page import="com.runwaysdk.business.BusinessDTO"%>
<%@page import="com.runwaysdk.business.RelationshipDTO"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.system.UsersDTO"%>

<%
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>

<script type="text/javascript">
<%
	// use a try catch before printing out the definitions, otherwise, if an
	// error occurs here, javascript spills onto the actual page (ugly!)
	try
	{
	  String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] {
	      UsersDTO.CLASS
	    }, true);
	  out.print(js);
	  
	}
	catch(Exception e)
	{
	  // perform cleanup
	  throw e;
	}
%>
</script>

<div id="usersTable"></div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  var ut = new com.runwaysdk.ui.userstable.UsersTable();
  ut.render("#usersTable");
  
</script>


<jsp:include page="../templates/footer.jsp"></jsp:include>
