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
<%@ taglib uri="../../WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
       
	            <div class="row-holder" style="display:none">
	              <div class="label-holder">
	                <strong><gdb:localize key="DashboardThematicLayer.form.styleTheLayer"/></strong>
	              </div>
	              <div class="holder">
	                <div id="layer-type-styler-container" class="tab-content"> 
	                  
	                  <!-- BASICPOINT -->
<!-- 	                  <div ng-show="'{{thematicLayerModel.layerType}}' == 'BASICPOINT'" class="tab-pane" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'BASICPOINT' }" id="tab001basicpoint"> -->
	                  <div ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'BASICPOINT' }" class="tab-pane" id="tab001basicpoint">
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="cell-holder">
		                        <div class="cell">
		                          <span><gdb:localize key="DashboardLayer.form.color"/></span>
		                          <div class="color-holder">
		                            <a href="#" class="color-choice">
		                              <span class="ico" style="background:{{thematicStyleModel.pointFill}}">icon</span>
		                              <span class="arrow">arrow</span>
		                              <input type="text" style="display: none;" class="color-input" name="style.pointFill" ng-model="thematicStyleModel.pointFill" />
		                            </a>
		                          </div>
		                        </div>
		                        <div class="cell">
		                          <label for="basic-point-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
		                          <div class="text">
		                            <select id="basic-point-opacity-select" class="tab-select" name="style.pointOpacity"
	                             		ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             		ng-model="thematicStyleModel.pointOpacity">
	                             	</select>
		                          </div>
		                        </div>
	                      	</div>
	                    </div>
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize key="DashboardLayer.form.stroke"/></strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.color"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.pointStroke}};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.pointStroke" ng-model="thematicStyleModel.pointStroke" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="basic-point-stroke-select"><gdb:localize key="DashboardLayer.form.width"/></label>
	                          <div class="select-holder">
	                          	 <select name="style.pointStrokeWidth" id="basic-point-stroke-select" class="tab-select" 
	                             	ng-options="n for n in [] | intrange:1:16"
	                             	ng-model="thematicStyleModel.pointStrokeWidth">
	                             </select>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="basic-point-stroke-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
	                          <div class="text">
	                             	<select id="basic-point-stroke-opacity-select" class="tab-select" name="style.pointStrokeOpacity"
	                             		ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             		ng-model="thematicStyleModel.pointStrokeOpacity">
	                             	</select>
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.shapeHeading"/></strong>
	                      <div class="cell-holder">
		                    	<div class="cell">
		                          <label for="basic-point-radius-select"><gdb:localize key="DashboardLayer.form.size"/></label>
		                          <div class="text">
		                          	<input id="basic-point-radius-select" name="style.basicPointSize" type="text" ng-model="thematicStyleModel.basicPointSize" placeholder="{{thematicStyleModel.basicPointSize}}"></input>
		                          </div>
		                        </div>
	                      </div>
	                    
		                    <div id="point-type-container" class="cell">
			                  <label for="point-type"><gdb:localize key="DashboardLayer.form.pointType"/></label>
			                  <div class="select-box">
			                    <select id="point-type" class="method-select" name="style.pointWellKnownName" 
			                      ng-model="thematicStyleModel.pointWellKnownName" ng-options="wkn as wkn for wkn in dynamicDataModel.pointTypes track by wkn">
			                    </select>
			                  </div>
			                </div>
		                </div>
	                  </div>
	                  
	                  
	                  <!-- GRADIENT POINT -->
