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
<div>
  <div class="select-holder">
    <select ng-model="attribute.filter.operation" name="{{attribute.mdAttributeId}}-operation" ng-class="{'filter-select':true, 'field-error': form[attribute.mdAttributeId + '-operation'].$invalid }" ng-required='attribute.filter.value' >
      <option value="gt">&gt;</option>
      <option value="ge">&gt;=</option>
      <option value="lt">&lt;</option>
      <option value="le">&lt;=</option>
    </select>
  </div>
  <div class="text">
    <label class="none"><gdb:localize key="dashboardViewer.number"/></label>
         
    <input ng-if="whole" ng-model="attribute.filter.value" name="{{attribute.mdAttributeId}}" ng-class="{'gdb-attr-filter':true, 'filter-number':true, 'field-error':(form[attribute.mdAttributeId].$invalid && !form[attribute.mdAttributeId].$pristine) }" type="text" placeholder="<gdb:localize key="dashboard.number.label" />" integer-only></input>
    <input ng-if="!whole" ng-model="attribute.filter.value" name="{{attribute.mdAttributeId}}" ng-class="{'gdb-attr-filter':true, 'filter-number':true, 'field-error':(form[attribute.mdAttributeId].$invalid && !form[attribute.mdAttributeId].$pristine) }" type="text" placeholder="<gdb:localize key="dashboard.number.label" />" number-only></input>
  </div>
</div>
