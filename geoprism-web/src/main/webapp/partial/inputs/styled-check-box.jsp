<%--

    Copyright (c) 2022 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<div class="check-block">
  <div ng-attr-id="{{id || undefined}}" ng-attr-name="{{name}}" ng-click="ctrl.toggle()" ng-class="{'chk-checked' : model}" ng-style="chkstyle" class="jcf-unselectable chk-area">
    <span></span>
  </div>
  <label ng-if="label.length>0" ng-attr-for="{{id || undefined}}">{{label}}</label>
</div>