<!-- 	                  <div ng-show="'{{thematicLayerModel.layerType}}' == 'GRADIENTPOINT'" class="tab-pane" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'GRADIENTPOINT' }" id="tab006gradientpoint"> -->
	                  <div class="tab-pane" id="tab006gradientpoint" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'GRADIENTPOINT' }">
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
						  <div class="cell-holder">
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.minFill"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.gradientPointMinFill}}">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.gradientPointMinFill" ng-model="thematicStyleModel.gradientPointMinFill" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.maxFill"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.gradientPointMaxFill}};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.${style.gradientPointMaxFillMd.name}" ng-model="thematicStyleModel.gradientPointMaxFill" />
	                            </a>
	                          </div>
	                        </div>
	                        <div id="gradient-point-fill-opacity-cell" class="cell">
			                    <label for="gradient-point-fill-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
			                    	<select id="gradient-point-fill-opacity-select" class="tab-select" name="style.gradientPointFillOpacity"
	                             		ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             		ng-model="thematicStyleModel.gradientPointFillOpacity">
	                             	</select>  
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.stroke"/></strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.color"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.gradientPointStroke}};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.gradientPointStroke" ng-model="thematicStyleModel.gradientPointStroke" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="gradient-point-stroke-select"><gdb:localize key="DashboardLayer.form.width"/></label>
	                          <div class="select-holder">
	                              
	                             <select name="style.gradientPointStrokeWidth" id="gradient-point-stroke-select" class="tab-select" 
	                             	ng-options="n for n in [] | intrange:1:16"
	                             	ng-model="thematicStyleModel.gradientPointStrokeWidth">
	                             </select>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="gradient-point-stroke-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
	                          <div class="text">
	                             
	                             <select id="gradient-point-stroke-opacity-select" class="tab-select" name="style.gradientPointStrokeOpacity"
	                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             	ng-model="thematicStyleModel.gradientPointStrokeOpacity">
	                             </select>
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="fill-block">
	                     <strong class="title"><gdb:localize key="DashboardThematicLayer.form.shapeHeading"/></strong>
	                      <div class="cell-holder">
		                    	<div class="cell">
		                          <label for="gradient-point-radius-select"><gdb:localize key="DashboardLayer.form.size"/></label>
		                          <div class="text">
		                          	<input id="gradient-point-radius-select" name="style.gradientPointSize" type="text" value="{{thematicStyleModel.gradientPointSize}}"></input>
		                          </div>
		                        </div>
	                      </div>
	                      <div id="gradient-point-type-container" class="cell">
			                  <label for="gradient-point-type"><gdb:localize var="pt_type" key="DashboardLayer.form.pointType"/>${pt_type}</label>
			                  <div class="select-box">
			                    
			                    <select id="gradient-point-type" class="method-select" name="style.gradientPointWellKnownName"
			                      ng-model="thematicStyleModel.gradientPointWellKnownName" ng-options="wkn as wkn for wkn in dynamicDataModel.pointTypes track by wkn">
			                    </select>
			                  </div>
			                </div>
	                    </div>
	                  </div>
	                  
	                  
	                  <!-- CATEGORY POINT -->
	                  <div class="tab-pane" id="tab007categoriespoint" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'CATEGORYPOINT' }">
	                  
	                    <div class="color-section">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="heading-list">
	                        <span><gdb:localize key="DashboardThematicLayer.form.category"/></span>
	                        <span><gdb:localize key="DashboardThematicLayer.form.color"/></span>
	                        <span><gdb:localize var="dl_form_cat_input_placeholder" key="DashboardThematicLayer.form.catInputPlaceholder"/></span>
	                      </div>
	                    <div class="category-block" id="category-point-colors-container">
	                      	  
						<!-- TODO: remove this input after full angular integration -->
	                    <input id="categories-point-input" data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-type="dynamicDataModel.thematicAttributeDataType" data-categoriesstore="{pointcategories}" type="hidden" class="category-input" name="style.categoryPointStyles" ></input>
									
									<!-- RENDER ONTOLOGY TREE DATA  -->
		                      		<div class="ontology-category-input-container" ng-if="dynamicDataModel.isOntologyAttribute">
										<div id="points-ontology-tree" data-termtype="{{dynamicDataModel.termType}}" data-reltype="{{dynamicDataModel.relationshipType}}" data-roots='{{dynamicDataModel.roots}}' ></div>
										<div id="other-cat-point-container" class="other-cat-container" ng-show="categoryWidget.ontPointOtherOptionSelected">
											<ul class="color-list other-cat">							                       
											   <li>
						                         <div class="category-container">
							                       	 <div class="text category-input-container">
							                       	 	<p id="cat-point-other-basic-label" ><gdb:localize key="Other"/></p>
							                       	 </div>
							                       	 <a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">
	                  									<span id="cat-point-other-color-selector" class="ico ontology-category-color-icon ontology-other-color-icon" style="background:#737678; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>
	                								 </a>
					                   	 		 </div>
						                       </li>	                       
						                    </ul>
						                </div>
									  	<div class="check-block">
								      		<input id="ont-cat-point-other-option" class="other-option-check-box" type="checkbox" name="otherOption" ng-model="categoryWidget.ontPointOtherOptionSelected"></input>
								        	<label for="ont-cat-point-other-option"><gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/></label>
								      	</div>
									</div>

									<!-- RENDER BASIC CATEGORIES -->
			                        <div class="panel-group choice-color category-group"  ng-if="!dynamicDataModel.isOntologyAttribute">
										<div class="panel">
					                    	<div id="choice-color02" class="panel-collapse">
					                    		<ul class="color-list">
					                    			<li ng-repeat="cat in categoryWidget.basicPointCatOptionsObj.catLiElems track by $index" ng-show="!cat.otherCat || categoryWidget.pointCatOtherOptionSelected">
												       <div class="category-container">
												          <div class="text category-input-container">
