<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>

<%@page import="com.runwaysdk.constants.DeployProperties" %>


<script type="text/javascript" src="${pageContext.request.contextPath}/jstree/jstree.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/jstree/style.css" ></link>

<!-- Runway Factory -->
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/runway.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/widget/Widget.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/form/Form.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/list/List.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/contextmenu/ContextMenu.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/button/Button.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/overlay/Overlay.js"></script>

<!-- JQuery -->
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/jquery/Factory.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/jquery/Dialog.js"></script>


<!-- Runway Generic -->
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/RunwayControllerForm.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/RunwayControllerFormDialog.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/ui/RunwayControllerFormDialogDownloader.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/ontology/TermTree.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/ontology/GeoEntityTree.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/ontology/OntologyTree.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/Form.js"></script>

<%-- <%@page import="com.runwaysdk.system.gis.geo.GeoEntityDTO" %> --%>
<%@page import="com.runwaysdk.system.gis.geo.LocatedInDTO" %>
<%@page import="com.runwaysdk.geodashboard.ontology.ClassifierIsARelationshipDTO" %>
<%@page import="com.runwaysdk.geodashboard.ontology.ClassifierDTO" %>

<script type="text/javascript"> ${js} </script>


<!-- Localization -->	
<script type="text/javascript" src="${pageContext.request.contextPath}/cldrjs-0.4.0/dist/cldr.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/cldrjs-0.4.0/dist/cldr/event.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/cldrjs-0.4.0/dist/cldr/supplemental.js"></script>
	
<script type="text/javascript" src="${pageContext.request.contextPath}/globalize-1.0.0-alpha.17/dist/globalize.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/globalize-1.0.0-alpha.17/dist/globalize/number.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/globalize-1.0.0-alpha.17/dist/globalize/currency.js"></script>
	
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery/ui/js/jquery-ui-i18n.min.js"></script>	
<script type="text/javascript" src="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/Localized.js.jsp"></script>

<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/com/runwaysdk/ui/factory/runway/default.css" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/com/runwaysdk/geodashboard/ontology/TermTree.css" />
</head>


<!-- Include the types of this form to get the default values the MdAction needs -->
<mjl:component param="style" item="${style}">
</mjl:component>

    <div id="DashboardLayer-mainDiv" class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1><gdb:localize var="dl_form_heading" key="DashboardLayer.form.heading"/>${dl_form_heading}${activeMdAttributeLabel}</h1>
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
                <strong><gdb:localize var="dl_form_nameTheLayer" key="DashboardLayer.form.nameTheLayer"/>${dl_form_nameTheLayer}</strong>
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
                <strong><gdb:localize var="dl_form_labelsAndValues" key="DashboardLayer.form.labelsAndValues"/>${dl_form_labelsAndValues}</strong>
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
            <div class="row-holder">
              <div class="label-holder style03">
                <strong><gdb:localize var="dl_form_defineAggMeth" key="DashboardLayer.form.defineAggMeth"/>${dl_form_defineAggMeth}</strong>
              </div>
              <div class="holder add">
                <div class="box">
                  <label for="f58"><gdb:localize var="dl_form_groupBy" key="DashboardLayer.form.groupBy"/>${dl_form_groupBy}</label>
                  <div class="select-box">
<mjl:component param="layer" item="${layer}">
                    <select id="f58" class="method-slect" name="layer.${layer.universalMd.name}">
                       <c:forEach items="${universals}" var="universal">
                         <c:choose>
                           <c:when test="${layer.universalId == universal.id}">
		                         <option value="${universal.id}" selected="selected">
		                           ${universal.displayLabel.value}
		                         </option>
                           </c:when>
                           <c:otherwise>
		                         <c:choose>
			                         <c:when test="${universal.id == universalLeafId}">
			                         	<option value="${universal.id}" class="method-slect universal-leaf">
			                         		${universal.displayLabel.value}
			                         	</option>
			                         </c:when>
			                         <c:otherwise>
			                         	<option value="${universal.id}">
		                           			${universal.displayLabel.value}
		                         		</option>
			                         </c:otherwise>
			                     </c:choose>
                           </c:otherwise>
                         </c:choose>
                      </c:forEach>
                    </select>
</mjl:component>
                  </div>
                </div>
