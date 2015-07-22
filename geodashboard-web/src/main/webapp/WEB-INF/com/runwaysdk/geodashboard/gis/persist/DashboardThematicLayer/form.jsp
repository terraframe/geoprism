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

<!-- Ontologies Javascript -->
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>
<jwr:script src="/bundles/ontology.js" useRandomParam="false"/>

<script type="text/javascript">${js}</script>

</head>

<!-- Include the types of this form to get the default values the MdAction needs -->
<mjl:component param="style" item="${style}">
</mjl:component>

    <div id="DashboardLayer-mainDiv" class="modal-dialog">
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
            
            <div class="row-holder">
              <div class="label-holder">
                <strong><gdb:localize var="dl_form_nameTheLayer" key="DashboardThematicLayer.form.nameTheLayer"/>${dl_form_nameTheLayer}</strong>
              </div>
              <div class="holder">
<mjl:component param="layer" item="${layer}">
                <label class="none" for="f312">${layer.nameMd.displayLabel}</label>
                <span class="text">
                  <input type="text" id="layer.name" value="${layer.name}" name="layer.name" />
                      <mjl:messages attribute="name" classes="error-message">
                        <mjl:message />
                      </mjl:messages>
                </span>
</mjl:component>
              </div>
            </div>
			<mjl:component param="style" item="${style}">
	            <div class="row-holder">
	              <div class="label-holder style02">
	                <strong><gdb:localize var="dl_form_labelsAndValues" key="DashboardThematicLayer.form.labelsAndValues"/>${dl_form_labelsAndValues}</strong>
	              </div>
	              <div class="holder">
	                <div class="row-holder">
	                  <div class="check-block">
	                    <input id="f51" type="checkbox" <c:if test="${style.enableValue}">checked</c:if> name="style.enableValue"></input>
	                    <label for="f51">${style.enableValueMd.displayLabel}</label>
	                    <mjl:messages attribute="enableValue" classes="error-message">
	                      <mjl:message />
	                    </mjl:messages>
	                  </div>
	                  <div class="check-block">
	                    <input id="f94" type="checkbox" <c:if test="${style.enableLabel}">checked</c:if> name="style.${style.enableLabelMd.name}"></input>
	                    <label for="f94">${style.enableLabelMd.displayLabel}</label>
	                  </div>              
	                </div>
	                <div class="row-holder">
	                  <div class="cell style02">
	                    <label for="f55">${style.labelFontMd.displayLabel}</label>
	                    <div class="select-holder">
	                      <select class="font-select" name="style.${style.labelFontMd.name}" id="f55">
	                        <c:forEach items="${fonts}" var="font">
	                          <c:choose>
	                            <c:when test="${style.labelFont == font}">
	                              <option value="${font}" style="font-family:${font}" selected="selected">${font}</option>
	                            </c:when>
	                            <c:otherwise>
	                              <option value="${font}" style="font-family:${font}" >${font}</option>
	                            </c:otherwise>
	                          </c:choose>
	                        </c:forEach>
	                      </select>
	                    </div>
	                  </div>
	                  <div class="cell">
	                    <label for="f95">${style.labelSizeMd.displayLabel}</label>
	                    <div class="select-holder">
	                      <select class="size-select" id="f95" name="style.${style.labelSizeMd.name}">
	                        <c:forEach begin="0" end="30" var="size">
	                          <c:choose>
	                            <c:when test="${style.labelSize == size}">
	                              <option selected="selected" value="${size}">${size}</option>
	                            </c:when>
	                            <c:otherwise>
	                              <option value="${size}">${size}</option>
	                            </c:otherwise>
	                          </c:choose>
	                        </c:forEach>
	                      </select>
	                    </div>
	                  </div>
	                  <div class="cell">
	                    <span>${style.labelColorMd.displayLabel}</span>
	                    <div class="color-holder">
	                      <a href="#" class="color-choice">
	                        <span class="ico" style="background:${style.labelColor};">icon</span>
	                        <span class="arrow">arrow</span>
	                        <input type="hidden" class="color-input" name="style.${style.labelColorMd.name}" value="${style.labelColor}" />
	                      </a>
	                    </div>
	                  </div>
	                  <div class="cell">
	                    <span>${style.labelHaloMd.displayLabel}</span>
	                    <div class="color-holder">
	                      <a href="#" class="color-choice">
	                        <span class="ico" style="background:${style.labelHalo};">icon</span>
	                        <span class="arrow">arrow</span>
	                        <input type="hidden" class="color-input" name="style.${style.labelHaloMd.name}" value="${style.labelHalo}" />
	                      </a>
	                    </div>
	                  </div>
	                  <div class="cell">
	                  <label for="f54">${style.labelHaloWidthMd.displayLabel}</label>
	                    <div class="select-holder">
	                      <select class="size-select" name="style.${style.labelHaloWidthMd.name}" id="f54">
	                        <c:forEach begin="0" end="15" var="size">
	                          <c:choose>
	                            <c:when test="${style.labelHaloWidth == size}">
	                              <option selected="selected" value="${size}">${size}</option>
	                            </c:when>
	                            <c:otherwise>
	                              <option value="${size}">${size}</option>
	                            </c:otherwise>
	                          </c:choose>
	                        </c:forEach>
	                      </select>
	                    </div>
	                  </div>
	                </div>
	              </div>
	            </div>
			</mjl:component>
            
            
			<!-- AGGREGATION SETTINGS -->
			
			<div id="geonode-holder" class="row-holder">
              <div class="label-holder style03">
                <strong><gdb:localize var="dl_form_defineGeoNode" key="DashboardThematicLayer.form.defineGeoNode"/>${dl_form_defineGeoNode}</strong>
              </div>
              <div class="holder add">
              	<mjl:component param="layer" item="${layer}">
                <div class="box">
                  <label for="geonode-select"><gdb:localize var="dl_form_geoNode" key="DashboardThematicLayer.form.geoNode"/>${dl_form_geoNode}</label>
                  <div class="select-box">
	                    <select id="geonode-select" class="method-select" name="layer.geoNode">
	                       <c:forEach items="${nodes}" var="node">
		                         <c:choose>
		                           <c:when test="${layer.geoNodeId == node.id}">
				                         <option value="${node.id}" data-type="${node.type}" selected="selected">${node.geoEntityAttribute.displayLabel}</option>
		                           </c:when>
		                           <c:otherwise>
		                           		<option value="${node.id}" data-type="${node.type}" >${node.geoEntityAttribute.displayLabel}</option>
		                           </c:otherwise>
		                         </c:choose>
	                      </c:forEach>
	                    </select>
                  </div>
                </div>
				</mjl:component>
              </div>
            </div>
			
			
            <div id="agg-level-holder" class="row-holder" style="display:none;">
              <div class="label-holder style03">
                <strong><gdb:localize var="dl_form_defineAggMeth" key="DashboardThematicLayer.form.defineAggMeth"/>${dl_form_defineAggMeth}</strong>
              </div>
              <div class="holder add">
              	<mjl:component param="layer" item="${layer}">
	                <div class="box">
	                  <label for="agg-level-dd"><gdb:localize var="dl_form_groupBy" key="DashboardThematicLayer.form.groupBy"/>${dl_form_groupBy}</label>
	                  <div class="select-box">
		                    <select id="agg-level-dd" class="method-select" name="layer.${layer.aggregationStrategyMd.name}">
		                    	 <option value="" data-aggType=""></option>
								 <!-- OPTIONS ARE DYNAMICALLY SET IN JAVASCRIPT -->
		                    </select>
	                  </div>
	                </div>
	                <div class="box">
	                  <label for="agg-method-dd"><gdb:localize var="dl_form_accordingTo" key="DashboardThematicLayer.form.accordingTo"/>${dl_form_accordingTo}</label>
	                  <div class="select-box">
	                    <select id="agg-method-dd" class="method-select" name="layer.${layer.aggregationTypeMd.name}">
	                      <c:forEach items="${aggregations}" var="aggregation">
	                         	<c:choose>
	                           		<c:when test="${aggregation.displayLabel.value == activeAggregation}">
	                             		<option value="${aggregation.enumName}" selected="selected">
	                               			${aggregation.displayLabel.value}
	                             		</option>
	                           		</c:when>
	                           		<c:otherwise>
	                             		<option value="${aggregation.enumName}">
	                               			${aggregation.displayLabel.value}
	                             		</option>
	                           		</c:otherwise>
	                         	</c:choose>
	                      </c:forEach>
	                    </select>
	                  </div>
	                </div>
				</mjl:component>
              </div>
            </div>
            
   
            
            <!-- 
               Begin Layer Types
             -->
			<mjl:component param="layer" item="${layer}">
	            <div id="geom-type-holder" class="row-holder" style="display:none" data-layerTypes="${layerTypeNamesJSON}">
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
			</mjl:component>
            <!-- 
              End Layer Types
             -->

			<mjl:component param="style" item="${style}">
	            <div class="row-holder">
	              <div class="label-holder">
	                <strong><gdb:localize var="dl_form_styleTheLayer" key="DashboardThematicLayer.form.styleTheLayer"/>${dl_form_styleTheLayer}</strong>
	              </div>
	              <div class="holder">
	                <div id="layer-type-styler-container" class="tab-content">
	                  
	                  <!-- BASICPOINT -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'BASICPOINT' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                    
	                    id="tab001basicpoint"
	                  >
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>${dl_form_fill}</strong>
	                      <div class="cell-holder">
		                        <div class="cell">
		                          <span>${style.pointFillMd.displayLabel}</span>
		                          <div class="color-holder">
		                            <a href="#" class="color-choice">
		                              <span class="ico" style="background:${style.pointFill};">icon</span>
		                              <span class="arrow">arrow</span>
		                              <input type="hidden" class="color-input" name="style.${style.pointFillMd.name}" value="${style.pointFill}" />
		                            </a>
		                          </div>
		                        </div>
		                        <div class="cell">
		                          <label for="basic-point-opacity-select">${style.pointOpacityMd.displayLabel}</label>
		                          <div class="text">
		                            <select id="basic-point-opacity-select" class="tab-select" name="style.${style.pointOpacityMd.name}">
		                              <c:forEach step="5" begin="0" end="100" var="size">
		                                <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>                              
		                                <c:choose>
		                                  <c:when test="${style.pointOpacity*100 == size}">
		                                    <option selected="selected" value="${potentialValue}">${size}</option>
		                                  </c:when>
		                                  <c:otherwise>
		                                    <option value="${potentialValue}">${size}</option>
		                                  </c:otherwise>
		                                </c:choose>
		                              </c:forEach>
		                            </select>                          
		                          </div>
		                        </div>
	                      	</div>
	                    </div>
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span>${style.pointStrokeMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.pointStroke};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.pointStrokeMd.name}" value="${style.pointStroke}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="basic-point-stroke-select">${style.pointStrokeWidthMd.displayLabel}</label>
	                          <div class="select-holder">
	                            <select name="style.${style.pointStrokeWidthMd.name}" id="basic-point-stroke-select" class="tab-select">
	                              <c:forEach begin="0" end="15" var="size">
	                                <c:choose>
	                                  <c:when test="${style.pointStrokeWidth == size}">
	                                    <option selected="selected" value="${size}">${size}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                    <option value="${size}">${size}</option>
	                                  </c:otherwise>
	                                </c:choose>
	                              </c:forEach>
	                            </select>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="basic-point-stroke-opacity-select">${style.pointStrokeOpacityMd.displayLabel}</label>
	                          <div class="text">
	                             <select id="basic-point-stroke-opacity-select" class="tab-select" name="style.${style.pointStrokeOpacityMd.name}">
	                              <c:forEach step="5" begin="0" end="100" var="size">
	                                <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
	                                
	                                <c:choose>
	                                  <c:when test="${style.pointStrokeOpacity*100 == size}">
	                                    <option selected="selected" value="${potentialValue}">${size}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                    <option value="${potentialValue}">${size}</option>
	                                  </c:otherwise>
	                                </c:choose>
	                              </c:forEach>
	                            </select>   
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize var="shape_heading" key="DashboardThematicLayer.form.shapeHeading"/>${shape_heading}</strong>
	                      <div class="cell-holder">
		                    	<div class="cell">
		                          <label for="basic-point-radius-select">${style.basicPointSizeMd.displayLabel}</label>
		                          <div class="text">
		                          	<input id="basic-point-radius-select" name="style.${style.basicPointSizeMd.name}" type="text" value="${style.basicPointSize}"></input>
		                          </div>
		                        </div>
	                      </div>
	                    
		                    <div id="point-type-container" class="cell">
			                  <label for="point-type"><gdb:localize var="pt_type" key="DashboardLayer.form.pointType"/>${pt_type}</label>
			                  <div class="select-box">
			                    <select id="point-type" class="method-select" name="style.pointWellKnownName">
			                      <c:forEach items="${pointTypes}" var="type">
			                         	<c:choose>
			                           		<c:when test="${type == activeBasicPointType}">
			                             		<option value="${type}" selected="selected">
			                               			${type}
			                             		</option>
			                           		</c:when>
			                           		<c:otherwise>
			                             		<option value="${type}">
			                               			${type}
			                             		</option>
			                           		</c:otherwise>
			                         	</c:choose>
			                      </c:forEach>
			                    </select>
			                  </div>
			                </div>
		                </div>
		                
	                
	                  </div>
	                  
	                  
	                  <!-- GRADIENT POINT -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'GRADIENTPOINT' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                    
	                    id="tab006gradientpoint"
	                  >
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>${dl_form_fill}</strong>

						  <div class="cell-holder">
	                        <div class="cell">
	                          <span>${style.gradientPointMinFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.gradientPointMinFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.gradientPointMinFillMd.name}" value="${style.gradientPointMinFill}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <span>${style.gradientPointMaxFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.gradientPointMaxFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.gradientPointMaxFillMd.name}" value="${style.gradientPointMaxFill}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div id="gradient-point-fill-opacity-cell" class="cell">
			                    <label for="gradient-point-fill-opacity-select">${style.gradientPointFillOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="gradient-point-fill-opacity-select" class="tab-select" name="style.${style.gradientPointFillOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.gradientPointFillOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>  
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span>${style.gradientPointStrokeMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.gradientPointStroke};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.gradientPointStrokeMd.name}" value="${style.gradientPointStroke}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="gradient-point-stroke-select">${style.gradientPointStrokeWidthMd.displayLabel}</label>
	                          <div class="select-holder">
	                            <select name="style.${style.gradientPointStrokeWidthMd.name}" id="gradient-point-stroke-select" class="tab-select">
	                              <c:forEach begin="0" end="15" var="size">
	                                <c:choose>
	                                  <c:when test="${style.gradientPointStrokeWidth == size}">
	                                    <option selected="selected" value="${size}">${size}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                    <option value="${size}">${size}</option>
	                                  </c:otherwise>
	                                </c:choose>
	                              </c:forEach>
	                            </select>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="gradient-point-stroke-opacity-select">${style.gradientPointStrokeOpacityMd.displayLabel}</label>
	                          <div class="text">
	                             <select id="gradient-point-stroke-opacity-select" class="tab-select" name="style.${style.gradientPointStrokeOpacityMd.name}">
	                              <c:forEach step="5" begin="0" end="100" var="size">
	                                <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
	                                
	                                <c:choose>
	                                  <c:when test="${style.gradientPointStrokeOpacity*100 == size}">
	                                    <option selected="selected" value="${potentialValue}">${size}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                    <option value="${potentialValue}">${size}</option>
	                                  </c:otherwise>
	                                </c:choose>
	                              </c:forEach>
	                            </select>   
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="fill-block">
	                     <strong class="title"><gdb:localize var="shape_heading" key="DashboardThematicLayer.form.shapeHeading"/>${shape_heading}</strong>
	                      <div class="cell-holder">
		                    	<div class="cell">
		                          <label for="gradient-point-radius-select">${style.gradientPointSizeMd.displayLabel}</label>
		                          <div class="text">
		                          	<input id="gradient-point-radius-select" name="style.${style.gradientPointSizeMd.name}" type="text" value="${style.gradientPointSize}"></input>
		                          </div>
		                        </div>
	                      </div>
	                      <div id="gradient-point-type-container" class="cell">
			                  <label for="gradient-point-type"><gdb:localize var="pt_type" key="DashboardLayer.form.pointType"/>${pt_type}</label>
			                  <div class="select-box">
			                    <select id="gradient-point-type" class="method-select" name="style.gradientPointWellKnownName">
			                      <c:forEach items="${pointTypes}" var="type">
			                         	<c:choose>
			                           		<c:when test="${type == activeGradientPointType}">
			                             		<option value="${type}" selected="selected">
			                               			${type}
			                             		</option>
			                           		</c:when>
			                           		<c:otherwise>
			                             		<option value="${type}">
			                               			${type}
			                             		</option>
			                           		</c:otherwise>
			                         	</c:choose>
			                      </c:forEach>
			                    </select>
			                  </div>
			                </div>
	                    </div>
	                  </div>
	                  
	                  
	                  <!-- CATEGORY POINT -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'CATEGORYPOINT' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                  	id="tab007categoriespoint" 
	                  >
	                  
	                    <div class="color-section">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="heading-list">
	                        <span><gdb:localize key="DashboardThematicLayer.form.category"/></span>
	                        <span><gdb:localize key="DashboardThematicLayer.form.color"/></span>
	                        <span><gdb:localize var="dl_form_cat_input_placeholder" key="DashboardThematicLayer.form.catInputPlaceholder"/></span>
	                      </div>
	                    <div class="category-block" id="category-point-colors-container">
	                      	  
	                    <input id="categories-point-input" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" data-categoriesstore='${pointcategories}' type="hidden" class="category-input" name="style.categoryPointStyles" ></input>
		                     <c:choose>
		                      	<c:when test="${'true' == isOntologyAttribute}">
		                      		<!-- RENDER ONTOLOGY TREE DATA -->
		                      		<div class="ontology-category-input-container">
										<div id="points-ontology-tree" data-termtype="${termType}" data-reltype="${relationshipType}" data-roots='${roots}' ></div>
										<div id="other-cat-point-container" class="other-cat-container">
											<ul class="color-list other-cat">							                       <li>
						                         <div class="category-container">
							                       	 <div class="text category-input-container">
							                       	    <gdb:localize var="other" key="Other"/>								                       	 
							                       	 	<p id="cat-point-other-basic-label" >${other}</p>
							                       	 </div>
							                       	 <a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">
	                  									<span id="cat-point-other-color-selector" class="ico ontology-category-color-icon" style="background:#737678; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>
	                								 </a>
					                   	 		 </div>
						                       </li>	                       
						                    </ul>
						                </div>
									  	<div class="check-block">
								      		<input id="ont-cat-point-other-option" class="other-option-check-box" type="checkbox" name="otherOption" checked></input>
								        	<label for="ont-cat-point-other-option"><gdb:localize var="dl_form_other_label" key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>${dl_form_other_label}</label>
								      	</div>
									</div>
		                      	</c:when>
		                      	<c:otherwise>
		                      		<!-- RENDER BASIC CATEGORIES -->
			                        <div class="panel-group choice-color category-group">
										<div class="panel">
					                    	<div id="choice-color02" class="panel-collapse">
	  					                    </div>
							            </div>
							            <div class="style-options-block">
