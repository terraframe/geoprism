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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>


<head>

<gdb:localize var="page_title" key="geoEntity.title"/>

<!-- Universal CSS -->
<jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
<jwr:style src="/bundles/termtree.css" useRandomParam="false"/>  

<jwr:style src="/com/runwaysdk/geodashboard/font-awesome-font-icons/font-awesome-4.3.0/css/font-awesome.min.css" useRandomParam="false"/> 

<!-- Universal Javascript -->
<jwr:script src="/bundles/termtree.js" useRandomParam="false"/>
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
<jwr:script src="/bundles/geoentity.js" useRandomParam="false"/>
<script type="text/javascript">${js}</script>
</head>

<div id="tree-container">
  <div id="tree"></div>
  <div id="problem-panel">
    <h4 id="problem-panel-heading"><gdb:localize key="geoEntity.problems.header"/></h4>
    <ul id="problems-list">
      <h4 id="problem-panel-noissue-msg" style="display:none;"><gdb:localize key="geoEntity.problems.noproblems-msg"/></h4>
    </ul>
  </div>
</div>

<script type="text/javascript">

  com.runwaysdk.ui.DOMFacade.execOnPageLoad(function(){
    com.runwaysdk.ui.Manager.setFactory("JQuery");

    var request = new Mojo.ClientRequest({
      onSuccess: function(views){
            
        var tree = new com.runwaysdk.geodashboard.ontology.GeoEntityTree({
          termType : "${type}",
          relationshipTypes : [ "${relationshipType}" ],
          rootTerms : [ { termId : "${rootId}"} ],
          editable : true,
          crud: {
            create: { // This configuration gets merged into the jquery create dialog.
              height: 320
            },
            update: {
              height: 320
            }
          }
        });
        tree.render("#tree", views);
      },
      onFailure : function(ex) {
    	// TODO fix this to use standard error popup
        alert("Error reading entity problems");
      }
    });
      
    com.runwaysdk.geodashboard.GeoEntityUtil.getAllProblems(request);    
  });
</script>
