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
      <div ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'BASICPOINT' }" class="tab-pane" id="tab001basicpoint">
        
        <!-- BASIC FILL -->
        <style-basic-fill fill="thematicStyleModel.pointFill" opacity="thematicStyleModel.pointOpacity"></style-basic-fill>
      
        <!-- BASIC STROKE -->
        <style-stroke class="stroke-block" stroke="thematicStyleModel.pointStroke" stroke-width="thematicStyleModel.pointStrokeWidth" stroke-opacity="thematicStyleModel.pointStrokeOpacity"></style-stroke>
      
        <!-- BASIC SHAPE -->
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
              <select id="category-point-type" class="method-select" name="style.pointWellKnownName" ng-model="thematicStyleModel.pointWellKnownName" ng-options="wkn as wkn for wkn in dynamicDataModel.pointTypes track by wkn"></select>
            </div>
          </div>
        </div>
      </div>
      
      <!-- GRADIENT POINT -->
      <div class="tab-pane" id="tab006gradientpoint" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'GRADIENTPOINT' }">
      
        <!-- POINT GRADIENT FILL -->
        <style-gradient-fill min-fill="thematicStyleModel.gradientPointMinFill" max-fill="thematicStyleModel.gradientPointMaxFill" opacity="thematicStyleModel.gradientPointFillOpacity" class="point-gradient"></style-gradient-fill>
        
        <!-- POINT GRADIENT STROKE -->
        <style-stroke class="stroke-block" stroke="thematicStyleModel.gradientPointStroke" stroke-width="thematicStyleModel.gradientPointStrokeWidth" stroke-opacity="thematicStyleModel.gradientPointStrokeOpacity"></style-stroke>
        
        <!-- POINT GRADIENT SHAPE -->        
        <div class="fill-block">
         <strong class="title"><gdb:localize key="DashboardThematicLayer.form.shapeHeading"/></strong>
          <div class="cell-holder">
            <div class="cell">
              <label for="gradient-point-radius-select"><gdb:localize key="DashboardLayer.form.size"/></label>
              <div class="text">
                <input id="gradient-point-radius-select" name="style.gradientPointSize" type="text" ng-model="thematicStyleModel.gradientPointSize"></input>
              </div>
            </div>
          </div>
          <div id="gradient-point-type-container" class="cell">
            <label for="gradient-point-type"><gdb:localize key="DashboardLayer.form.pointType"/></label>
            <styled-basic-select options="dynamicDataModel.pointTypes" model="thematicStyleModel.gradientPointWellKnownName" class="method-select"></styled-basic-select>            
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
            <span></span>
          </div>
          
          <div class="category-block" id="category-point-colors-container">

            <!-- RENDER ONTOLOGY TREE DATA  -->
            <style-category-ontology ng-if="dynamicDataModel.isOntologyAttribute" categories="categoryWidget.basicPointCatOptionsObj" ontology="dynamicDataModel"></style-category-ontology>
            
            <!-- RENDER BASIC CATEGORIES -->
            <style-category-list ng-if="!dynamicDataModel.isOntologyAttribute" categories="categoryWidget.basicPointCatOptionsObj" auto-complete="basicCategoryAutocompleteSource"></style-category-list>
          </div>
        </div>
        
        <div class="stroke-block">
          <!-- POINT CATEGORY STROKE -->        
          <style-stroke stroke="thematicStyleModel.categoryPointStroke" stroke-width="thematicStyleModel.categoryPointStrokeWidth" stroke-opacity="thematicStyleModel.categoryPointStrokeOpacity"></style-stroke>
        
          <div id="category-point-radius-block" class="fill-block">
            <strong class="title"><gdb:localize key="DashboardThematicLayer.form.shapeHeading"/></strong>
            <div class="cell-holder">
              <div class="cell">
                <label for="category-point-radius-select"><gdb:localize key="DashboardLayer.form.size"/></label>
                <div class="text">
                  <input id="category-point-radius-select" name="style.categoryPointSize" type="text" ng-model="thematicStyleModel.categoryPointSize"></input>
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
                  ng-model="thematicStyleModel.categoryPointWellKnownName"
                  ng-options="wkn as wkn for wkn in dynamicDataModel.pointTypes track by wkn">
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
     <input id="f76" name="style.bubbleMinSize" type="text" ng-model="thematicStyleModel.bubbleMinSize"/>
  </div>
</div>
<div class="cell">
  <label for="f77"><gdb:localize key="DashboardLayer.form.max"/></label>
  <div class="text">
    <input id="f77" name="style.bubbleMaxSize" type="text" ng-model="thematicStyleModel.bubbleMaxSize"></input>
  </div>
</div>