<!-- 												          thematicStyleModel.categoryPointStyles -->
												          	<!-- Regular cat  -->
												          	<input ng-show="!cat.otherCat" id="cat-{{$index}}" class="category-input"  data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-mdattributetype="{{dynamicDataModel.thematicAttributeDataType}}" type="text" value="{{cat.val}}" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="yes" category-auto-complete>
															<!-- OTHER cat  -->
												          	<input ng-show="cat.otherCat" id="cat-{{$index}}-point" class="category-input"  data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-mdattributetype="{{dynamicDataModel.thematicAttributeDataType}}" type="text" value="{{cat.val}}" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="no" disabled >
												          </div>
												          <div class="cell">
												            <div class="color-holder">
												              <a href="#" class="color-choice">
												                <!-- Regular cat  -->
												                <span ng-show="!cat.otherCat" id="cat-{{$index}}-color-selector" class="ico cat-color-selector" style="background:{{cat.color}}">icon</span>
												                <!-- OTHER cat  -->
												                <span ng-show="cat.otherCat" id="cat-{{$index}}-point-color-selector" class="ico cat-color-selector" style="background:{{cat.color}}">icon</span>
												                <span class="arrow">arrow</span>
												              </a>
												            </div>
												          </div>
												        </div>
												     </li>
					                    		</ul>
	  					                    </div>
							            </div>
							            
										<!-- enable/disable checkbox -->
							            <div class="style-options-block">
										  <div class="check-block">
										    <input id="basic-cat-point-other-option" class="other-option-check-box" type="checkbox" name="" ng-model="categoryWidget.pointCatOtherOptionSelected"></input>
										    <label for="basic-cat-point-other-option"><gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/></label>
										  </div>
						                </div>
			                        </div>
	                      </div>
	                    </div>
	                    
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.stroke"/></strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span><gdb:localize key="DashboardLayer.form.color"/></span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:{{thematicStyleModel.categoryPointStroke}}">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="text" style="display: none;" class="color-input" name="style.categoryPointStroke" ng-model="thematicStyleModel.categoryPointStroke" />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="category-point-stroke-width-select"><gdb:localize key="DashboardLayer.form.width"/></label>
			                    <div class="select-holder">
			                     <select id="category-point-stroke-width-select" class="tab-select" name="style.categoryPointStrokeWidth"
	                             	ng-options="n for n in [] | intrange:1:16"
	                             	ng-model="thematicStyleModel.categoryPointStrokeWidth">
	                             </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="category-point-stroke-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
			                     <select id="category-point-stroke-opacity-select" class="tab-select" name="style.categoryPointStrokeOpacity"
	                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             	ng-model="thematicStyleModel.categoryPointStrokeOpacity">
	                             </select>
			                    </div>
			                  </div>
	                      </div>

	                    <div id="category-point-radius-block" class="fill-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.shapeHeading"/></strong>
	                      <div class="cell-holder">
		                    	<div class="cell">
		                          <label for="category-point-radius-select"><gdb:localize key="DashboardLayer.form.size"/></label>
		                          <div class="text">
		                          	<input id="category-point-radius-select" name="style.categoryPointSize" type="text" value="{{thematicStyleModel.categoryPointSize}}"></input>
		                          </div>
		                        </div>
		                        <div class="cell">
			                    <label for="category-point-fill-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
			                      	<select id="category-point-fill-opacity-select" class="tab-select" name="style.categoryPointFillOpacity"
	                             		ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             		ng-model="thematicStyleModel.categoryPointFillOpacity">
	                             	</select>  
			                    </div>
			                  </div>
	                      </div>
	                      
	                      	<div id="category-point-type-container" class="cell">
			                  <label for="category-point-type"><gdb:localize key="DashboardLayer.form.pointType"/></label>
			                  <div class="select-box">
			                    <select id="category-point-type" class="method-select" name="style.categoryPointWellKnownName" 
			                      ng-model="thematicStyleModel.categoryPointWellKnownName" ng-options="wkn as wkn for wkn in dynamicDataModel.pointTypes track by wkn">
			                    </select>
			                  </div>
			                </div>
	                      </div>
	                    </div>
	                </div>
	                
	                  
	                  <!-- BUBBLE -->
	                  <div class="tab-pane" id="tab002bubble" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'BUBBLE' }">
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.color"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.bubbleFill}};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.bubbleFill" ng-model="thematicStyleModel.bubbleFill" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="f71"><gdb:localize key="DashboardLayer.form.opacity"/></label>
	                          <div class="text">
								 <select id="f71" class="tab-select" name="style.bubbleOpacity"
	                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             	ng-model="thematicStyleModel.bubbleOpacity">
	                             </select>
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.stroke"/></strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.color"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.bubbleStroke}};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.bubbleStroke" ng-model="thematicStyleModel.bubbleStroke" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="f33"><gdb:localize key="DashboardLayer.form.width"/></label>
	                          <div class="select-holder">
	                            
	                             <select name="style.bubbleStrokeWidth" id="f73" class="tab-select"
	                             	ng-options="n for n in [] | intrange:1:16"
	                             	ng-model="thematicStyleModel.bubbleStrokeWidth">
	                             </select>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <label for="f74"><gdb:localize key="DashboardLayer.form.opacity"/></label>
	                          <div class="text">
	                             
	                             <select id="f74" class="tab-select" name="style.bubbleStrokeOpacity"
	                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             	ng-model="thematicStyleModel.bubbleStrokeOpacity">
	                             </select>
	                          </div>
	                        </div>
	                      </div>
	                    </div>
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.shapeHeading"/></strong>
	                      <div class="cell-holder">
		                        <div class="cell">
		                          <label for="f76"><gdb:localize key="DashboardLayer.form.min"/></label>
		                          <div class="text">
		                          	 <input id="f76" name="style.bubbleMinSize" type="text" value="{{thematicStyleModel.bubbleMinSize}}"/>
		                          </div>
		                        </div>
		                        <div class="cell">
		                          <label for="f77"><gdb:localize key="DashboardLayer.form.max"/></label>
		                          <div class="text">
		                            <input id="f77" name="style.bubbleMaxSize" type="text" value="{{thematicStyleModel.bubbleMaxSize}}"></input>
		                          </div>
		                        </div>
		                        
