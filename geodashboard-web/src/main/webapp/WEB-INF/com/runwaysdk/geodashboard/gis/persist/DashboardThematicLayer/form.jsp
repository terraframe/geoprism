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
	                    <input id="f51" type="checkbox" <c:if test="${style.enableValue}">checked</c:if> name="style.enableValue">
	                    <label for="f51">${style.enableValueMd.displayLabel}</label>
	                    <mjl:messages attribute="enableValue" classes="error-message">
	                      <mjl:message />
	                    </mjl:messages>
	                  </div>
	                  <div class="check-block">
	                    <input id="f94" type="checkbox" <c:if test="${style.enableLabel}">checked</c:if> name="style.${style.enableLabelMd.name}">
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
			
			           <div class="row-holder">
              <div class="label-holder style03">
                <strong><gdb:localize var="dl_form_defineGeoNode" key="DashboardThematicLayer.form.defineGeoNode"/>${dl_form_defineGeoNode}</strong>
              </div>
              <div class="holder add">
              	<mjl:component param="layer" item="${layer}">
                <div class="box">
                  <label for="f60"><gdb:localize var="dl_form_geoNode" key="DashboardThematicLayer.form.geoNode"/>${dl_form_geoNode}</label>
                  <div class="select-box">
<%-- 	                    <select id="f60" class="method-slect" name="layer.${layer.universalMd.name}"> --%>
<%-- 	                       <c:forEach items="${universals}" var="universal"> --%>
<%-- 		                         <c:choose> --%>
<%-- 		                           <c:when test="${layer.universalId == universal.id}"> --%>
<%-- 				                         <option value="${universal.id}" selected="selected">${universal.displayLabel.value}</option> --%>
<%-- 		                           </c:when> --%>
<%-- 		                           <c:otherwise> --%>
<%-- 		                           		<option value="${universal.id}">${universal.displayLabel.value}</option> --%>
<%-- 		                           </c:otherwise> --%>
<%-- 		                         </c:choose> --%>
<%-- 	                      </c:forEach> --%>
<!-- 	                    </select> -->
                  </div>
                </div>
				</mjl:component>
              </div>
            </div>
			
			
            <div class="row-holder">
              <div class="label-holder style03">
                <strong><gdb:localize var="dl_form_defineAggMeth" key="DashboardThematicLayer.form.defineAggMeth"/>${dl_form_defineAggMeth}</strong>
              </div>
              <div class="holder add">
              	<mjl:component param="layer" item="${layer}">
                <div class="box">
                  <label for="f58"><gdb:localize var="dl_form_groupBy" key="DashboardThematicLayer.form.groupBy"/>${dl_form_groupBy}</label>
                  <div class="select-box">
