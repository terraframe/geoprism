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

<%@page import="com.runwaysdk.system.gis.geo.GeoEntityDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.GeoEntityViewDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalDisplayLabelDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.LocatedInDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.GeoEntityDisplayLabelDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.GeoEntityController" %>
<%@page import="com.runwaysdk.business.ontology.OntologyStrategyIF" %>
<%@page import="com.runwaysdk.RunwayExceptionDTO" %>

<c:set var="page_title" scope="request" value="Manage Geoentities"/>

<%@page import="com.runwaysdk.constants.DeployProperties" %>
<%
  String webappRoot = "/" + DeployProperties.getAppName() + "/";
%>

<script type="text/javascript" src="<% out.print(webappRoot); %>jquerytree/tree.jquery.js"></script>
<link rel="stylesheet" href="<% out.print(webappRoot); %>jquerytree/jqtree.css" ></link>

<!-- Runway Factory -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/runway.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/widget/Widget.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/form/Form.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/list/List.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/contextmenu/ContextMenu.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/button/Button.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/overlay/Overlay.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/busymodal/BusyModal.js"></script>


<!-- JQuery -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Factory.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Dialog.js"></script>

<!-- Runway Generic -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/RunwayControllerForm.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/RunwayControllerFormDialog.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/ontology/TermTree.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/ontology/GeoEntityTree.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/geodashboard/Form.js"></script>

<link rel="stylesheet" type="text/css" href="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/runway/default.css" />

<%@page import="com.runwaysdk.constants.ClientConstants"%>
<%@page import="com.runwaysdk.constants.ClientRequestIF"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%@page import="com.runwaysdk.business.BusinessDTO"%>
<%@page import="com.runwaysdk.business.RelationshipDTO"%>
<%@page import="com.runwaysdk.business.RelationshipDTO"%>
<%@page import="com.runwaysdk.web.json.JSONController"%>

<%
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
%>

<%!
public void doIt(ServletRequest request, JspWriter out) throws Exception {
  ClientRequestIF clientRequest = (ClientRequestIF) request.getAttribute(ClientConstants.CLIENTREQUEST);
  
  /* OntologyStrategyIF strategy = Universal.getStrategy();
  strategy.initialize(AllowedInDTO.CLASS); */
  
  /* try {
  UniversalDTO termRoot = new UniversalDTO(clientRequest);
  termRoot.getDisplayLabel().setValue("defaultLocale", "Earth");
  termRoot.setName(String.valueOf(Math.random() * 100000));
  termRoot.apply();
  out.println("g_idTermRoot = '" + termRoot.getId() + "'");
  }
  catch (RunwayExceptionDTO e) {
    System.out.println(e.getDeveloperMessage());
  } */
}
%>

<script type="text/javascript">
<%
  // use a try catch before printing out the definitions, otherwise, if an
  // error occurs here, javascript spills onto the actual page (ugly!)
  try
  {
    String js = JSONController.importTypes(clientRequest.getSessionId(), new String[] {
      GeoEntityDTO.CLASS, LocatedInDTO.CLASS, GeoEntityDisplayLabelDTO.CLASS, GeoEntityController.CLASS, UniversalDTO.CLASS, UniversalDisplayLabelDTO.CLASS,
      GeoEntityViewDTO.CLASS
      }, true);
    out.print(js);
  }
  catch(Exception e)
  {
    // perform cleanup
    throw e;
  }

  /* doIt(request, out); */
%>
</script>

<div id="tree"></div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  var tree = new com.runwaysdk.geodashboard.ontology.GeoEntityTree({
    termType : <% out.print("\"" + GeoEntityDTO.CLASS + "\""); %>,
    relationshipType : <% out.print("\"" + LocatedInDTO.CLASS + "\""); %>,
    rootTerm : <% out.print("\"" + GeoEntityDTO.getRoot(clientRequest).getId() + "\""); %>,
    crud: {
      create: { // This configuration gets merged into the jquery create dialog.
                // The height of an attribute is about 45
        height: 370
      },
      update: {
        height: 370
      }
    }
  });
  tree.render("#tree");
</script>
