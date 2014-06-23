<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Include the types of this form to get the default values the MdAction needs -->
<mjl:component param="style" item="${style}">
</mjl:component>

    <div class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1>New map layer for Number of Units</h1>
        </div>
          <fieldset>
            <legend class="none"> modal form</legend>
            <button class="button none">sabmit</button>
            <div class="row-holder">
              <div class="label-holder">
                <strong>Name the layer</strong>
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
                <strong>Labels and Values</strong>
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
                  <!-- 
                  <div class="cell style02">
                    <label for="f52">${style.valueFontMd.displayLabel}</label>
                    <div class="select-holder">
                      <select class="font-select" name="style.${style.valueFontMd.name}" id="f52">
                        <c:forEach items="${fonts}" var="font">
                          <c:choose>
                            <c:when test="${style.valueFont == font}">
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
                    <label for="f53">${style.valueSizeMd.displayLabel}</label>
                    <div class="select-holder">
                      <select class="size-select" name="style.${style.valueSizeMd.name}" id="f53">
                        <c:forEach begin="0" end="30" var="size">
                          <c:choose>
                            <c:when test="${style.valueSize == size}">
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
                    <span>${style.valueColorMd.displayLabel}</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico" style="background:${style.valueColor};">icon</span>
                        <span class="arrow">arrow</span>
                        <input type="hidden" class="color-input" name="style.${style.valueColorMd.name}" value="${style.valueColor}" />
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <span>${style.valueHaloMd.displayLabel}</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico" style="background:${style.valueHalo};">icon</span>
                        <span class="arrow">arrow</span>
                        <input type="hidden" class="color-input" name="style.${style.valueHaloMd.name}" value="${style.valueHalo}" />
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <label for="f54">${style.valueHaloWidthMd.displayLabel}</label>
                    <div class="select-holder">
                      <select class="size-select" name="style.${style.valueHaloWidthMd.name}" id="f54">
                        <option value=""></option>
                        <c:forEach begin="0" end="15" var="size">
                          <c:choose>
                            <c:when test="${style.valueHaloWidth == size}">
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
                  -->
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
                <strong>Define an aggregation method</strong>
              </div>
              <div class="holder add">
                <div class="box">
                  <label for="f58">Group by</label>
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
		                         <option value="${universal.id}">
		                           ${universal.displayLabel.value}
		                         </option>
                           </c:otherwise>
                         </c:choose>
                      </c:forEach>
                    </select>
</mjl:component>
                  </div>
                </div>
<mjl:component param="style" item="${style}">
                <div class="box">
                  <label for="f59">According to</label>
                  <div class="select-box">
                    <select id="f59" class="method-slect" name="style.${style.aggregationTypeMd.name}">
                      <option></option>
                      <c:forEach items="${aggregations}" var="aggregation">
                         <c:choose>
                           <c:when test="">
                             <option value="${fn:length(style.aggregationTypeEnumNames) > 0 && style.aggregationTypeEnumNames[0] == aggregation.enumName}" selected="selected">
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
<mjl:component param="layer" item="${layer}">
            <div class="row-holder">
              <div class="label-holder style04">
                <strong>Choose a layer type</strong>
              </div>
              <div class="holder style04">
                <ul class="nav-tabs type-tabs">
                  <li class="active">
                    <a href="#" data-toggle="tab">
                      <input checked id="radio1" name="layer.${layer.layerTypeMd.name}" value="BASIC" type="radio">
                      <label for="radio1">${features['BASIC']}</label>
                    </a>
                  </li>
                  <li class="bubble">
                    <a href="#" data-toggle="tab">
                      <input id="radio2" name="layer.${layer.layerTypeMd.name}" value="BUBBLE" type="radio">
                      <label for="radio2">${features['BUBBLE']}</label>
                    </a>
                  </li>
                  <li class="gradient">
                    <a href="#" data-toggle="tab">
                      <input id="radio3" name="layer.${layer.layerTypeMd.name}" value="GRADIENT" type="radio">
                      <label for="radio3">${features['GRADIENT']}</label>
                    </a>
                  </li>
                  <!-- Removed for deploy until functionality exists.
                  <li class="category">
                    <a href="#" data-toggle="tab">
                      <input id="radio4" name="layer.${layer.layerTypeMd.name}" value="CATEGORY" type="radio">
                      <label for="radio4">${features['CATEGORY']}</label>
                    </a>
                  </li>
                   -->
                </ul>
              </div>
            </div>