<%-- 	                    <select id="f58" class="method-slect" name="layer.${layer.universalMd.name}"> --%>
<%-- 	                       <c:forEach items="${universals}" var="universal"> --%>
<%-- 		                         <c:choose> --%>
<%-- 		                           <c:when test="${layer.universalId == universal.id}"> --%>
<%-- 				                         <option value="${universal.id}" selected="selected">${universal.displayLabel.value}</option> --%>
<%-- 		                           </c:when> --%>
<%-- 		                           <c:otherwise> --%>
<%-- 		                           		<option value="${universal.id}">${universal.displayLabel.value}</option> --%>
<%-- 		                           </c:otherwise> --%>
<%-- 		                         </c:choose> --%>
<%-- 	                      </c:forEach> --%>
<!-- 	                    </select> -->
                  </div>
                </div>
	                <div class="box">
	                  <label for="f59"><gdb:localize var="dl_form_accordingTo" key="DashboardThematicLayer.form.accordingTo"/>${dl_form_accordingTo}</label>
	                  <div class="select-box">
	                    <select id="f59" class="method-slect" name="layer.${layer.aggregationTypeMd.name}">
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
	            <div class="row-holder">
	              <div class="label-holder style04">
	                <strong><gdb:localize var="dl_form_chooseLayerType" key="DashboardThematicLayer.form.chooseLayerType"/>${dl_form_chooseLayerType}</strong>
	              </div>
	              <div class="holder style04">
	                <ul class="nav-tabs type-tabs">
		                <c:forEach items="${layerTypeNames}" var="layerTypeName" varStatus="loop">
		                	<c:choose>
			          			<c:when test="${'GRADIENT' != layerTypeName || 'false' == isOntologyAttribute && 'false' == isTextAttribute}">
				                  <li
				                    class="${layerTypeName}  
				                    <c:if test="${layerTypeName == activeLayerTypeName}">
				                        active
				                    </c:if>
				                    ">
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
				                </c:when>
				             </c:choose>
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
	                  <%-- 
	                    These are reusable cell components that are used across multiple tabs. These are inserted on tab change by custom javascript in DynamicMap.js
	                  --%>
	                  <div id="gdb-reusable-cell-polygonStroke" style="display: none;" class="cell">
	                    <span>${style.polygonStrokeMd.displayLabel}</span>
	                    <div class="color-holder">
	                      <a href="#" class="color-choice">
	                       <span class="ico" style="background:${style.polygonStroke};">icon</span>
	                       <span class="arrow">arrow</span>
	                       <input type="hidden" class="color-input" name="style.${style.polygonStrokeMd.name}" value="${style.polygonStroke}" />
		                    </a>
		                   </div>
		                </div>
		                <div id="gdb-reusable-cell-polygonStrokeWidth" style="display: none;" class="cell">
	                    <label for="f80">${style.polygonStrokeWidthMd.displayLabel}</label>
	                    <div class="select-holder">
	                      <select id="f80" class="tab-select" name="style.${style.polygonStrokeWidthMd.name}">
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
	                  <div id="gdb-reusable-cell-polygonStrokeOpacity" style="display: none;" class="cell">
	                    <label for="f81">${style.polygonStrokeOpacityMd.displayLabel}</label>
	                    <div class="text">
	                      <select id="f81" class="tab-select" name="style.${style.polygonStrokeOpacityMd.name}">
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
	                  <div id="gdb-reusable-cell-polygonFillOpacity" style="display: none;" class="cell">
	                    <label for="f78">${style.polygonFillOpacityMd.displayLabel}</label>
	                    <div class="text">
	                      <select id="f78" class="tab-select" name="style.${style.polygonFillOpacityMd.name}">
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
	                  <%--
	                    End Reusable Cell Components
	                  --%>
	                  
	                  
					  <!-- BASIC -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'BASIC' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                    
	                    id="tab001basic"
	                  >
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>${dl_form_fill}</strong>
	                      <div id="gdb-reusable-basic-fill-cell-holder" class="cell-holder">
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
	                      </div>
	                      <%-- Dynamically inserted: PolygonFillOpacity --%>
	                    </div>
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div id="gdb-reusable-basic-stroke-cell-holder" class="cell-holder">
	                        <%-- Dynamically inserted with javascript--%>
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
	                          <label for="f71">${style.pointOpacityMd.displayLabel}</label>
	                          <div class="text">
	                            <select id="f71" class="tab-select" name="style.${style.pointOpacityMd.name}">
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
	                          <label for="f33">${style.pointStrokeWidthMd.displayLabel}</label>
	                          <div class="select-holder">
	                            <select name="style.${style.pointStrokeWidthMd.name}" id="f73" class="tab-select">
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
	                          <label for="f74">${style.pointStrokeOpacityMd.name}</label>
	                          <div class="text">
	                             <select id="f74" class="tab-select" name="style.${style.pointStrokeOpacityMd.name}">
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
	                      <strong class="title"><gdb:localize var="dl_form_radius" key="DashboardThematicLayer.form.radius"/>${dl_form_radius}</strong>
	                      <div class="cell-holder">
		                    <c:choose>
			                    <c:when test="${'true' == isOntologyAttribute || 'true' == isTextAttribute}">
			                    	<div class="cell">
			                          <label for="f79">${style.pointFixedSizeMd.displayLabel}</label>
			                          <div class="text">
			                          	<input id="f79" name="style.${style.pointFixedSizeMd.name}" type="text" value="${style.pointFixedSize}">
			                          </div>
			                          <input type="hidden" class="" name="style.${style.pointFixedMd.name}" value="${style.pointFixed}" />
			                        </div>
		                        </c:when>
		                        <c:otherwise>
		                        
					                
			                        <div class="cell">
			                          <label for="f76">${style.pointMinSizeMd.displayLabel}</label>
			                          <div class="text"><input id="f76" name="style.${style.pointMinSizeMd.name}" type="text" value="${style.pointMinSize}"></div>
			                        </div>
			                        <div class="cell">
			                          <label for="f77">${style.pointMaxSizeMd.displayLabel}</label>
			                          <div class="text">
			                            <input id="f77" name="style.${style.pointMaxSizeMd.name}" type="text" value="${style.pointMaxSize}">
			                          </div>
			                        </div>
			                        
			                        <div class="check-block">
					                    <input id="f52" type="checkbox" <c:if test="${style.bubbleContinuousSize}">checked</c:if> name="style.bubbleContinuousSize">
					                    <label for="f52">${style.bubbleContinuousSizeMd.displayLabel}</label>
					                    <mjl:messages attribute="bubbleContinuousSize" classes="error-message">
					                      <mjl:message />
					                    </mjl:messages>
					                </div>
		                        </c:otherwise>
	                        </c:choose>
	                      </div>
	                    </div>
	                    <div class="secondary-box">
	                      <input type="hidden" id="secondaryCategories" data-categoriesstore="${secondaryCategories}" />
	                      <input type="hidden" id="secondaryAggregationValue" value="${secondaryAggregation}" />
	                      
	                      <strong class="title"><gdb:localize key="DashboardLayer.form.secondaryAttributeStyle"/></strong>
	                      <div class="cell-holder">	                    
	                        <label class="secondary-label" for="secondaryAttribute" ><gdb:localize key="DashboardLayer.form.secondaryAttribute"/></label>
  	                        <div id="secondary-select-box" class="select-box">
	                          <select id="secondaryAttribute" class="method-slect" name="secondaryAttribute">
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
	                  
	                  
	                  
	                  
	                  
	                  <!-- GRADIENT -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'GRADIENT' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                    
	                    id="tab003gradient"
	                  >
	                    <div class="gradient-block">
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>${dl_form_fill}</strong>
	                      <div id="gdb-reusable-gradient-fill-cell-holder" class="cell-holder">
	                        <div class="cell">
	                          <span>${style.polygonMinFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.polygonMinFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.polygonMinFillMd.name}" value="${style.polygonMinFill}" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <span>${style.polygonMaxFillMd.displayLabel}</span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:${style.polygonMaxFill};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="hidden" class="color-input" name="style.${style.polygonMaxFillMd.name}" value="${style.polygonMaxFill}" />
	                            </a>
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div id="gdb-reusable-gradient-stroke-cell-holder" class="cell-holder">
	                         <%-- Dynamically inserted with javascript--%>
	                      </div>
	                    </div>
	                  </div>
	                  
	                  <gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>

	                 
	                  <!-- CATEGORIES -->
	                  <div
	                    <c:choose>
	                      <c:when test="${'CATEGORY' == activeLayerTypeName}">
	                        class="tab-pane active"
	                      </c:when>
	                      <c:otherwise>
	                        class="tab-pane" style="display: none;"
	                      </c:otherwise>
	                    </c:choose>
	                  	id="tab004categories" 
	                  >
	                  
	                    <div class="color-section">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="heading-list">
	                        <span><gdb:localize key="DashboardThematicLayer.form.category"/></span>
	                        <span><gdb:localize key="DashboardThematicLayer.form.color"/></span>
	                        <span><gdb:localize var="dl_form_cat_input_placeholder" key="DashboardThematicLayer.form.catInputPlaceholder"/></span>
	                      </div>
	                    <div class="category-block" id="category-colors-container">
	                      	  
	                    <input id="categories-input" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" data-categoriesstore='${categories}' type="hidden" class="category-input" name="style.styleCategories" >
		                      
		                     <c:choose>
		                      	<c:when test="${'true' == isOntologyAttribute}">
		                      		<!-- RENDER ONTOLOGY TREE DATA -->
		                      		<div class="ontology-category-input-container">
										<div id="ontology-tree" data-termtype="${termType}" data-reltype="${relationshipType}" data-roots='${roots}' ></div>
									</div>
		                      	</c:when>
		                      	<c:otherwise>
		                      		<!-- RENDER BASIC CATEGORIES -->
			                        <div class="panel-group choice-color category-group">
										<div class="panel">
					                    	<div id="choice-color01" class="panel-collapse">
							                    <ul class="color-list">
							                       <li>
							                         <div class="category-container">
								                       	 <div class="text category-input-container">		
								                       	 	<gdb:localize var="dl_form_fill" key="DashboardThematicLayer.form.fill"/>
								                       	 	<input id="cat1" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="" type="text" value="" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
								                       	 </div>
						                   	 		 	 <div class="cell">
										                  	<div class="color-holder">
								                            	<a href="#" class="color-choice">
								                              	<span id="cat1-color-selector" class="ico cat-color-selector" style="background:#000000">icon</span>
								                              	<span class="arrow">arrow</span>
								                            	</a>
								                          	</div>
								                         </div>
						                   	 		 </div>
							                       </li>
							                       <li>
							                         <div class="category-container">
								                       	 <div class="text category-input-container">
								                       	 	<input id="cat2" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="" type="text" value="" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
								                       	 </div>
						                   	 		 	 <div class="cell">
										                  	<div class="color-holder">
								                            	<a href="#" class="color-choice">
								                              	<span id="cat2-color-selector" class="ico cat-color-selector" style="background:#000000">icon</span>
								                              	<span class="arrow">arrow</span>
								                            	</a>
								                          	</div>
								                         </div>
						                   	 		 </div>
							                       </li>
							                       <li>
							                         <div class="category-container">
								                       	 <div class="text category-input-container">
								                       	 	<input id="cat3" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="" type="text" value="" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
								                       	 </div>
						                   	 		 	 <div class="cell">
										                  	<div class="color-holder">
								                            	<a href="#" class="color-choice">
								                              	<span id="cat3-color-selector" class="ico cat-color-selector" style="background:#000000">icon</span>
								                              	<span class="arrow">arrow</span>
								                            	</a>
								                          	</div>
								                         </div>
						                   	 		 </div>
							                       </li>
							                       <li>
							                         <div class="category-container">
								                       	 <div class="text category-input-container">
								                       	 	<input id="cat4" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="" type="text" value="" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
								                       	 </div>
						                   	 		 	 <div class="cell">
										                  	<div class="color-holder">
								                            	<a href="#" class="color-choice">
								                              	<span id="cat4-color-selector" class="ico cat-color-selector" style="background:#000000">icon</span>
								                              	<span class="arrow">arrow</span>
								                            	</a>
								                          	</div>
								                         </div>
						                   	 		 </div>
							                       </li>		                       		                       
							                       <li>
							                         <div class="category-container">
								                       	 <div class="text category-input-container">
								                       	 	<input id="cat5" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="" type="text" value="" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
								                       	 </div>
						                   	 		 	 <div class="cell">
										                  	<div class="color-holder">
								                            	<a href="#" class="color-choice">
								                              	<span id="cat5-color-selector" class="ico cat-color-selector" style="background:#000000">icon</span>
								                              	<span class="arrow">arrow</span>
								                            	</a>
								                          	</div>
								                         </div>
						                   	 		 </div>
							                       </li>	
							                       <li>
							                         <div class="category-container">
								                       	 <div class="text category-input-container">
								                       	    <gdb:localize var="other" key="Other"/>								                       	 
															<input id="cat-other" data-mdattributeid="${mdAttributeId}" data-type="text" class="category-input" name="" type="text" value="${other}" placeholder="${other}" autocomplete="off" disabled> 
								                       	 </div>
						                   	 		 	 <div class="cell">
										                  	<div class="color-holder">
								                            	<a href="#" class="color-choice">
								                              	<span id="cat-other-color-selector" class="ico cat-color-selector" style="background:#737678">icon</span>
								                              	<span class="arrow">arrow</span>
								                            	</a>
								                          	</div>
								                         </div>
						                   	 		 </div>
							                       </li>	                       
							                     </ul>
							                   </div>
							                 </div>
							                 <div class="style-options-block">
