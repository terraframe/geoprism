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

<gdb:localize var="page_title" key="ontologies.title"/>

<!-- Ontologies CSS -->
<jwr:style src="/bundles/termtree.css" useRandomParam="false"/>  

<!-- Ontologies Javascript -->
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
<jwr:script src="/bundles/termtree.js" useRandomParam="false"/>
<jwr:script src="/bundles/ontology.js" useRandomParam="false"/>

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
      var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
  	    termType : "${type}",
	      relationshipTypes : [ "${relationshipType}" ],
        rootTerms : [ {termId : "${rootId}"} ],
        editable : true,
        /* checkable: true, */
        crud: {
          create: { // This configuration gets merged into the jquery create dialog.
            height: 290
          },
          update: {
            height: 290
          }
        },
        onCreateLi : function(node, $li) {
          if (!node.phantom) {
            if(node.hasProblem){
              var msg = "";
              var msgEls = $("#problems-list").find('[data-classifier="'+node.runwayId+'"]');
              
              for(var i=0; i<msgEls.length; i++){
                var msgEl = msgEls[i];
                msg +=  i+1 + "." + "&nbsp;&nbsp;" + $(msgEl).find(".classifier-problem-msg").text(); // gets the message from the problems panel
                if(i < msgEls.length - 1){
                  msg += "<br/><br/>";
                }
              }
              $li.find("span").parent().append("<i data-problemid='"+ node.problemId +"' data-classifier='"+ node.runwayId +"' class='fa fa-times-circle classifier-problem-msg-icon classifier-problem-error'></i>");
              $li.find("i").tooltip({
                items: "i",
                content: msg,
                tooltipClass: "geoentity-problem-tooltip"
              });
            }
          }
        }
      });
      tree.renderWithProblems("#tree", views);
    },
    onFailure : function(ex) {
      // TODO fix this to use standard error popup
      alert("Error reading classifier problems");
    }
  });
    
  com.runwaysdk.geodashboard.ontology.Classifier.getAllProblems(request);      
});
</script>
