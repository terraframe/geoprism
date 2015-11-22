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
            
            
<!--             <div class="row-holder"> -->
<!--               <div class="label-holder"> -->
<%--                 <strong><gdb:localize var="dl_form_nameTheLayer" key="DashboardThematicLayer.form.nameTheLayer"/>${dl_form_nameTheLayer}</strong> --%>
<!--               </div> -->
<!--               <div class="holder"> -->
<%-- <mjl:component param="layer" item="${layer}"> --%>
<%--                 <label class="none" for="f312">${layer.nameMd.displayLabel}</label> --%>
<!--                 <span class="text"> -->
<%--                   <input type="text" id="layer.name" value="${layer.name}" name="layer.name" /> --%>
<%--                       <mjl:messages attribute="name" classes="error-message"> --%>
<%--                         <mjl:message /> --%>
<%--                       </mjl:messages> --%>
<!--                 </span> -->
<%-- </mjl:component> --%>
<!--               </div> -->
<!--             </div> -->


			<layer-label></layer-label>


<%-- 			<mjl:component param="style" item="${style}"> --%>
<!-- 	            <div class="row-holder"> -->
<!-- 	              <div class="label-holder style02"> -->
<%-- 	                <strong><gdb:localize var="dl_form_labelsAndValues" key="DashboardThematicLayer.form.labelsAndValues"/>${dl_form_labelsAndValues}</strong> --%>
<!-- 	              </div> -->
<!-- 	              <div class="holder"> -->
<!-- 	                <div class="row-holder"> -->
<!-- 	                  <div class="check-block"> -->
<%-- 	                    <input id="f51" type="checkbox" <c:if test="${style.enableValue}">checked</c:if> name="style.enableValue"></input> --%>
<%-- 	                    <label for="f51">${style.enableValueMd.displayLabel}</label> --%>
<%-- 	                    <mjl:messages attribute="enableValue" classes="error-message"> --%>
<%-- 	                      <mjl:message /> --%>
<%-- 	                    </mjl:messages> --%>
<!-- 	                  </div> -->
<!-- 	                  <div class="check-block"> -->
<%-- 	                    <input id="f94" type="checkbox" <c:if test="${style.enableLabel}">checked</c:if> name="style.${style.enableLabelMd.name}"></input> --%>
<%-- 	                    <label for="f94">${style.enableLabelMd.displayLabel}</label> --%>
<!-- 	                  </div>               -->
<!-- 	                </div> -->
<!-- 	                <div class="row-holder"> -->
<!-- 	                  <div class="cell style02"> -->
<%-- 	                    <label for="f55">${style.labelFontMd.displayLabel}</label> --%>
<!-- 	                    <div class="select-holder"> -->
<%-- 	                      <select class="font-select" name="style.${style.labelFontMd.name}" id="f55"> --%>
<%-- 	                        <c:forEach items="${fonts}" var="font"> --%>
<%-- 	                          <c:choose> --%>
<%-- 	                            <c:when test="${style.labelFont == font}"> --%>
<%-- 	                              <option value="${font}" style="font-family:${font}" selected="selected">${font}</option> --%>
<%-- 	                            </c:when> --%>
<%-- 	                            <c:otherwise> --%>
<%-- 	                              <option value="${font}" style="font-family:${font}" >${font}</option> --%>
<%-- 	                            </c:otherwise> --%>
<%-- 	                          </c:choose> --%>
<%-- 	                        </c:forEach> --%>
<!-- 	                      </select> -->
<!-- 	                    </div> -->
<!-- 	                  </div> -->
<!-- 	                  <div class="cell"> -->
<%-- 	                    <label for="f95">${style.labelSizeMd.displayLabel}</label> --%>
<!-- 	                    <div class="select-holder"> -->
<%-- 	                      <select class="size-select" id="f95" name="style.${style.labelSizeMd.name}"> --%>
<%-- 	                        <c:forEach begin="0" end="30" var="size"> --%>
<%-- 	                          <c:choose> --%>
<%-- 	                            <c:when test="${style.labelSize == size}"> --%>
<%-- 	                              <option selected="selected" value="${size}">${size}</option> --%>
<%-- 	                            </c:when> --%>
<%-- 	                            <c:otherwise> --%>
<%-- 	                              <option value="${size}">${size}</option> --%>
<%-- 	                            </c:otherwise> --%>
<%-- 	                          </c:choose> --%>
<%-- 	                        </c:forEach> --%>
<!-- 	                      </select> -->
<!-- 	                    </div> -->
<!-- 	                  </div> -->
<!-- 	                  <div class="cell"> -->
<%-- 	                    <span>${style.labelColorMd.displayLabel}</span> --%>
<!-- 	                    <div class="color-holder"> -->
<!-- 	                      <a href="#" class="color-choice"> -->
<%-- 	                        <span class="ico" style="background:${style.labelColor};">icon</span> --%>
<!-- 	                        <span class="arrow">arrow</span> -->
<%-- 	                        <input type="hidden" class="color-input" name="style.${style.labelColorMd.name}" value="${style.labelColor}" /> --%>
<!-- 	                      </a> -->
<!-- 	                    </div> -->
<!-- 	                  </div> -->
<!-- 	                  <div class="cell"> -->
<%-- 	                    <span>${style.labelHaloMd.displayLabel}</span> --%>
<!-- 	                    <div class="color-holder"> -->
<!-- 	                      <a href="#" class="color-choice"> -->
<%-- 	                        <span class="ico" style="background:${style.labelHalo};">icon</span> --%>
<!-- 	                        <span class="arrow">arrow</span> -->
<%-- 	                        <input type="hidden" class="color-input" name="style.${style.labelHaloMd.name}" value="${style.labelHalo}" /> --%>
<!-- 	                      </a> -->
<!-- 	                    </div> -->
<!-- 	                  </div> -->
<!-- 	                  <div class="cell"> -->
<%-- 	                  <label for="f54">${style.labelHaloWidthMd.displayLabel}</label> --%>
<!-- 	                    <div class="select-holder"> -->
<%-- 	                      <select class="size-select" name="style.${style.labelHaloWidthMd.name}" id="f54"> --%>
<%-- 	                        <c:forEach begin="0" end="15" var="size"> --%>
<%-- 	                          <c:choose> --%>
<%-- 	                            <c:when test="${style.labelHaloWidth == size}"> --%>
<%-- 	                              <option selected="selected" value="${size}">${size}</option> --%>
<%-- 	                            </c:when> --%>
<%-- 	                            <c:otherwise> --%>
<%-- 	                              <option value="${size}">${size}</option> --%>
<%-- 	                            </c:otherwise> --%>
<%-- 	                          </c:choose> --%>
<%-- 	                        </c:forEach> --%>
<!-- 	                      </select> -->
<!-- 	                    </div> -->
<!-- 	                  </div> -->
<!-- 	                </div> -->
<!-- 	              </div> -->
<!-- 	            </div> -->
<%-- 			</mjl:component> --%>
            
            
            
            
            
            <layer-geo-node></layer-geo-node>
            
            
            
            
			<!-- AGGREGATION SETTINGS -->
			