<!-- 		                        <div class="check-block"> -->
<%-- 				                    <input id="f52" type="checkbox" <c:if test="${style.bubbleContinuousSize}">checked</c:if> name="style.bubbleContinuousSize"></input> --%>
<%-- 				                    <label for="f52">${style.bubbleContinuousSizeMd.displayLabel}</label> --%>
<!-- 				                    <mjl:messages attribute="bubbleContinuousSize" classes="error-message"> -->
<!-- 				                      <mjl:message /> -->
<!-- 				                    </mjl:messages> -->
<!-- 				                </div> -->
	                      </div>
	                    </div>
	                    
<!-- 	                    TODO: THIS SECONDARY BOX -->
	                    <div class="secondary-box">
	                      <input type="hidden" id="secondaryCategories" data-categoriesstore="${secondaryCategories}" />
	                      <input type="hidden" id="secondaryAggregationValue" value="${secondaryAggregation}" />
	                      
	                      <strong class="title"><gdb:localize key="DashboardLayer.form.secondaryAttributeStyle"/></strong>
	                      <div class="cell-holder">	                    
	                        <label class="secondary-label" for="secondaryAttribute" ><gdb:localize key="DashboardLayer.form.secondaryAttribute"/></label>
  	                        <div id="secondary-select-box" class="select-box">
	                          
	                          	<select rebuild-secondary-agg-method-dropdown id="secondaryAttribute" class="method-select" name="secondaryAttribute"
			                      ng-model="thematicStyleModel.secondaryAttribute" ng-options="attr as attr.label for attr in dynamicDataModel.secondaryAttributes track by attr.id">
			                    	<option label="<gdb:localize key="DashboardLayer.form.noSelection"/>" value=""></option>
			                    </select>
			                    
