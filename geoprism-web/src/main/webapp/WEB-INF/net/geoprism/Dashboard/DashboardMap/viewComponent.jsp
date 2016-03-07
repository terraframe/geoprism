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
<c:set var="page_title" scope="request" value="View a Map"/>
<dl>
  <mjl:form method="POST" name="net.geoprism.dashboard.DashboardMap.form.name" id="net.geoprism.dashboard.DashboardMap.form.id">
    <mjl:input param="id" type="hidden" value="${item.id}" />
    <mjl:component item="${item}" param="dto">
      <mjl:dt attribute="activeBaseMap">
        ${item.activeBaseMap}
      </mjl:dt>
      <mjl:dt attribute="dashboard">
        ${item.dashboard.keyName}
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="net.geoprism.dashboard.DashboardMap.form.edit.button" action="net.geoprism.dashboard.DashboardMapController.edit.mojo" value="Edit" />
  </mjl:form>
</dl>
<dl>
</dl>
<mjl:commandLink name="net.geoprism.dashboard.DashboardMap.viewAll.link" action="net.geoprism.dashboard.DashboardMapController.viewAll.mojo">
  View All
</mjl:commandLink>
