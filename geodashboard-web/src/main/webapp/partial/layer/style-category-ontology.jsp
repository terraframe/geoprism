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

<div class="ontology-category-input-container">
  <div class="ontology-tree"></div>
  
  <div class="other-cat-container" ng-show="categories.otherEnabled">
    <ul class="color-list other-cat">
      <li>
        <div class="category-container">
          <div class="text category-input-container">
            <p><gdb:localize key="Other"/></p>
          </div>
          <a href="#" class="color-choice" style="float:right; width:20px; height:20px; padding: 0px; margin-right:15px; border:none;">
            <span class="ico ontology-category-color-icon ontology-other-color-icon" style="background:#737678; border:1px solid #ccc; width:20px; height:20px; float:right; cursor:pointer;">icon</span>
          </a>
        </div>
      </li>
    </ul>
  </div>
  <div class="check-block">
    <styled-check-box model="categories.otherEnabled" label="<gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>"></styled-check-box>
  </div>
</div>
