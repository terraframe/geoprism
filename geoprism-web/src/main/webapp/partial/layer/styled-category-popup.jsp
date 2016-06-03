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

<div id="category-icon-modal-container">
  <div class="cell">
    <styled-check-box model="category.enableIcon" name="style.enableIcon" label="<gdb:localize key="DashboardThematicLayer.form.enableIcon"/>"></styled-check-box>
  </div>
<!--   <div class="cell"> -->
<!--     <styled-color-picker model="category.color" scroll="#layer-modal"></styled-color-picker>              -->
<!--   </div> -->

  <div class="select-box">
    <select class="method-select" ng-model="category.icon" ng-options="opt.id as opt.label for opt in icons">
      <option value=""></option>    
    </select>
  </div>
<!--   <div class="fill-block"> -->
    <div class="cell-holder">
      <div class="cell">
        <label for="custom-icon-radius-select"><gdb:localize key="DashboardLayer.form.size"/></label>
        <div class="text">
          <input id="custom-icon-radius-select" name="category.iconSize" type="text" ng-model="category.iconSize" placeholder="{{category.iconSize}}" required integer-only></input>
        </div>
      </div>
    </div>
<!--   </div> -->

</div>