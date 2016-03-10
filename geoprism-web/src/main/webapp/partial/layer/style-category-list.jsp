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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<div class="panel-group choice-color category-group">
  <div class="panel">
    <div id="choice-color02" class="panel-collapse">
      <ul class="color-list">
        <li ng-repeat="category in categories.catLiElems track by $index">
          <div class="category-container">
            <div class="text category-input-container">
              <div class="category-input-sub-container" ng-class="{'left' : (categories.rangeCategoriesEnabled)}">
              	<i class="range-cat-ico range-cat-left-ico" ng-class="{'active' : (category.rangeAllMin)}" ng-click="toggleMin($event)" ng-show="categories.rangeCategoriesEnabled" title="<gdb:localize key="DashboardThematicLayer.form.minCatRangeTooltip"/>">&#60;</i>
              	<input class="category-input left" type="text" ng-class="{'category-range-input' : (categories.rangeCategoriesEnabled), 'active' : (category.rangeAllMin)}" ng-disabled="category.rangeAllMin" ng-model="category.val" ng-required="categoryCheck()" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="on" category-auto-complete source="autoComplete()" number-only enforce="{{type === 'number'}}"></input>
              </div>
              <div class="category-input-sub-container right">
              	<i class="range-cat-ico range-cat-right-ico" ng-class="{'active' : (category.rangeAllMax)}" ng-click="toggleMax($event)" ng-show="categories.rangeCategoriesEnabled" title="<gdb:localize key="DashboardThematicLayer.form.maxCatRangeTooltip"/>">&#62;</i>
              	<input class="category-input category-range-input cat-range-max-val right" ng-class="{'active' : (category.rangeAllMin)}" ng-disabled="category.rangeAllMax" type="text" ng-model="category.valMax" ng-required="categoryCheck()" ng-show="(categories.rangeCategoriesEnabled)" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="on" category-auto-complete source="autoComplete()" number-only enforce="{{type === 'number'}}"></input>
              </div>	
            </div>
            <div class="cell">
              <styled-color-picker model="category.color" scroll="#layer-modal"></styled-color-picker>             
            </div>
          </div>
        </li> <!-- end categories iterator -->
        
        <!-- Other category -->
        <li>
          <div class="category-container" ng-show="(categories.otherEnabled && showOther == 'true')">
            <div class="text category-input-container">
              <input class="category-input" ng-disabled="true" type="text" placeholder="<gdb:localize key="Other"/>"></input>
            </div>
            <div class="cell">
              <styled-color-picker model="categories.other.color" scroll="#layer-modal"></styled-color-picker>             
            </div>
          </div>          
        </li>
      </ul>
    </div>
  </div>

  <!-- enable/disable checkbox -->
  <div class="style-options-block" ng-show="showOther == 'true'">    
    <styled-check-box id="basic-cat-point-other-option" model="categories.otherEnabled" label="<gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>"></styled-check-box>
  </div>
  
  <!-- enable/disable range categories -->
  <div class="style-options-block" ng-if="type === 'number'">    
    <styled-check-box id="cat-range-categories" model="categories.rangeCategoriesEnabled" label="<gdb:localize key="DashboardThematicLayer.form.categoryRangeCategoriesLabel"/>"></styled-check-box>
  </div>
</div>