<!-- 			<div id="geonode-holder" class="row-holder"> -->
<!--               <div class="label-holder style03"> -->
<%--                 <strong><gdb:localize var="dl_form_defineGeoNode" key="DashboardThematicLayer.form.defineGeoNode"/>${dl_form_defineGeoNode}</strong> --%>
<!--               </div> -->
<!--               <div class="holder add"> -->
<%--               	<mjl:component param="layer" item="${layer}"> --%>
<!--                 <div class="box"> -->
<%--                   <label for="geonode-select"><gdb:localize var="dl_form_geoNode" key="DashboardThematicLayer.form.geoNode"/>${dl_form_geoNode}</label> --%>
<!--                   <div class="select-box"> -->
<!-- 	                    <select id="geonode-select" class="method-select" name="layer.geoNode"> -->
<%-- 	                       <c:forEach items="${nodes}" var="node"> --%>
<%-- 		                         <c:choose> --%>
<%-- 		                           <c:when test="${layer.geoNodeId == node.id}"> --%>
<%-- 				                         <option value="${node.id}" data-type="${node.type}" selected="selected">${node.geoEntityAttribute.displayLabel}</option> --%>
<%-- 		                           </c:when> --%>
<%-- 		                           <c:otherwise> --%>
<%-- 		                           		<option value="${node.id}" data-type="${node.type}" >${node.geoEntityAttribute.displayLabel}</option> --%>
<%-- 		                           </c:otherwise> --%>
<%-- 		                         </c:choose> --%>
<%-- 	                      </c:forEach> --%>
<!-- 	                    </select> -->
<!--                   </div> -->
<!--                 </div> -->
<%-- 				</mjl:component> --%>
<!--               </div> -->
<!--             </div> -->
			
			
			
			 <layer-aggregation></layer-aggregation>
			
			
			
			
