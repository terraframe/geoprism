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
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<%@page import="com.runwaysdk.system.gis.geo.LocatedInDTO" %>
<%@page import="com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO" %>
<%@page import="com.runwaysdk.geodashboard.ontology.ClassifierDTO" %>    
    
<head>

<!-- Ontologies CSS -->
<jwr:style src="/com/runwaysdk/geodashboard/ontology/TermTree.css" useRandomParam="false"/>  

<!-- <script type="text/javascript" src="/com/runwaysdk/geodashboard/dashboard/DashboardLayerForm.js"></script> -->

<!-- Ontologies Javascript -->
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
<jwr:script src="/bundles/ontology.js" useRandomParam="false"/>

<script type="text/javascript">${js}</script>

</head>

<!-- Include the types of this form to get the default values the MdAction needs -->
<mjl:component param="style" item="${style}"></mjl:component>

    <div id="DashboardLayer-mainDiv" class="modal-dialog" ng-controller="LayerFormController" ng-init="init('${layer.id}', '${style.newInstance}', '${layer.geoNodeId}', '${mdAttributeId}', '${mapId}' )">
      <div class="modal-content">
        <div class="heading">
          <c:if test="${style.newInstance}">
            <h1><gdb:localize key="DashboardThematicLayer.form.newHeading"/>${activeMdAttributeLabel}</h1>          
          </c:if>
          <c:if test="${!style.newInstance}">
            <h1><gdb:localize key="DashboardThematicLayer.form.editHeading"/>${activeMdAttributeLabel}</h1>          
          </c:if>
        </div>
          <fieldset>
          
          <c:if test="${errorMessage != null || errorMessageArray != null}">
            <div class="row-holder">
              <div class="label-holder">
              </div>
              <div class="holder">
                <%@include file="../../../../../../templates/inlineError.jsp" %>
              </div>
            </div>
          </c:if>
            
            
            <layer-name-input></layer-name-input>
            
			<layer-label></layer-label>

            <layer-geo-node></layer-geo-node>
            
			 <layer-aggregation></layer-aggregation>
			
   			<layer-types></layer-types>
   
			<layer-types-style></layer-types-style>




            <div class="row-holder">
              <div class="label-holder"></div>
              <div class="holder">
                <div class="row-holder">
                  <div class="check-block style02">
					<mjl:component param="layer" item="${layer}">
                    	<input id="f65" type="checkbox" <c:if test="${layer.displayInLegend}">checked</c:if> name="layer.${layer.displayInLegendMd.name}" ></input>
                    	<label for="f65">${layer.displayInLegendMd.displayLabel}</label>
					</mjl:component>
                  </div>
                </div>
                <div class="button-holder">
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.create.button" 
							      value="Map It" action="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerController.applyWithStyle.mojo"
							      classes="btn btn-primary"  />
							    <!-- <input id="DashboardThematicLayer-create-button" name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.create.button" value="Map It" class="btn btn-primary" type="button"> -->
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.cancel.button"
							      value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayerController.cancel.mojo"
							      classes="btn btn-default" />
                </div>
              </div>
            </div>
          </fieldset>
      </div>
    </div>
    