<%-- 						                      <strong class="title"><gdb:localize var="dl_form_options_heading" key="DashboardThematicLayer.form.categoryOptionsHeading"/>${dl_form_options_heading}</strong> --%>
											  	<div class="check-block">
										      		<input id="f53" type="checkbox" name="" checked>
										        	<label for="f53"><gdb:localize var="dl_form_other_label" key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>${dl_form_other_label}</label>
										      	</div>
						                     </div>
			                        	</div>
		                      		</c:otherwise>
		                      </c:choose>
	
	                      </div>
	                    </div>
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardThematicLayer.form.stroke"/>${dl_form_stroke}</strong>
	                      <div id="gdb-reusable-categories-stroke-cell-holder" class="cell-holder">
							 <%-- Dynamically inserted with javascript--%>
	                      </div>
	                    </div>
	                    
<!-- 	                    <div class="style-options-block"> -->
<%-- 	                      <strong class="title"><gdb:localize var="dl_form_options_heading" key="DashboardThematicLayer.form.categoryOptionsHeading"/>${dl_form_options_heading}</strong> --%>
<!-- 						  <div class="check-block"> -->
<!-- 					      	<input id="f53" type="checkbox" name="" checked> -->
<%-- 					        <label for="f53"><gdb:localize var="dl_form_other_label" key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>${dl_form_other_label}</label> --%>
<!-- 					      </div> -->
<!-- 	                    </div> -->
	                  
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
                    	<input id="f65" type="checkbox" <c:if test="${layer.displayInLegend}">checked</c:if> name="layer.${layer.displayInLegendMd.name}">
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
    

