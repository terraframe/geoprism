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
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Dashboard"/>
<dl>
  <mjl:form id="net.geoprism.dashboard.Dashboard.form.id" name="net.geoprism.dashboard.Dashboard.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="displayLabel">
        ${item.displayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="net.geoprism.dashboard.Dashboard.form.edit.button" value="Edit" action="net.geoprism.dashboard.DashboardController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
  <dt>
    <label>
      Parent Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="net.geoprism.dashboard.DashboardMetadata.parentQuery.link" action="net.geoprism.dashboard.DashboardMetadataController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="net.geoprism.dashboard.Dashboard.viewAll.link" action="net.geoprism.dashboard.DashboardController.viewAll.mojo">
  View All
</mjl:commandLink>