<!-- <div class="check-block"> -->
<%-- <input id="f52" type="checkbox" <c:if test="${style.bubbleContinuousSize}">checked</c:if> name="style.bubbleContinuousSize"></input> --%>
<%-- <label for="f52">${style.bubbleContinuousSizeMd.displayLabel}</label> --%>
<!-- <mjl:messages attribute="bubbleContinuousSize" classes="error-message"> -->
<!--   <mjl:message /> -->
<!-- </mjl:messages> -->
<!--           </div> -->
          </div>
        </div>
        
<!--         TODO: THIS SECONDARY BOX -->
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
            
<!-- <select id="secondaryAttribute" class="method-select" name="secondaryAttribute"> -->
<%--   <option ${style.secondaryAttributeId == '' ? 'selected="selected"' : ''} value=""><gdb:localize key="DashboardLayer.form.none"/></option> --%>
<%--   <c:forEach items="${secondaryAttributes}" var="secondaryAttribute"> --%>
<%--   <option ${style.secondaryAttributeId == secondaryAttribute.mdAttributeId ? 'selected="selected"' : ''} value="${secondaryAttribute.mdAttributeId}" data-type="${secondaryAttribute.attributeType}">${secondaryAttribute.displayLabel}</option> --%>
<!-- </c:forEach> -->
<!--             </select> -->
            </div>
          </div>
          
          <div id="secondary-content" ng-show="secondaryAttributeIsValid()" >
             <div class="cell-holder">
<label class="secondary-label" for="secondaryAggregation"><gdb:localize key="DashboardLayer.form.secondaryAggregation"/></label>
<div class="select-box" id="secondary-aggregation-container"> 
<!--     <select rebuild-secondary-agg-method-dropdown id="secondaryAggregation" class="method-select" name="secondaryAggregation"  -->
<!--     ng-model="thematicStyleModel.secondaryAggregationType" -->
<!--     ng-options="agg as agg.label for agg in dynamicDataModel.secondaryAggregationMethods track by agg.id"> -->
<!--     </select> -->
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
        <!-- BASIC POLYGON FILL -->
        <style-basic-fill fill="thematicStyleModel.polygonFill" opacity="thematicStyleModel.polygonFillOpacity"></style-basic-fill>
      
        <!-- BASIC POLYGON STROKE -->
        <style-stroke class="stroke-block" stroke="thematicStyleModel.polygonStroke" stroke-width="thematicStyleModel.polygonStrokeWidth" stroke-opacity="thematicStyleModel.polygonStrokeOpacity"></style-stroke>                
      </div>
      
      
      <!-- GRADIENT POLYGON -->
      <div class="tab-pane" id="tab004gradientpolygon" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'GRADIENTPOLYGON' }">
        <!-- POINT GRADIENT FILL -->
        <style-gradient-fill min-fill="thematicStyleModel.gradientPolygonMinFill" max-fill="thematicStyleModel.gradientPolygonMaxFill" opacity="thematicStyleModel.gradientPolygonFillOpacity" class="point-gradient"></style-gradient-fill>
        
        <!-- POINT GRADIENT STROKE -->
        <style-stroke class="stroke-block" stroke="thematicStyleModel.gradientPolygonStroke" stroke-width="thematicStyleModel.gradientPolygonStrokeWidth" stroke-opacity="thematicStyleModel.gradientPolygonStrokeOpacity"></style-stroke>        
      </div>
      
     
      <!-- CATEGORY POLYGON -->
       <div class="tab-pane" id="tab005categoriespolygon" ng-class="{ 'active' : '{{thematicLayerModel.layerType}}' == 'CATEGORYPOLYGON' }">
        <div class="color-section">
          <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
          <div class="heading-list">
            <span><gdb:localize key="DashboardThematicLayer.form.category"/></span>
            <span><gdb:localize key="DashboardThematicLayer.form.color"/></span>
            <span></span>
          </div>
        <div class="category-block" id="category-colors-container">
          <!-- RENDER ONTOLOGY TREE DATA  -->
          <style-category-ontology ng-if="dynamicDataModel.isOntologyAttribute" categories="categoryWidget.polygonCatOptionsObj" ontology="dynamicDataModel"></style-category-ontology>
                    
          <!-- RENDER BASIC CATEGORIES -->
          <style-category-list ng-if="!dynamicDataModel.isOntologyAttribute" categories="categoryWidget.polygonCatOptionsObj" auto-complete="basicCategoryAutocompleteSource"></style-category-list>
        </div>
      </div>
      
      <!-- POINT CATEGORY STROKE -->        
      <style-stroke class="stroke-block" stroke="thematicStyleModel.categoryPolygonStroke" stroke-width="thematicStyleModel.categoryPolygonStrokeWidth" stroke-opacity="thematicStyleModel.categoryPolygonStrokeOpacity"></style-stroke>
    
    </div> <!--  end style container  -->
  </div>  <!--  end holder  -->
</div> 
