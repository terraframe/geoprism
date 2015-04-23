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
<mjl:component param="style" item="${style}"></mjl:component>


    <div id="DashboardLayer-mainDiv" class="modal-dialog">
      <div class="modal-content">
        	<div class="heading">
          		<h1><gdb:localize var="dl_form_heading" key="DashboardReferenceLayer.form.heading"/>${dl_form_heading}</h1>
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
                <strong><gdb:localize var="dl_form_nameTheLayer" key="DashboardReferenceLayer.form.nameTheLayer"/>${dl_form_nameTheLayer}</strong>
              </div>
              <div class="holder">
				<mjl:component param="layer" item="${layer}">
                	<label class="none" for="f312">${layer.nameMd.displayLabel}</label>
                	<span class="text">
                  	<input type="text" id="layer.name" value="${layerName}" name="layer.name" readonly/>
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
	                <strong><gdb:localize var="dl_form_labelsAndValues" key="DashboardReferenceLayer.form.labelsAndValues"/>${dl_form_labelsAndValues}</strong>
	              </div>
	              <div class="holder">
	                <div class="row-holder">
<!-- 	                  <div class="check-block"> -->
<%-- 	                    <input id="f51" type="checkbox" <c:if test="${style.enableValue}">checked</c:if> name="style.enableValue"> --%>
<%-- 	                    <label for="f51">${style.enableValueMd.displayLabel}</label> --%>
<%-- 	                    <mjl:messages attribute="enableValue" classes="error-message"> --%>
<%-- 	                      <mjl:message /> --%>
<%-- 	                    </mjl:messages> --%>
<!-- 	                  </div> -->
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
	                              <option value="${font}" selected="selected">${font}</option>
	                            </c:when>
	                            <c:otherwise>
	                              <option value="${font}">${font}</option>
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
            
            <!-- 
               Begin Layer Types
             -->
			<mjl:component param="layer" item="${layer}">
			  <input type="hidden" name="layer.${layer.universalMd.name}" value="${universalId}" />
	            <div class="row-holder">
	              <div class="label-holder style04">
	                <strong><gdb:localize var="dl_form_chooseLayerType" key="DashboardReferenceLayer.form.chooseLayerType"/>${dl_form_chooseLayerType}</strong>
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
	                <strong><gdb:localize var="dl_form_styleTheLayer" key="DashboardReferenceLayer.form.styleTheLayer"/>${dl_form_styleTheLayer}</strong>
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
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardReferenceLayer.form.fill"/>${dl_form_fill}</strong>
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
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardReferenceLayer.form.stroke"/>${dl_form_stroke}</strong>
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
	                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardReferenceLayer.form.fill"/>${dl_form_fill}</strong>
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
	                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardReferenceLayer.form.stroke"/>${dl_form_stroke}</strong>
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
<!-- 	                    <div class="fill-block"> -->
<%-- 	                      <strong class="title"><gdb:localize var="dl_form_radius" key="DashboardReferenceLayer.form.radius"/>${dl_form_radius}</strong> --%>
<!-- 	                      <div class="cell-holder"> -->
<!-- 		                        <div class="cell"> -->
<%-- 		                          <label for="f76">${style.pointMinSizeMd.displayLabel}</label> --%>
<%-- 		                          <div class="text"><input id="f76" name="style.${style.pointMinSizeMd.name}" type="text" value="${style.pointMinSize}"></div> --%>
<!-- 		                        </div> -->
<!-- 		                        <div class="cell"> -->
<%-- 		                          <label for="f77">${style.pointMaxSizeMd.displayLabel}</label> --%>
<!-- 		                          <div class="text"> -->
<%-- 		                            <input id="f77" name="style.${style.pointMaxSizeMd.name}" type="text" value="${style.pointMaxSize}"> --%>
<!-- 		                          </div> -->
<!-- 		                        </div> -->
		                        
<!-- 		                        <div class="check-block"> -->
<%-- 				                    <input id="f52" type="checkbox" <c:if test="${style.bubbleContinuousSize}">checked</c:if> name="style.bubbleContinuousSize"> --%>
<%-- 				                    <label for="f52">${style.bubbleContinuousSizeMd.displayLabel}</label> --%>
<%-- 				                    <mjl:messages attribute="bubbleContinuousSize" classes="error-message"> --%>
<%-- 				                      <mjl:message /> --%>
<%-- 				                    </mjl:messages> --%>
<!-- 				                </div> -->
<!-- 	                      </div> -->
<!-- 	                    </div> -->
	                  </div>
	                  
	                  
	                  
	                  
	                  
	                  <!-- GRADIENT -->
	                  
	                  <gdb:localize var="dl_form_fill" key="DashboardReferenceLayer.form.fill"/>

	                 
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
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.create.button" 
							      value="Map It" action="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.applyWithStyle.mojo"
							      classes="btn btn-primary"  />
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayer.form.cancel.button"
							      value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardReferenceLayerController.cancel.mojo"
							      classes="btn btn-default" />
                </div>
              </div>
            </div>
          </fieldset>
      </div>
    </div>
    

