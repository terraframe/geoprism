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
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component item="${item}" param="dto">
  <mjl:dt attribute="universal">
    <mjl:select valueAttribute="id" param="universal" var="current" items="${_universal}">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="BBoxIncluded">
    <mjl:boolean param="BBoxIncluded" />
  </mjl:dt>
  <mjl:dt attribute="activeByDefault">
    <mjl:boolean param="activeByDefault" />
  </mjl:dt>
  <mjl:dt attribute="dashboardLegend">
    <mjl:struct param="dashboardLegend">
      <mjl:dt attribute="groupedInLegend">
        <mjl:boolean param="groupedInLegend" />
      </mjl:dt>
      <mjl:dt attribute="legendXPosition">
        <mjl:input param="legendXPosition" type="text" />
      </mjl:dt>
      <mjl:dt attribute="legendYPosition">
        <mjl:input param="legendYPosition" type="text" />
      </mjl:dt>
    </mjl:struct>
  </mjl:dt>
  <mjl:dt attribute="dashboardMap">
    <mjl:select valueAttribute="id" param="dashboardMap" var="current" items="${_dashboardMap}">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="displayInLegend">
    <mjl:boolean param="displayInLegend" />
  </mjl:dt>
  <mjl:dt attribute="lastPublishDate">
    <mjl:input param="lastPublishDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="layerEnabled">
    <mjl:boolean param="layerEnabled" />
  </mjl:dt>
  <mjl:dt attribute="layerType">
    <mjl:select valueAttribute="enumName" param="layerType" var="current" items="${_layerType}">
      <mjl:option selected="${mjl:contains(item.layerTypeEnumNames, current.enumName) ? 'selected' : 'false'}">
        ${item.layerTypeMd.enumItems[current.enumName]}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="nameLabel">
    <mjl:input param="nameLabel" type="text" />
  </mjl:dt>
  <mjl:dt attribute="viewName">
    <mjl:input param="viewName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="virtual">
    <mjl:boolean param="virtual" />
  </mjl:dt>
</mjl:component>
