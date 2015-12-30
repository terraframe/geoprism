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
  
  <div class="other-cat-container" ng-show="categories.otherEnabled && showOther == 'true'">
    <ul class="color-list other-cat">
      <li>
        <div class="category-container">
          <div class="text category-input-container">
            <p><gdb:localize key="Other"/></p>
          </div>
          <simple-color-picker category="categories.other" scroll="#layer-modal"></simple-color-picker>
        </div>
      </li>
    </ul>
  </div>
  <div class="check-block" ng-show="showOther == 'true'">
    <styled-check-box model="categories.otherEnabled" label="<gdb:localize key="DashboardThematicLayer.form.categoryOtherOptionLabel"/>"></styled-check-box>
  </div>
</div>
