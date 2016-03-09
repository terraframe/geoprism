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
<div class="checks-frame">
  <div>
    <input ng-model="attribute.filter.value"  class="gdb-attr-filter filter-boolean" id="{{attribute.mdAttributeId}}-true" name="filter-{{attribute.mdAttributeId}}" type="radio" value="true">
    <label for="{{attribute.mdAttributeId}}-true"><gdb:localize key="filter.true" /></label>
  </div>
  <div>
    <input ng-model="attribute.filter.value" class="gdb-attr-filter filter-boolean" id="{{attribute.mdAttributeId}}-false" name="filter-{{attribute.mdAttributeId}}" type="radio" value="false">
    <label for="{{attribute.mdAttributeId}}-false"><gdb:localize key="filter.false" /></label>
  </div>
</div>