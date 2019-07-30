<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

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
<div class="data-block">
  <div class="col">
    <label><gdb:localize key="dashboardViewer.dates.from" /></label>
    <span class="data-text"> 
      <input ng-model="attribute.filter.startDate" class="checkin gdb-attr-filter filter-date" type="text" placeholder="" />
      <a href="#" class="datapicker-opener"></a>
    </span>
  </div>
  <div class="col">
    <label><gdb:localize key="dashboardViewer.dates.to" /></label>
    <span class="data-text"> 
      <input ng-model="attribute.filter.endDate" class="checkout gdb-attr-filter filter-date" type="text" placeholder="" />
      <a href="#" class="datapicker-opener"></a>
    </span>
  </div>
</div>