<mjl:component param="style" item="${style}">
                <div class="box">
                  <label for="f59"><gdb:localize var="dl_form_accordingTo" key="DashboardLayer.form.accordingTo"/>${dl_form_accordingTo}</label>
                  <div class="select-box">
                    <select id="f59" class="method-slect" name="style.${style.aggregationTypeMd.name}">
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
                <strong><gdb:localize var="dl_form_chooseLayerType" key="DashboardLayer.form.chooseLayerType"/>${dl_form_chooseLayerType}</strong>
              </div>
              <div class="holder style04">
                <ul class="nav-tabs type-tabs">
	                <c:forEach items="${layerTypeNames}" var="layerTypeName" varStatus="loop">
	                  <li
	                    class="${layerTypeName}  
	                    <c:if test="${layerTypeName == activeLayerTypeName}">
	                        active
	                    </c:if>
	                    "
	                  >
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
                <strong><gdb:localize var="dl_form_styleTheLayer" key="DashboardLayer.form.styleTheLayer"/>${dl_form_styleTheLayer}</strong>
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
                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardLayer.form.fill"/>${dl_form_fill}</strong>
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
                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardLayer.form.stroke"/>${dl_form_stroke}</strong>
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
                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardLayer.form.fill"/>${dl_form_fill}</strong>
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
                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardLayer.form.stroke"/>${dl_form_stroke}</strong>
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
                      <strong class="title"><gdb:localize var="dl_form_radius" key="DashboardLayer.form.radius"/>${dl_form_radius}</strong>
                      <div class="cell-holder">
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
                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardLayer.form.fill"/>${dl_form_fill}</strong>
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
                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardLayer.form.stroke"/>${dl_form_stroke}</strong>
                      <div id="gdb-reusable-gradient-stroke-cell-holder" class="cell-holder">
                         <%-- Dynamically inserted with javascript--%>
                      </div>
                    </div>
                  </div>
                  
                  <gdb:localize var="dl_form_fill" key="DashboardLayer.form.fill"/>
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
                      <strong class="title"><gdb:localize var="dl_form_fill" key="DashboardLayer.form.fill"/>${dl_form_fill}</strong>
                      <div class="heading-list">
                        <span><gdb:localize var="dl_form_category" key="DashboardLayer.form.category"/>${dl_form_category}</span>
                        <span><gdb:localize var="dl_form_color" key="DashboardLayer.form.color"/>${dl_form_color}</span>
                        <span><gdb:localize var="dl_form_cat_input_placeholder" key="DashboardLayer.form.catInputPlaceholder"/></span>
                      </div>
                      <div class="category-block" id="category-colors-container">
                        <div class="panel-group choice-color category-group">
                        
<!--                           <div class="panel"> -->
<!--                             <a class="opener" data-toggle="collapse" href="#choice-color01">First Node <span class="icon-color">color</span></a> -->
<!--                             <div id="choice-color01" class="panel-collapse"> -->
<!--                               <ul class="color-list"> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                               </ul> -->
<!--                             </div> -->
<!--                           </div> -->
                          
					<div class="panel">
                    	<div id="choice-color01" class="panel-collapse">
		                    <ul class="color-list">
		                       <li>
		                         <div class="category-container">
			                       	 <div class="text category-input-container">		
			                       	 	<gdb:localize var="dl_form_fill" key="DashboardLayer.form.fill"/>
			                       	 	<input id="cat1" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="style.styleCategory1" type="text" value="${style.styleCategory1}" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
			                       	 </div>
	                   	 		 	 <div class="cell">
					                  	<div class="color-holder">
			                            	<a href="#" class="color-choice">
			                              	<span class="ico" style="background:${style.styleCategoryFill1};">icon</span>
			                              	<span class="arrow">arrow</span>
			                              	<input type="hidden" class="color-input" name="style.styleCategoryFill1" value="${style.styleCategoryFill1}" />
			                            	</a>
			                          	</div>
			                         </div>
	                   	 		 </div>
		                       </li>
		                       
		                       <li>
		                         <div class="category-container">
			                       	 <div class="text category-input-container">
			                       	 	<input id="cat2" data-mdattributeid="${mdAttributeId}" class="category-input" name="style.styleCategory2" type="text" value="${style.styleCategory2}" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
			                       	 </div>
	                   	 		 	 <div class="cell">
					                  	<div class="color-holder">
			                            	<a href="#" class="color-choice">
			                              	<span class="ico" style="background:${style.styleCategoryFill2};">icon</span>
			                              	<span class="arrow">arrow</span>
			                              	<input type="hidden" class="color-input" name="style.styleCategoryFill2" value="${style.styleCategoryFill2}" />
			                            	</a>
			                          	</div>
			                         </div>
	                   	 		 </div>
		                       </li>
		                       <li>
		                         <div class="category-container">
			                       	 <div class="text category-input-container">
			                       	 	<input id="cat3" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="style.styleCategory3" type="text" value="${style.styleCategory3}" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
			                       	 </div>
	                   	 		 	 <div class="cell">
					                  	<div class="color-holder">
			                            	<a href="#" class="color-choice">
			                              	<span class="ico" style="background:${style.styleCategoryFill3};">icon</span>
			                              	<span class="arrow">arrow</span>
			                              	<input type="hidden" class="color-input" name="style.styleCategoryFill3" value="${style.styleCategoryFill3}" />
			                            	</a>
			                          	</div>
			                         </div>
	                   	 		 </div>
		                       </li>
		                       <li>
		                         <div class="category-container">
			                       	 <div class="text category-input-container">
			                       	 	<input id="cat4" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="style.styleCategory4" type="text" value="${style.styleCategory4}" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
			                       	 </div>
	                   	 		 	 <div class="cell">
					                  	<div class="color-holder">
			                            	<a href="#" class="color-choice">
			                              	<span class="ico" style="background:${style.styleCategoryFill4};">icon</span>
			                              	<span class="arrow">arrow</span>
			                              	<input type="hidden" class="color-input" name="style.styleCategoryFill4" value="${style.styleCategoryFill4}" />
			                            	</a>
			                          	</div>
			                         </div>
	                   	 		 </div>
		                       </li>		                       		                       
		                       <li>
		                         <div class="category-container">
			                       	 <div class="text category-input-container">
			                       	 	<input id="cat5" data-mdattributeid="${mdAttributeId}" data-type="${categoryType}" class="category-input" name="style.styleCategory5" type="text" value="${style.styleCategory5}" placeholder="${dl_form_cat_input_placeholder}" autocomplete="on" > 
			                       	 </div>
	                   	 		 	 <div class="cell">
					                  	<div class="color-holder">
			                            	<a href="#" class="color-choice">
			                              	<span class="ico" style="background:${style.styleCategoryFill5};">icon</span>
			                              	<span class="arrow">arrow</span>
			                              	<input type="hidden" class="color-input" name="style.styleCategoryFill5" value="${style.styleCategoryFill5}" />
			                            	</a>
			                          	</div>
			                         </div>
	                   	 		 </div>
		                       </li>		                       
		                       
