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
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

<div class="panel-group choice-color category-group">
  <div class="panel">
    <div id="choice-color02" class="panel-collapse">
      <ul class="color-list">
        <li ng-repeat="category in categories.catLiElems track by $index">
          
          <div class="category-container" ng-show="!category.otherCat || categories.otherEnabled">
            <div class="text category-input-container">
              <input class="category-input" ng-disabled="category.otherCat" type="text" ng-model="category.val" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="on" category-auto-complete source="autoComplete()"></input>
            </div>
            <div class="cell">
              <div class="color-holder">
                <a href="#" class="color-choice" color-picker model="category.color" element='#modal01'>
                  <span class="ico cat-color-selector" style="background:{{category.color}}">icon</span>
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
    <styled-check-box id="basic-cat-point-other-option" model="categories.otherEnabled" label="<gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>"></styled-check-box>
  </div>
</div>



