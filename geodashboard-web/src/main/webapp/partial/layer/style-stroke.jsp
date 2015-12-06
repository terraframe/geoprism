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

<div ng-class="divClass">
  <strong class="title"><gdb:localize key="DashboardLayer.form.stroke"/></strong>
  <div class="cell-holder">
    <div class="cell">
      <span><gdb:localize key="DashboardLayer.form.color"/></span>
      <div class="color-holder">
        <a href="#" class="color-choice" color-picker model="stroke" element='#modal01'>
          <span class="ico" style="background:{{stroke}};">icon</span>
          <span class="arrow">arrow</span>
        </a>
      </div>
    </div>
    <div class="cell">
      <label><gdb:localize key="DashboardLayer.form.width"/></label>
      <div class="select-holder">
        <select class="tab-select" ng-options="n for n in [] | intrange:1:16" ng-model="strokeWidth"></select>
      </div>
    </div>
    <div class="cell">
      <label><gdb:localize key="DashboardLayer.form.opacity"/></label>
      <div class="text">
        <select class="tab-select" ng-options="ctrl.getFormattedInt(n) for n in [] | decimalrange:0:101 track by n"  ng-model="strokeOpacity"></select>
      </div>
    </div>
  </div>
</div>