<!-- 	                          <select id="secondaryAttribute" class="method-select" name="secondaryAttribute"> -->
<%-- 	                            <option ${style.secondaryAttributeId == '' ? 'selected="selected"' : ''} value=""><gdb:localize key="DashboardLayer.form.none"/></option> --%>
<%-- 	                            <c:forEach items="${secondaryAttributes}" var="secondaryAttribute"> --%>
<%-- 		                          <option ${style.secondaryAttributeId == secondaryAttribute.mdAttributeId ? 'selected="selected"' : ''} value="${secondaryAttribute.mdAttributeId}" data-type="${secondaryAttribute.attributeType}">${secondaryAttribute.displayLabel}</option> --%>
<!-- 		                        </c:forEach> -->
<!-- 		                      </select> -->
	                        </div>
	                      </div>
					
	                      <div id="secondary-content" ng-show="secondaryAttributeIsValid()" >
     	                    <div class="cell-holder">
	                          <label class="secondary-label" for="secondaryAggregation"><gdb:localize key="DashboardLayer.form.secondaryAggregation"/></label>
	                          <div class="select-box" id="secondary-aggregation-container"> 
<!-- 	                          		<select rebuild-secondary-agg-method-dropdown id="secondaryAggregation" class="method-select" name="secondaryAggregation"  -->
<!-- 	                          		ng-model="thematicStyleModel.secondaryAggregationType" -->
<!-- 	                          		ng-options="agg as agg.label for agg in dynamicDataModel.secondaryAggregationMethods track by agg.id"> -->
<!-- 	                          		</select> -->
	                          </div> 
	                        </div>
	                        <div id="secondary-cateogries">
								<!-- Ontology categories -->
                                <div class="color-section" ng-show="thematicStyleModel.secondaryAttribute.type == 'com.runwaysdk.system.metadata.MdAttributeTerm'">
						           <div class="heading-list">
						             <span><gdb:localize key="DashboardLayer.form.category"/></span>
						             <span><gdb:localize key="DashboardLayer.form.color"/></span>
						             <span></span>
						           </div>              
						           <div class="category-block">
						             <div class="ontology-category-input-container"></div>
						             <div id="secondary-tree"></div>
						           </div>
						        </div>
						        
	         					<!-- Basic categories -->
		                        <div class="color-section" ng-show="thematicStyleModel.secondaryAttribute.type != 'com.runwaysdk.system.metadata.MdAttributeTerm'">
						            <div class="heading-list">
						              <span><gdb:localize key="DashboardLayer.form.category"/></span>
						              <span><gdb:localize key="DashboardLayer.form.color"/></span>
						              <span></span>
						            </div>
						            <div class="category-block">
						              <div class="panel-group choice-color category-group">
						                <div class="panel">
						                  <div id="secondary-choice-color" class="panel-collapse">
						                  		<ul class="color-list">
					                    			<li ng-repeat="cat in categoryWidget.secondaryCatOptionsObj.catLiElems track by $index">
												       <div class="category-container">
												          <div class="text category-input-container">
															<!-- thematicStyleModel.categoryPolygonStyles -->
															<!-- Regular cat  -->
												          	<input ng-show="!cat.otherCat" id="secondary-{{$index}}" class="category-input"  data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-mdattributetype="{{dynamicDataModel.thematicAttributeDataType}}" type="text" value="{{cat.val}}" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="on" category-auto-complete>
												          	<!-- OTHER cat  -->
												          	<input ng-show="cat.otherCat" id="secondary-{{$index}}-poly" class="category-input"  data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-mdattributetype="{{dynamicDataModel.thematicAttributeDataType}}" type="text" value="{{cat.val}}" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="off" disabled >
												          </div>
												          <div class="cell">
												            <div class="color-holder">
												              <a href="#" class="color-choice">
												              	<!-- Regular cat  -->
												                <span ng-show="!cat.otherCat" id="secondary-{{$index}}-color-selector" class="ico cat-color-selector" style="background:{{cat.color}}">icon</span>
												                <!-- OTHER cat  -->
												                <span ng-show="cat.otherCat" id="secondary-{{$index}}-poly-color-selector" class="ico cat-color-selector" style="background:{{cat.color}}">icon</span>
												                <span class="arrow">arrow</span>
												              </a>
												            </div>
												          </div>
												        </div>
												     </li>
					                    		</ul>
						                  </div>                              
						                </div>              
						              </div>            
						            </div>
						        </div>  
	                        </div>
	                      </div>
                        </div>
	                  </div>
	                  
	                  
	                  <!-- BASICPOLYGON -->
	                  <div class="tab-pane" id="tab003basicpolygon" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'BASICPOLYGON' }">
	                    <div class="fill-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.color"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{ thematicStyleModel.polygonFill }};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.polygonFill" ng-model="thematicStyleModel.polygonFill"  />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
			                    <label for="basic-polygon-fill-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
			                     <select id="basic-polygon-fill-opacity-select" class="tab-select" name="style.polygonFillOpacity"
	                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
	                             	ng-model="thematicStyleModel.polygonFillOpacity">
	                             </select>
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                      
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.stroke"/></strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span><gdb:localize key="DashboardLayer.form.color"/></span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:{{thematicStyleModel.polygonStroke}};">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="text" style="display: none;" class="color-input" name="style.polygonStroke" ng-model="thematicStyleModel.polygonStroke"  />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="basic-polygon-stroke-width-select"><gdb:localize key="DashboardLayer.form.width"/></label>
			                    <div class="select-holder">
				                     <select id="basic-polygon-stroke-width-select" class="tab-select" name="style.polygonStrokeWidth"
		                             	ng-options="n for n in [] | intrange:1:16"
		                             	ng-model="thematicStyleModel.polygonStrokeWidth">
		                             </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="basic-polygon-stroke-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
				                     <select id="basic-polygon-stroke-opacity-select" class="tab-select" name="style.polygonStrokeOpacity"
		                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
		                             	ng-model="thematicStyleModel.polygonStrokeOpacity">
		                             </select>
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                  </div>
	                  
	                  
	                  <!-- GRADIENT POLYGON -->
	                  <div class="tab-pane" id="tab004gradientpolygon" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'GRADIENTPOLYGON' }">
	                    <div class="gradient-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="cell-holder">
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.min"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.gradientPolygonMinFill}};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.gradientPolygonMinFill" ng-model="thematicStyleModel.gradientPolygonMinFill"/>
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
	                          <span><gdb:localize key="DashboardLayer.form.max"/></span>
	                          <div class="color-holder">
	                            <a href="#" class="color-choice">
	                              <span class="ico" style="background:{{thematicStyleModel.gradientPolygonMaxFill}};">icon</span>
	                              <span class="arrow">arrow</span>
	                              <input type="text" style="display: none;" class="color-input" name="style.gradientPolygonMaxFill" ng-model="thematicStyleModel.gradientPolygonMaxFill" />
	                            </a>
	                          </div>
	                        </div>
	                        <div class="cell">
			                    <label for="gradient-polygon-fill-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
			                      
			                         <select id="gradient-polygon-fill-opacity-select" class="tab-select" name="style.gradientPolygonFillOpacity"
		                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
		                             	ng-model="thematicStyleModel.gradientPolygonFillOpacity">
		                             </select>
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.stroke"/></strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span><gdb:localize key="DashboardLayer.form.color"/></span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:{{thematicStyleModel.gradientPolygonStroke}};">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="text" style="display: none;" class="color-input" name="style.gradientPolygonStroke" ng-model="thematicStyleModel.gradientPolygonStroke" />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="gradient-polygon-stroke-width-select"><gdb:localize key="DashboardLayer.form.width"/></label>
			                    <div class="select-holder">
			                      
			                      	 <select id="gradient-polygon-stroke-width-select" class="tab-select" name="style.gradientPolygonStrokeWidth"
		                             	ng-options="n for n in [] | intrange:1:16"
		                             	ng-model="thematicStyleModel.gradientPolygonStrokeWidth">
		                             </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="gradient-polygon-stroke-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
			                    
			            			 <select id="gradient-polygon-stroke-opacity-select" class="tab-select" name="style.gradientPolygonStrokeOpacity"
		                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
		                             	ng-model="thematicStyleModel.gradientPolygonStrokeOpacity">
		                             </select>          
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                  </div>
	                  
	                 
	                  <!-- CATEGORY POLYGON -->
	                   <div class="tab-pane" id="tab005categoriespolygon" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'CATEGORYPOLYGON' }">
	                    <div class="color-section">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
	                      <div class="heading-list">
	                        <span><gdb:localize key="DashboardThematicLayer.form.category"/></span>
	                        <span><gdb:localize key="DashboardThematicLayer.form.color"/></span>
	                        <span><gdb:localize var="dl_form_cat_input_placeholder" key="DashboardThematicLayer.form.catInputPlaceholder"/></span>
	                      </div>
	                    <div class="category-block" id="category-colors-container">
	                      	  
							<!-- TODO: remove this input after full angular integration -->
	                    	<input id="categories-polygon-input" data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-type="dynamicDataModel.thematicAttributeDataType" data-categoriesstore="{polygoncategories}" type="hidden" class="category-input" name="style.categoryPointStyles" ></input>
	                      	<!-- RENDER ONTOLOGY TREE DATA  -->
                      		<div class="ontology-category-input-container" ng-if="dynamicDataModel.isOntologyAttribute">
								<div id="polygon-ontology-tree" data-termtype="{{dynamicDataModel.termType}}" data-reltype="{{dynamicDataModel.relationshipType}}" data-roots='{{dynamicDataModel.roots}}' ></div>
								<div id="other-cat-poly-container" class="other-cat-container" ng-show="categoryWidget.ontPolygonOtherOptionSelected">
									<ul class="color-list other-cat">							                       
									   <li>
				                         <div class="category-container">
					                       	 <div class="text category-input-container">
					                       	 	<p id="cat-other-basic-label" ><gdb:localize key="Other"/></p>
					                       	 </div>
					                       	 <a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">
                 								<span id="cat-other-color-selector" class="ico ontology-category-color-icon ontology-other-color-icon" style="background:#737678; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>
               								 </a>
			                   	 		 </div>
				                       </li>	                       
				                    </ul>
				                </div>
				                
							  	<div class="check-block">
						      		<input id="ont-cat-poly-other-option" class="other-option-check-box" type="checkbox" name="otherOption" ng-model="categoryWidget.ontPolygonOtherOptionSelected"></input>
						        	<label for="ont-cat-poly-other-option"><gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/></label>
						      	</div>
							</div>

							<!-- RENDER BASIC CATEGORIES -->
	                        <div class="panel-group choice-color category-group"  ng-if="!dynamicDataModel.isOntologyAttribute">
								<div class="panel">
			                    	<div id="choice-color02" class="panel-collapse">
			                    		<ul class="color-list">
			                    			<li ng-repeat="cat in categoryWidget.polygonCatOptionsObj.catLiElems track by $index" ng-show="!cat.otherCat || categoryWidget.polygonCatOtherOptionSelected">
										       <div class="category-container">
										          <div class="text category-input-container">
													<!-- thematicStyleModel.categoryPolygonStyles -->
													<!-- Regular cat  -->
										          	<input ng-show="!cat.otherCat" id="cat-{{$index}}" class="category-input"  data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-mdattributetype="{{dynamicDataModel.thematicAttributeDataType}}" type="text" value="{{cat.val}}" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="on" category-auto-complete>
										          	<!-- OTHER cat  -->
										          	<input ng-show="cat.otherCat" id="cat-{{$index}}-poly" class="category-input"  data-mdattributeid="{{thematicLayerModel.mdAttributeId}}" data-mdattributetype="{{dynamicDataModel.thematicAttributeDataType}}" type="text" value="{{cat.val}}" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="off" disabled >
										          </div>
										          <div class="cell">
										            <div class="color-holder">
										              <a href="#" class="color-choice">
										              	<!-- Regular cat  -->
										                <span ng-show="!cat.otherCat" id="cat-{{$index}}-color-selector" class="ico cat-color-selector" style="background:{{cat.color}}">icon</span>
										                <!-- OTHER cat  -->
										                <span ng-show="cat.otherCat" id="cat-{{$index}}-poly-color-selector" class="ico cat-color-selector" style="background:{{cat.color}}">icon</span>
										                <span class="arrow">arrow</span>
										              </a>
										            </div>
										          </div>
										        </div>
										     </li>
			                    		</ul>
 					                    </div>
					            </div>
					            
								<!-- enable/disable checkbox -->
					            <div class="style-options-block">
								  <div class="check-block">
								    <input id="basic-cat-poly-other-option" class="other-option-check-box" type="checkbox" name="" ng-model="categoryWidget.polygonCatOtherOptionSelected"></input>
								    <label for="basic-cat-poly-other-option"><gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/></label>
								  </div>
				                </div>
	                        </div>
	                      </div>
	                    </div>
	                    
	                    
	                    <div class="stroke-block">
	                      <strong class="title"><gdb:localize key="DashboardThematicLayer.form.stroke"/></strong>
	                      <div class="cell-holder">
	                        	<div class="cell">
				                    <span><gdb:localize key="DashboardLayer.form.color"/></span>
				                    <div class="color-holder">
				                      <a href="#" class="color-choice">
				                       	<span class="ico" style="background:{{thematicStyleModel.categoryPolygonStroke}};">icon</span>
				                       	<span class="arrow">arrow</span>
				                       	<input type="text" style="display: none;" class="color-input" name="style.categoryPolygonStroke" ng-model="thematicStyleModel.categoryPolygonStroke" />
					                  </a>
					                </div>
				                </div>
				                <div class="cell">
			                    <label for="category-polygon-stroke-width-select"><gdb:localize key="DashboardLayer.form.width"/></label>
			                    <div class="select-holder">
			                      	 <select id="category-polygon-stroke-width-select" class="tab-select" name="style.categoryPolygonStrokeWidth"
		                             	ng-options="n for n in [] | intrange:1:16"
		                             	ng-model="thematicStyleModel.categoryPolygonStrokeWidth">
		                             </select>
			                    </div>
			                  </div>
			                  <div class="cell">
			                    <label for="category-polygon-stroke-opacity-select"><gdb:localize key="DashboardLayer.form.opacity"/></label>
			                    <div class="text">
			                      	 <select id="category-polygon-stroke-opacity-select" class="tab-select" name="style.categoryPolygonStrokeOpacity"
		                             	ng-options="getFormattedInt(n) for n in [] | decimalrange:0:101 track by n" 
		                             	ng-model="thematicStyleModel.categoryPolygonStrokeOpacity">
		                             </select>
			                    </div>
			                  </div>
	                      </div>
	                    </div>
	                
	                
	                </div> <!--  end style container  -->
	              </div>  <!--  end holder  -->
	            </div> 