</mjl:component>
<mjl:component param="style" item="${style}">
            <div class="row-holder">
              <div class="label-holder">
                <strong>Style the layer</strong>
              </div>
              <div class="holder">
                <div id="layer-type-styler-container" class="tab-content">
                  <div class="tab-pane active" id="tab001">
                    <div class="fill-block">
                      <strong class="title">Fill</strong>
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
                          <label for="f210">${style.polygonFillOpacityMd.displayLabel}</label>
                          <div class="text">
                            <input id="f210" type="text" value="${style.polygonFillOpacity}" name="style.${style.polygonFillOpacityMd.name}" />
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
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
                          <label for="f63">${style.polygonStrokeWidthMd.displayLabel}</label>
                          <div class="select-holder">
                            <select id="f63" class="tab-select" name="style.${style.polygonStrokeWidthMd.name}">
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
                          <label for="f64">${style.polygonStrokeOpacityMd.displayLabel}</label>
                          <div class="text">
                            <input id="f64" type="text" name="style.${style.polygonStrokeOpacityMd.name}" value="${style.polygonStrokeOpacity}" />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane" id="tab002" style="display: none;">
                    <div class="fill-block">
                      <strong class="title">Fill</strong>
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
                            <input id="f71" type="text" value="${style.pointOpacity}" name="style.${style.pointOpacityMd.name}" />
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>${style.pointStrokeMd.displayLabel}</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:${style.pointFill};">icon</span>
                              <span class="arrow">arrow</span>
                              <input type="hidden" class="color-input" name="style.${style.pointStrokeMd.name}" value="${style.pointStroke}" />
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f33">${style.pointStrokeWidthMd.displayLabel}</label>
                          <div class="select-holder">
                            <select id="f73" class="tab-select">
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
                          <input id="f74" type="text" name="style.${style.pointStrokeOpacityMd.name}" value="${style.pointStrokeOpacity}" />
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="fill-block">
                      <strong class="title">Radius</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <label for="f76">${style.pointMinSizeMd.displayLabel}</label>
                          <div class="text"><input id="f76" name="${style.pointMinSizeMd.name}" type="text" value="${style.pointMinSize}"></div>
                        </div>
                        <div class="cell">
                          <label for="f77">${style.pointMaxSizeMd.displayLabel}</label>
                          <div class="text">
                            <input id="f77" name="${style.pointMaxSizeMd.name}" type="text" value="${style.pointMaxSize}">
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane active" id="tab003" style="display: none;">
                    <div class="gradient-block">
                      <strong class="title">Fill</strong>
                      <div class="cell-holder">
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
                        <div class="cell">
                          <label for="f78">${style.polygonFillOpacityMd.displayLabel}</label>
                          <div class="text">
                            <input id="f78" type="text" name="style.${style.polygonFillOpacityMd.name}" value="${style.polygonFillOpacity}" />
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
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
                        <div class="cell">
                          <label for="f81">${style.polygonStrokeOpacityMd.displayLabel}</label>
                          <div class="text">
                            <input id="f81" type="text" name="style.${style.polygonStrokeOpacityMd.name}" value="${style.polygonStrokeOpacity}" />
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane active" id="tab004" style="display: none;">
                    <div class="color-section">
                      <strong class="title">Fill</strong>
                      <div class="heading-list">
                        <span>Name of Ontology</span>
                        <span>Color</span>
                      </div>
                      <div class="color-block" id="category-colors-container">
                        <div class="panel-group choice-color">
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color01">First Node <span class="icon-color">color</span></a>
                            <div id="choice-color01" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color02">Second Node <span class="icon-color" style="background:#d24dff;">color</span></a>
                            <div id="choice-color02" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color03">Third Node <span class="icon-color" style="background:#00a3d9;">color</span></a>
                            <div id="choice-color03" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color04">Fourth Node <span class="icon-color" style="background:#ffdb6f;">color</span></a>
                            <div id="choice-color04" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color05">Fifth Node <span class="icon-color" style="background:#ff7a4d;">color</span></a>
                            <div id="choice-color05" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color06">Sixth Node <span class="icon-color" style="background:#ffa64d;">color</span></a>
                            <div id="choice-color06" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color07">First Node <span class="icon-color">color</span></a>
                            <div id="choice-color07" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#a6e53b">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color08">Second Node <span class="icon-color" style="background:#d24dff;">color</span></a>
                            <div id="choice-color08" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#d24dff">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color09">Third Node <span class="icon-color" style="background:#00a3d9;">color</span></a>
                            <div id="choice-color09" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#00a3d9">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color10">Fourth Node <span class="icon-color" style="background:#ffdb6f;">color</span></a>
                            <div id="choice-color10" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffdb6f">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color11">Fifth Node <span class="icon-color" style="background:#ff7a4d;">color</span></a>
                            <div id="choice-color11" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                          <div class="panel">
                            <a class="opener" data-toggle="collapse" href="#choice-color12">Sixth Node <span class="icon-color" style="background:#ffa64d;">color</span></a>
                            <div id="choice-color12" class="panel-collapse collapse">
                              <ul class="color-list">
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                                <li><a href="#">Child Node <span class="icon-color" style="background:#ffa64d;">color</span></a></li>
                              </ul>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="stroke-block">
                      <strong class="title">Stroke</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#0086b3;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f101">Width</label>
                          <div class="select-holder">
                            <select id="f101" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f82">Opacity</label>
                          <div class="text"><input id="f82" type="text" placeholder="0 to 100"></div>
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
                    <input id="f65" type="checkbox" <c:if test="${layer.displayInLegend}">checked</c:if> name="layer.${layer.displayInLegendMd.name}">
                    <label for="f65">${layer.displayInLegendMd.displayLabel}</label>
</mjl:component>
                  </div>
                </div>
                <div class="button-holder">
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.create.button" 
							      value="Map It" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.applyWithStyle.mojo"
							      classes="btn btn-primary"  />
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.cancel.button"
							      value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.cancel.mojo"
							      classes="btn btn-default" />
                </div>
              </div>
            </div>
          </fieldset>
      </div>
    </div>
    

