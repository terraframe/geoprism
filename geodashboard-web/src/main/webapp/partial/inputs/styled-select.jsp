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
<div class="select-box" ng-class="selectClass">
  <span style="width: auto;" class="jcf-unselectable select-method-select select-area" ng-click="ctrl.toggle($event)">
    <span class="left"></span>
    <span class="center jcf-unselectable">{{ctrl.cache[model]}}</span>
    <a class="select-opener"></a>
  </span>
  <div style="display:none; position: fixed;" ng-style="{'top' : ctrl.offset.top + 35, 'width' : ctrl.width}" class="select-options drop-method-select styled-select-options">
    <div class="drop-holder">
      <div class="drop-list">
        <ul>
          <li ng-repeat="opt in options track by opt[value]" rel="{{$index}}" class="jcfcalc"
             ng-class="{'option-even':($index % 2 == 1), 'current-selected':ctrl.isSelected(opt[value]), 'item-selected':hovering}"
             ng-mouseenter="hovering=true"
             ng-mouseleave="hovering=false">
            <a href="#" ng-click="ctrl.setValue(opt)"><span>{{opt[label]}}</span></a>
          </li>
        </ul>
      </div>
    </div>
  </div>
</div>