<!--             <div id="agg-level-holder" class="row-holder" style="display:none;"> -->
<!--               <div class="label-holder style03"> -->
<%--                 <strong><gdb:localize var="dl_form_defineAggMeth" key="DashboardThematicLayer.form.defineAggMeth"/>${dl_form_defineAggMeth}</strong> --%>
<!--               </div> -->
<!--               <div class="holder add"> -->
<%--               	<mjl:component param="layer" item="${layer}"> --%>
<!-- 	                <div class="box"> -->
<%-- 	                  <label for="agg-level-dd"><gdb:localize var="dl_form_groupBy" key="DashboardThematicLayer.form.groupBy"/>${dl_form_groupBy}</label> --%>
<!-- 	                  <div class="select-box"> -->
<%-- 		                    <select id="agg-level-dd" class="method-select" name="layer.${layer.aggregationStrategyMd.name}"> --%>
<!-- 		                    	 <option value="" data-aggType=""></option> -->
<!-- 								 OPTIONS ARE DYNAMICALLY SET IN JAVASCRIPT -->
<!-- 		                    </select> -->
<!-- 	                  </div> -->
<!-- 	                </div> -->
<!-- 	                <div class="box"> -->
<%-- 	                  <label for="agg-method-dd"><gdb:localize var="dl_form_accordingTo" key="DashboardThematicLayer.form.accordingTo"/>${dl_form_accordingTo}</label> --%>
<!-- 	                  <div class="select-box"> -->
<%-- 	                    <select id="agg-method-dd" class="method-select" name="layer.${layer.aggregationTypeMd.name}"> --%>
<%-- 	                      <c:forEach items="${aggregations}" var="aggregation"> --%>
<%-- 	                         	<c:choose> --%>
<%-- 	                           		<c:when test="${aggregation.displayLabel.value == activeAggregation}"> --%>
<%-- 	                             		<option value="${aggregation.enumName}" selected="selected"> --%>
<%-- 	                               			${aggregation.displayLabel.value} --%>
<!-- 	                             		</option> -->
<%-- 	                           		</c:when> --%>
<%-- 	                           		<c:otherwise> --%>
<%-- 	                             		<option value="${aggregation.enumName}"> --%>
<%-- 	                               			${aggregation.displayLabel.value} --%>
<!-- 	                             		</option> -->
<%-- 	                           		</c:otherwise> --%>
<%-- 	                         	</c:choose> --%>
<%-- 	                      </c:forEach> --%>
<!-- 	                    </select> -->
<!-- 	                  </div> -->
<!-- 	                </div> -->
<%-- 				</mjl:component> --%>
<!--               </div> -->
<!--             </div> -->
            
   
   
   			<layer-types></layer-types>
   
   
            
            <!-- 
               Begin Layer Types
             -->
		 <!--	<mjl:component param="layer" item="${layer}">
	            <div id="geom-type-holder" class="row-holder" style="display:none">
	              <div class="label-holder style04">
	                <strong><gdb:localize var="dl_form_chooseLayerType" key="DashboardThematicLayer.form.chooseLayerType"/>${dl_form_chooseLayerType}</strong>
	              </div>
	              <div class="holder style04">
	                <ul class="nav-tabs type-tabs">
		                <c:forEach items="${layerTypeNames}" var="layerTypeName" varStatus="loop">
			                  <li id="${layerTypeName}"  class="${layerTypeName}  
			                    <c:if test="${layerTypeName == activeLayerTypeName}">
			                        active 
 			                    </c:if> 
			                    	" style="display:none;">
 			                    		<a href="#" data-toggle="tab" data-gdb-tab-type="${layerTypeName}"> 
				                      	<input 
			                        	<c:if test="${layerTypeName == activeLayerTypeName}">
			                          		checked="checked"
			                        	</c:if>
			                        	id="radio${loop.index}" name="layer.layerType" value="${layerTypeName}" type="radio">
			                      	</input>
			                      	<label for="radio${loop.index}">${layerTypeLabels[loop.index]}</label>
			                    	</a>
			                  </li>
		                </c:forEach>
	                </ul>
	              </div>
	            </div>
			</mjl:component> -->
            <!-- 
              End Layer Types
             -->


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
    