<!-- 		                       <li> -->
<!-- 		                         <div class="category-container"> -->
<!-- 			                       	 <div class="text category-input-container"> -->
<%-- 			                       	 	<input id="cat2" class="category-input" name="style.${style.styleCategory2Md.name}" type="text" placeholder="Enter a category value" autocomplete="on" >  --%>
<!-- 			                       	 </div> -->
<!-- 			                       	 <a href="#"> -->
<%-- 			                       	 	<span class="icon-color category-icon" style="background:${style.styleCategoryFill2};">color</span> --%>
<!-- 			                       	 </a> -->
<%-- 			                       	 <input type="hidden" class="color-input" name="style.${style.styleCategoryFill2Md.name}" value="${style.styleCategoryFill2}" /> --%>
<!-- 		                       	 </div> -->
<!-- 		                       </li> -->

		                     </ul>
		                   </div>
		                 </div>
                    
                        
<!--                         START OF ORIGINAL UI LIST ITEM DESIGN FOR ONTOLOGIES -->
<!--                           <div class="panel"> -->
<!--                             <a class="opener" data-toggle="collapse" href="#choice-color01">First Node <span class="icon-color">color</span></a> -->
<!--                             <div id="choice-color01" class="panel-collapse collapse"> -->
<!--                               <ul class="color-list"> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                                 <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li> -->
<!--                               </ul> -->
<!--                             </div> -->
<!--                           </div> -->
<!--                         END OF ORIGINAL UI LIST ITEM DESIGN FOR ONTOLOGIES -->

                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title"><gdb:localize var="dl_form_stroke" key="DashboardLayer.form.stroke"/>${dl_form_stroke}</strong>
                      <div id="gdb-reusable-categories-stroke-cell-holder" class="cell-holder">
						 <%-- Dynamically inserted with javascript--%>
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
                    <input id="f65" type="checkbox" <c:if test="${layer.displayInLegend}">checked</c:if> name="layer.${layer.displayInLegendMd.name}">
                    <label for="f65">${layer.displayInLegendMd.displayLabel}</label>
</mjl:component>
                  </div>
                </div>
                <div class="button-holder">
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.create.button" 
							      value="Map It" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.applyWithStyle.mojo"
							      classes="btn btn-primary"  />
							    <!-- <input id="DashboardLayer-create-button" name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.create.button" value="Map It" class="btn btn-primary" type="button"> -->
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.cancel.button"
							      value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.cancel.mojo"
							      classes="btn btn-default" />
                </div>
              </div>
            </div>
          </fieldset>
      </div>
    </div>
    
    <script type="text/javascript">

	$( document ).ready(function() {

	  // This prevents building duplicate trees when the javascript is being run twice.
	  // This is a potential bug with a ticket reported #279
	  // 
	  // This was the start of an implementation that got put on hold temporarily. The current rootTerm param
	  // is being set from the controller as a request attribute but needs to be set as the roots of the layer 
	  // ontology rather than the root of the entire ontology tree for all the data in the system. 
	  if ('${isOntologyAttribute}' === true && $("#ontology-tree").children().length === 0){
		      com.runwaysdk.ui.Manager.setFactory("JQuery");
			  var tree = new com.runwaysdk.geodashboard.ontology.OntologyTree({
			      termType : <%="\"" + ClassifierDTO.CLASS + "\""%>,
			      relationshipTypes : [ <%="\"" + ClassifierIsARelationshipDTO.CLASS + "\""%> ],
			      rootTerm : '${ontologyId}',
				  crud: {
				      create: { // This configuration gets merged into the jquery create dialog.
				        height: 320
				      },
				      update: {
				        height: 320
				      }
				  }
			  });
			  tree.render("#ontology-tree");
	  }
	});
	</script>