<%-- 						              <strong class="title"><gdb:localize var="dl_form_options_heading" key="DashboardThematicLayer.form.categoryOptionsHeading"/>${dl_form_options_heading}</strong> --%>
										  <div class="check-block">
										    <input id="basic-cat-point-other-option" class="other-option-check-box" type="checkbox" name="" checked></input>
										    <label for="basic-cat-point-other-option"><gdb:localize var="dl_form_other_label" key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>${dl_form_other_label}</label>
										  </div>
						                </div>
			                        </div>
		                      	</c:otherwise>
		                      </c:choose>
	                      </div>
	                    </div>
	                    
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span>${style.categoryPointStrokeMd.displayLabel}</span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:${style.categoryPointStroke};">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="hidden" class="color-input" name="style.${style.categoryPointStrokeMd.name}" value="${style.categoryPointStroke}" />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="category-point-stroke-width-select">${style.categoryPointStrokeWidthMd.displayLabel}</label>
			                    <div class="select-holder">
			                      <select id="category-point-stroke-width-select" class="tab-select" name="style.${style.categoryPointStrokeWidthMd.name}">
			                        <c:forEach begin="0" end="15" var="size">
			                          <c:choose>
			                            <c:when test="${style.categoryPointStrokeWidth == size}">
			                              <option selected="selected" value="${size}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${size}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="category-point-stroke-opacity-select">${style.categoryPointStrokeOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="category-point-stroke-opacity-select" class="tab-select" name="style.${style.categoryPointStrokeOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.categoryPointStrokeOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
	                      </div>

	                    <div id="category-point-radius-block" class="fill-block">
	                      <strong class="title"><gdb:localize var="shape_heading" key="DashboardThematicLayer.form.shapeHeading"/>${shape_heading}</strong>
	                      <div class="cell-holder">
		                    	<div class="cell">
		                          <label for="category-point-radius-select">${style.categoryPointSizeMd.displayLabel}</label>
		                          <div class="text">
		                          	<input id="category-point-radius-select" name="style.${style.categoryPointSizeMd.name}" type="text" value="${style.categoryPointSize}"></input>
		                          </div>
		                        </div>
		                        <div class="cell">
			                    <label for="category-point-fill-opacity-select">${style.categoryPointFillOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="category-point-fill-opacity-select" class="tab-select" name="style.${style.categoryPointFillOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.categoryPointFillOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>  
			                    </div>
			                  </div>
	                      </div>
	                      
	                      	<div id="category-point-type-container" class="cell">
			                  <label for="category-point-type"><gdb:localize var="pt_type" key="DashboardLayer.form.pointType"/>${pt_type}</label>
			                  <div class="select-box">
			                    <select id="category-point-type" class="method-select" name="style.categoryPointWellKnownName">
			                      <c:forEach items="${pointTypes}" var="type">
			                         	<c:choose>
			                           		<c:when test="${type == activeCategoryPointType}">
			                             		<option value="${type}" selected="selected">
			                               			${type}
			                             		</option>
			                           		</c:when>
			                           		<c:otherwise>
			                             		<option value="${type}">
			                               			${type}
			                             		</option>
			                           		</c:otherwise>
			                         	</c:choose>
			                      </c:forEach>
			                    </select>
			                  </div>
			                </div>
	                      </div>
	                    </div>
	                </div>
	                
	                  
	                  <!-- BUBBLE -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'BUBBLE' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                    
	                    id="tab002bubble"
	                  >
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>${dl_form_fill}</strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span>${style.bubbleFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.bubbleFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.bubbleFillMd.name}" value="${style.bubbleFill}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="f71">${style.bubbleOpacityMd.displayLabel}</label>
	                          <div class="text">
	                            <select id="f71" class="tab-select" name="style.${style.bubbleOpacityMd.name}">
	                              <c:forEach step="5" begin="0" end="100" var="size">
	                                <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>                              
	                                <c:choose>
	                                  <c:when test="${style.bubbleOpacity*100 == size}">
	                                    <option selected="selected" value="${potentialValue}">${size}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                    <option value="${potentialValue}">${size}</option>
	                                  </c:otherwise>
	                                </c:choose>
	                              </c:forEach>
	                            </select>                          
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span>${style.bubbleStrokeMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.bubbleStroke};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.bubbleStrokeMd.name}" value="${style.bubbleStroke}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="f33">${style.bubbleStrokeWidthMd.displayLabel}</label>
	                          <div class="select-holder">
	                            <select name="style.${style.bubbleStrokeWidthMd.name}" id="f73" class="tab-select">
	                              <c:forEach begin="0" end="15" var="size">
	                                <c:choose>
	                                  <c:when test="${style.bubbleStrokeWidth == size}">
	                                    <option selected="selected" value="${size}">${size}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                    <option value="${size}">${size}</option>
	                                  </c:otherwise>
	                                </c:choose>
	                              </c:forEach>
	                            </select>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="f74">${style.bubbleStrokeOpacityMd.name}</label>
	                          <div class="text">
	                             <select id="f74" class="tab-select" name="style.${style.bubbleStrokeOpacityMd.name}">
	                              <c:forEach step="5" begin="0" end="100" var="size">
	                                <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
	                                
	                                <c:choose>
	                                  <c:when test="${style.bubbleStrokeOpacity*100 == size}">
	                                    <option selected="selected" value="${potentialValue}">${size}</option>
	                                  </c:when>
	                                  <c:otherwise>
	                                    <option value="${potentialValue}">${size}</option>
	                                  </c:otherwise>
	                                </c:choose>
	                              </c:forEach>
	                            </select>   
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize var="shape_heading" key="DashboardThematicLayer.form.shapeHeading"/>${shape_heading}</strong>
	                      <div class="cell-holder">
		                        <div class="cell">
		                          <label for="f76">${style.bubbleMinSizeMd.displayLabel}</label>
		                          <div class="text"><input id="f76" name="style.${style.bubbleMinSizeMd.name}" type="text" value="${style.bubbleMinSize}"/></div>
		                        </div>
		                        <div class="cell">
		                          <label for="f77">${style.bubbleMaxSizeMd.displayLabel}</label>
		                          <div class="text">
		                            <input id="f77" name="style.${style.bubbleMaxSizeMd.name}" type="text" value="${style.bubbleMaxSize}"></input>
		                          </div>
		                        </div>
		                        
		                        <div class="check-block">
				                    <input id="f52" type="checkbox" <c:if test="${style.bubbleContinuousSize}">checked</c:if> name="style.bubbleContinuousSize"></input>
				                    <label for="f52">${style.bubbleContinuousSizeMd.displayLabel}</label>
				                    <mjl:messages attribute="bubbleContinuousSize" classes="error-message">
				                      <mjl:message />
				                    </mjl:messages>
				                </div>
	                      </div>
	                    </div>
	                    <div class="secondary-box">
	                      <input type="hidden" id="secondaryCategories" data-categoriesstore="${secondaryCategories}" />
	                      <input type="hidden" id="secondaryAggregationValue" value="${secondaryAggregation}" />
	                      
	                      <strong class="title"><gdb:localize key="DashboardLayer.form.secondaryAttributeStyle"/></strong>
	                      <div class="cell-holder">	                    
	                        <label class="secondary-label" for="secondaryAttribute" ><gdb:localize key="DashboardLayer.form.secondaryAttribute"/></label>
  	                        <div id="secondary-select-box" class="select-box">
	                          <select id="secondaryAttribute" class="method-select" name="secondaryAttribute">
	                            <option ${style.secondaryAttributeId == '' ? 'selected="selected"' : ''} value=""><gdb:localize key="DashboardLayer.form.none"/></option>
	                            <c:forEach items="${secondaryAttributes}" var="secondaryAttribute">
		                          <option ${style.secondaryAttributeId == secondaryAttribute.mdAttributeId ? 'selected="selected"' : ''} value="${secondaryAttribute.mdAttributeId}" data-type="${secondaryAttribute.attributeType}">${secondaryAttribute.displayLabel}</option>
		                        </c:forEach>
		                      </select>
	                        </div>
	                      </div>
	                      <div id="secondary-content" ${style.secondaryAttributeId == '' ? 'style="display:none;"' : ''}>
     	                    <div class="cell-holder">
	                          <label class="secondary-label" for="secondaryAggregation"><gdb:localize key="DashboardLayer.form.secondaryAggregation"/></label>
	                          <div class="select-box" id="secondary-aggregation-container"> 
	                          </div> 
	                        </div>
	                        <div id="secondary-cateogries"></div>
	                      </div>
                        </div>
	                  </div>
	                  
	                  
	                  <!-- BASICPOLYGON -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'BASICPOLYGON' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                    
	                    id="tab003basicpolygon"
	                  >
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>${dl_form_fill}</strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span>${style.polygonFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.polygonFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.polygonFillMd.name}" value="${style.polygonFill}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
			                    <label for="basic-polygon-fill-opacity-select">${style.polygonFillOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="basic-polygon-fill-opacity-select" class="tab-select" name="style.${style.polygonFillOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.polygonFillOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>  
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                      
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span>${style.polygonStrokeMd.displayLabel}</span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:${style.polygonStroke};">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="hidden" class="color-input" name="style.${style.polygonStrokeMd.name}" value="${style.polygonStroke}" />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="basic-polygon-stroke-width-select">${style.polygonStrokeWidthMd.displayLabel}</label>
			                    <div class="select-holder">
			                      <select id="basic-polygon-stroke-width-select" class="tab-select" name="style.${style.polygonStrokeWidthMd.name}">
			                        <c:forEach begin="0" end="15" var="size">
			                          <c:choose>
			                            <c:when test="${style.polygonStrokeWidth == size}">
			                              <option selected="selected" value="${size}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${size}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="basic-polygon-stroke-opacity-select">${style.polygonStrokeOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="basic-polygon-stroke-opacity-select" class="tab-select" name="style.${style.polygonStrokeOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.polygonStrokeOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                  </div>
	                  
	                  
	                  <!-- GRADIENT POLYGON -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'GRADIENTPOLYGON' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                    
	                    id="tab004gradientpolygon"
	                  >
	                    <div class="gradient-block">
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>${dl_form_fill}</strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span>${style.gradientPolygonMinFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.gradientPolygonMinFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.gradientPolygonMinFillMd.name}" value="${style.gradientPolygonMinFill}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <span>${style.gradientPolygonMaxFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.gradientPolygonMaxFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.gradientPolygonMaxFillMd.name}" value="${style.gradientPolygonMaxFill}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
			                    <label for="gradient-polygon-fill-opacity-select">${style.gradientPolygonFillOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="gradient-polygon-fill-opacity-select" class="tab-select" name="style.${style.gradientPolygonFillOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.gradientPolygonFillOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>  
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span>${style.gradientPolygonStrokeMd.displayLabel}</span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:${style.gradientPolygonStroke};">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="hidden" class="color-input" name="style.${style.gradientPolygonStrokeMd.name}" value="${style.gradientPolygonStroke}" />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="gradient-polygon-stroke-width-select">${style.gradientPolygonStrokeWidthMd.displayLabel}</label>
			                    <div class="select-holder">
			                      <select id="gradient-polygon-stroke-width-select" class="tab-select" name="style.${style.gradientPolygonStrokeWidthMd.name}">
			                        <c:forEach begin="0" end="15" var="size">
			                          <c:choose>
			                            <c:when test="${style.gradientPolygonStrokeWidth == size}">
			                              <option selected="selected" value="${size}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${size}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="gradient-polygon-stroke-opacity-select">${style.gradientPolygonStrokeOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="gradient-polygon-stroke-opacity-select" class="tab-select" name="style.${style.gradientPolygonStrokeOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.gradientPolygonStrokeOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                  </div>
	                  
	                 
	                  <!-- CATEGORY POLYGON -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'CATEGORYPOLYGON' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                  	id="tab005categoriespolygon" 
	                  >
	                  
	                    <div class="color-section">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="heading-list">
	                        <span><gdb:localize key="DashboardThematicLayer.form.category"/></span>
	                        <span><gdb:localize key="DashboardThematicLayer.form.color"/></span>
	                        <span><gdb:localize var="dl_form_cat_input_placeholder" key="DashboardThematicLayer.form.catInputPlaceholder"/></span>
	                      </div>
	                    <div class="category-block" id="category-colors-container">
	                      	  
	                    <input id="categories-polygon-input" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" data-categoriesstore='${polygoncategories}' type="hidden" class="category-input" name="style.styleCategories" ></input>
		                     <c:choose>
		                      	<c:when test="${'true' == isOntologyAttribute}">
		                      		<!-- RENDER ONTOLOGY TREE DATA -->
		                      		<div class="ontology-category-input-container">
										<div id="polygon-ontology-tree" data-termtype="${termType}" data-reltype="${relationshipType}" data-roots='${roots}' ></div>
										<div id="other-cat-poly-container" class="other-cat-container">
											<ul class="color-list other-cat">							                       <li>
						                         <div class="category-container">
							                       	 <div class="text category-input-container">
							                       	    <gdb:localize var="other" key="Other"/>								                       	 
							                       	 	<p id="cat-other-basic-label" >${other}</p>
							                       	 </div>
							                       	 <a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">
	                  									<span id="cat-other-color-selector" class="ico ontology-category-color-icon" style="background:#737678; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>
	                								 </a>
					                   	 		 </div>
						                       </li>	                       
						                    </ul>
						                </div>
									  	<div class="check-block">
								      		<input id="ont-cat-poly-other-option" class="other-option-check-box" type="checkbox" name="otherOption" checked></input>
								        	<label for="ont-cat-poly-other-option"><gdb:localize var="dl_form_other_label" key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>${dl_form_other_label}</label>
								      	</div>
									</div>
		                      	</c:when>
		                      	<c:otherwise>
		                      		<!-- RENDER BASIC CATEGORIES -->
			                        <div class="panel-group choice-color category-group">
										<div class="panel">
					                    	<div id="choice-color01" class="panel-collapse"></div>
							            </div>
							            <div class="style-options-block">
<%-- 						              <strong class="title"><gdb:localize var="dl_form_options_heading" key="DashboardThematicLayer.form.categoryOptionsHeading"/>${dl_form_options_heading}</strong> --%>
										  <div class="check-block">
										    <input id="basic-cat-poly-other-option" class="other-option-check-box" type="checkbox" name="" checked ></input>
										    <label for="basic-cat-poly-other-option"><gdb:localize var="dl_form_other_label" key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>${dl_form_other_label}</label>
										  </div>
						                </div>
			                        </div>
		                      	</c:otherwise>
		                      </c:choose>
	                      </div>
	                    </div>
	                    
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span>${style.categoryPolygonStrokeMd.displayLabel}</span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:${style.categoryPolygonStroke};">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="hidden" class="color-input" name="style.${style.categoryPolygonStrokeMd.name}" value="${style.categoryPolygonStroke}" />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="category-polygon-stroke-width-select">${style.categoryPolygonStrokeWidthMd.displayLabel}</label>
			                    <div class="select-holder">
			                      <select id="category-polygon-stroke-width-select" class="tab-select" name="style.${style.categoryPolygonStrokeWidthMd.name}">
			                        <c:forEach begin="0" end="15" var="size">
			                          <c:choose>
			                            <c:when test="${style.categoryPolygonStrokeWidth == size}">
			                              <option selected="selected" value="${size}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${size}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="category-polygon-stroke-opacity-select">${style.categoryPolygonStrokeOpacityMd.displayLabel}</label>
			                    <div class="text">
			                      <select id="category-polygon-stroke-opacity-select" class="tab-select" name="style.${style.categoryPolygonStrokeOpacityMd.name}">
			                        <c:forEach step="5" begin="0" end="100" var="size">
			                          <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>
			                          <c:choose>
			                            <c:when test="${style.categoryPolygonStrokeOpacity*100 == size}">
			                              <option selected="selected" value="${potentialValue}">${size}</option>
			                            </c:when>
			                            <c:otherwise>
			                              <option value="${potentialValue}">${size}</option>
			                            </c:otherwise>
			                          </c:choose>
			                        </c:forEach>
			                      </select>
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                </div>
	              </div>
	            </div>
			</mjl:component>
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
    

