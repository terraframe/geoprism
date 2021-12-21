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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>

<gdb:localize var="page_title" key="universal.title"/>

<!-- Universal CSS -->
<jwr:style src="/bundles/widget.css" useRandomParam="false"/>  
<jwr:style src="/bundles/termtree.css" useRandomParam="false"/>  

<!-- Universal Javascript -->
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
<jwr:script src="/bundles/termtree.js" useRandomParam="false"/>
<jwr:script src="/bundles/universal.js" useRandomParam="false"/>
<script type="text/javascript">${js}</script>

</head>

<div id="tree"></div>

<script type="text/javascript">
// var $tree = document.getElementById('main-content-frame').contentWindow.document.universaltree.getImpl();

  com.runwaysdk.ui.DOMFacade.execOnPageLoad(function(){
    com.runwaysdk.ui.Manager.setFactory("JQuery");
    
    document.universaltree = new net.geoprism.ontology.UniversalTree({
  	  termType : "${type}",
	  relationshipTypes : [ "${allowedInType}", "${isARelationshipType}" ],
      rootTerms : [ { termId : "${rootId}" } ],
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
