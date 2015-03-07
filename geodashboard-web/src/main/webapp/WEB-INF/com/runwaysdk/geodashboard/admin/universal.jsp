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

<gdb:localize var="page_title" key="universal.title"/>

<!-- Universal CSS -->
<jwr:style src="/com/runwaysdk/geodashboard/ontology/TermTree.css" useRandomParam="false"/>  

<!-- Universal Javascript -->
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
<jwr:script src="/com/runwaysdk/geodashboard/ontology/UniversalTree.js" useRandomParam="false"/>

<%@page import="com.runwaysdk.geodashboard.gis.UniversalExportMenuDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.GeoEntityDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.AllowedInDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.IsARelationshipDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalDisplayLabelDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalController" %>
<%@page import="com.runwaysdk.system.ontology.TermUtilDTO" %>
<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>


<script type="text/javascript">
<%
    ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);

    // use a try catch before printing out the definitions, otherwise, if an
	// error occurs here, javascript spills onto the actual page (ugly!)
	try
	{
	  String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] {
	    UniversalDTO.CLASS, AllowedInDTO.CLASS, UniversalDisplayLabelDTO.CLASS, UniversalController.CLASS, GeoEntityDTO.CLASS, IsARelationshipDTO.CLASS, TermUtilDTO.CLASS, UniversalExportMenuDTO.CLASS
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

<div id="tree"></div>

<script type="text/javascript">
// var $tree = document.getElementById('main-content-frame').contentWindow.document.universaltree.getImpl();

  com.runwaysdk.ui.DOMFacade.execOnPageLoad(function(){
    com.runwaysdk.ui.Manager.setFactory("JQuery");
    
    document.universaltree = new com.runwaysdk.geodashboard.ontology.UniversalTree({
      termType : <% out.print("\"" + UniversalDTO.CLASS + "\""); %>,
      relationshipTypes : [ <% out.print("\"" + AllowedInDTO.CLASS + "\""); %>, <% out.print("\"" + IsARelationshipDTO.CLASS + "\""); %> ],
      rootTerms : [ { termId: <% out.print("\"" + UniversalDTO.getRoot(clientRequest).getId() + "\""); %> } ],
      editable : true,
      /* checkable: true, */
      crud: {
        create: { // This configuration gets merged into the jquery create dialog.
          height: 290
        },
        update: {
          height: 290
        }
      }
    });
    document.universaltree.render("#tree");
  });
</script>
