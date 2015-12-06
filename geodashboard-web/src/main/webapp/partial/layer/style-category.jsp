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

<div class="category-container">
  <div class="text category-input-container">
    <!-- Regular cat  -->
    <input class="category-input" ng-disabled="category.otherCat" type="text" ng-model="category.val" placeholder="<gdb:localize key="DashboardLayer.form.catPlaceHolder"/>" autocomplete="on" category-auto-complete source="autoComplete()">
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