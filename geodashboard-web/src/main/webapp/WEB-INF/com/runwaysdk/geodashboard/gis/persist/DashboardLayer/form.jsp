<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<mjl:component param="layer" item="${layer}">

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
                <label class="none" for="f312">${layer.nameMd.displayLabel}</label>
                <span class="text">
                  <mjl:input  type="text" param="layer.name"></mjl:input>
                </span>
              </div>
            </div>
</mjl:component>
<mjl:component param="style" item="${style}">
            <div class="row-holder">
              <div class="label-holder style02">
                <strong>Labels and Values</strong>
              </div>
              <div class="holder">
                <div class="row-holder">
                  <div class="check-block">
                    <input id="f51" class="check" type="checkbox" <c:if test="${style.enableValue}">checked="checked"</c:if> name="style.${style.enableValueMd.name}">
                    <label for="f51">${style.enableValueMd.displayLabel}</label>
                  </div>
                  <div class="cell style02">
                    <label for="f52">${style.valueFontMd.displayLabel}</label>
                    <div class="select-holder">
                      <select class="font-select" name="style.${style.valueFontMd.name}" id="f52">
                        <c:forEach items="${fonts}" var="font">
                          <c:choose>
                            <c:when test="${style.valueFont == font.fontName}">
                              <option value="${font.fontName}" selected="selected">${font.fontName}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${font.fontName}">${font.fontName}</option>
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
                        <span class="ico">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <span>${style.valueHaloMd.displayLabel}</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico" style="background:#fff;">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <label for="f54">${style.valueHaloWidthMd.displayLabel}</label>
                    <div class="select-holder">
                      <select class="size-select" name="style.${style.valueHaloWidthMd.name}" id="f54">
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
                </div>
                <div class="row-holder">
                  <div class="check-block">
                    <input id="f94" class="check" type="checkbox" <c:if test="${style.enableLabel}">checked="checked"</c:if> name="style.${style.enableLabelMd.name}">
                    <label for="f94">${style.enableLabelMd.displayLabel}</label>
                  </div>
                  <div class="cell style02">
                    <label for="f55">${style.labelFontMd.displayLabel}</label>
                    <div class="select-holder">
                    
                      <select class="font-select" name="style.${style.labelFontMd.name}" id="f55">
                        <c:forEach items="${fonts}" var="font">
                          <c:choose>
                            <c:when test="${style.labelFont == font.fontName}">
                              <option value="${font.fontName}" selected="selected">${font.fontName}</option>
                            </c:when>
                            <c:otherwise>
                              <option value="${font.fontName}">${font.fontName}</option>
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
                    <span>Color</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <span>Label Halo</span>
                    <div class="color-holder">
                      <a href="#" class="color-choice">
                        <span class="ico" style="background:#fff;">icon</span>
                        <span class="arrow">arrow</span>
                      </a>
                    </div>
                  </div>
                  <div class="cell">
                    <label for="f57">Halo Width</label>
                    <div class="select-holder">
                      <select class="size-select" id="f57">
                        <option>3</option>
                        <option>4</option>
                        <option>5</option>
                        <option>6</option>
                        <option>7</option>
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
                    <select id="f58" class="method-slect">
                      <option>District</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                    </select>
                  </div>
                </div>
                <div class="box">
                  <label for="f59">According to</label>
                  <div class="select-box">
                    <select id="f59" class="method-slect">
                      <option>Sum</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                      <option>Lorem ipsum</option>
                    </select>
                  </div>
                </div>
              </div>
            </div>
            <div class="row-holder">
              <div class="label-holder style04">
                <strong>Choose a layer type</strong>
              </div>
              <div class="holder style04">
                <ul class="nav-tabs type-tabs">
                  <li class="active">
                    <a href="#" data-toggle="tab">
                      <input checked id="radio1" name="radio-group" type="radio">
                      <label for="radio1">Basic</label>
                    </a>
                  </li>
                  <li class="bubble">
                    <a href="#" data-toggle="tab">
                      <input id="radio2" name="radio-group" type="radio">
                      <label for="radio2">Bubble</label>
                    </a>
                  </li>
                  <li class="gradient">
                    <a href="#" data-toggle="tab">
                      <input id="radio3" name="radio-group" type="radio">
                      <label for="radio3">Gradient</label>
                    </a>
                  </li>
                  <li class="category">
                    <a href="#" data-toggle="tab">
                      <input id="radio4" name="radio-group" type="radio">
                      <label for="radio4">Category</label>
                    </a>
                  </li>
                </ul>
              </div>
            </div>
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
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#00a3d9;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f210">Opacity</label>
                          <div class="text">
                          <input id="f210" type="text" placeholder="0 to 100"></div>
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
                          <label for="f63">Width</label>
                          <div class="select-holder">
                            <select id="f63" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f64">Opacity</label>
                          <div class="text"><input id="f64" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane" id="tab002" style="display: none;">
                    <div class="fill-block">
                      <strong class="title">Fill</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#00a3d9;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f71">Opacity</label>
                          <div class="text"><input id="f71" type="text" placeholder="0 to 100"></div>
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
                          <label for="f73">Width</label>
                          <div class="select-holder">
                            <select id="f73" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f74">Opacity</label>
                          <div class="text"><input id="f74" type="text" placeholder="0 to 100"></div>
                        </div>
                      </div>
                    </div>
                    <div class="fill-block">
                      <strong class="title">Radius</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <label for="f75">Min</label>
                          <div class="text"><input id="f75" type="text" placeholder="10"></div>
                        </div>
                        <div class="cell">
                          <label for="f76">Max</label>
                          <div class="text"><input id="f76" type="text" placeholder="100"></div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="tab-pane active" id="tab003" style="display: none;">
                    <div class="fill-block">
                      <strong class="title">Fill</strong>
                      <div class="cell-holder">
                        <div class="cell">
                          <span>Color</span>
                          <div class="color-holder">
                            <a href="#" class="color-choice">
                              <span class="ico" style="background:#00a3d9;">icon</span>
                              <span class="arrow">arrow</span>
                            </a>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f78">Opacity</label>
                          <div class="text"><input id="f78" type="text" placeholder="0 to 100"></div>
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
                          <label for="f80">Width</label>
                          <div class="select-holder">
                            <select id="f80" class="tab-select">
                              <option>1</option>
                              <option>2</option>
                              <option>3</option>
                              <option>4</option>
                            </select>
                          </div>
                        </div>
                        <div class="cell">
                          <label for="f81">Opacity</label>
                          <div class="text"><input id="f81" type="text" placeholder="0 to 100"></div>
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
            <div class="row-holder">
              <div class="label-holder"></div>
              <div class="holder">
                <div class="row-holder">
                  <div class="check-block style02">
                    <input id="f65" class="check" type="checkbox" checked="checked">
                    <label for="f65">Display in Legend</label>
                  </div>
                </div>
                <div class="button-holder">
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.create.button" 
							      value="Map It" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.create.mojo"
							      classes="btn btn-primary" />
							    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayer.form.cancel.button"
							      value="Cancel" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerController.cancel.mojo"
							      classes="btn btn-default" />
                </div>
              </div>
            </div>
          </fieldset>
      </div>
    </div>
    

