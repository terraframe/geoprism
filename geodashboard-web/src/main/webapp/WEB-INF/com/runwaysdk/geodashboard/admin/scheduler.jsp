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
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>

<gdb:localize var="page_title" key="scheduler.title"/>

<!-- Scheduler CSS -->
<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
<jwr:style src="/com/runwaysdk/geodashboard/scheduler/Scheduler.css" useRandomParam="false"/>  

<!-- Scheduler Javascript -->
<jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
<jwr:script src="/bundles/scheduler.js" useRandomParam="false"/>

<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>
<%@page import="com.runwaysdk.system.scheduler.ExecutableJobDTO" %>
<%@page import="com.runwaysdk.system.scheduler.ExecutableJobDescriptionDTO" %>
<%@page import="com.runwaysdk.system.scheduler.QualifiedTypeJobDTO" %>
<%@page import="com.runwaysdk.system.scheduler.JobHistoryDTO" %>
<%@page import="com.runwaysdk.system.scheduler.JobSnapshotDTO" %>
<%@page import="com.runwaysdk.system.scheduler.JobHistoryViewDTO" %>
<%@page import="com.runwaysdk.system.scheduler.JobHistoryHistoryInformationDTO" %>

<script type="text/javascript">
<%
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);

  // use a try catch before printing out the definitions, otherwise, if an
  // error occurs here, javascript spills onto the actual page (ugly!)
  try
  {
    String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] {
      ExecutableJobDTO.CLASS, ExecutableJobDescriptionDTO.CLASS, QualifiedTypeJobDTO.CLASS, JobHistoryDTO.CLASS, JobSnapshotDTO.CLASS,
      JobHistoryViewDTO.CLASS, JobHistoryHistoryInformationDTO.CLASS
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

</head>

<div id="scheduler"></div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  var ut = new com.runwaysdk.geodashboard.scheduler.Scheduler();
  ut.render("#scheduler");
  
</script>
