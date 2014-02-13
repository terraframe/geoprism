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

<%@page import="com.runwaysdk.system.gis.geo.Universal" %>
<%@page import="com.runwaysdk.system.gis.geo.GeoEntityDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.AllowedInDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalDisplayLabelDTO" %>
<%@page import="com.runwaysdk.system.gis.geo.UniversalController" %>
<%@page import="com.runwaysdk.business.ontology.OntologyStrategyIF" %>
<%@page import="com.runwaysdk.RunwayExceptionDTO" %>

<header id="header">
  <h1>Manage Geoentities</h1>
</header>

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


<!-- JQuery -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Factory.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/factory/jquery/Dialog.js"></script>

<!-- Runway Generic -->
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/RunwayControllerForm.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/ontology/TermTree.js"></script>
<script type="text/javascript" src="<% out.print(webappRoot); %>com/runwaysdk/ui/ontology/GeoEntityTree.js"></script>

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
      UniversalDTO.CLASS, AllowedInDTO.CLASS, UniversalDisplayLabelDTO.CLASS, UniversalController.CLASS
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
  
  var tree = new com.runwaysdk.ui.ontology.GeoEntityTree({
    termType : <% out.print("\"" + UniversalDTO.CLASS + "\""); %>,
    relationshipType : <% out.print("\"" + AllowedInDTO.CLASS + "\""); %>,
    rootTerm : <% out.print("\"" + Universal.getUniversal(Universal.ROOT).getId() + "\""); %>,
  });
  tree.render("#tree");
</script>


<jsp:include page="../templates/footer.jsp"></jsp:include>
