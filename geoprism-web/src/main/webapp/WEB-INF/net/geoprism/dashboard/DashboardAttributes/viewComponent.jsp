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
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="View a Dashboard Attributes"/>
<dl>
  <mjl:form id="net.geoprism.dashboard.DashboardAttributes.form.id" name="net.geoprism.dashboard.DashboardAttributes.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Dashboard Wrapper
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="net.geoprism.dashboard.MetadataWrapper.form.view.link" action="net.geoprism.dashboard.MetadataWrapperController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Attribute Wrapper
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="net.geoprism.dashboard.AttributeWrapper.form.view.link" action="net.geoprism.dashboard.AttributeWrapperController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
    </mjl:component>
    <mjl:command name="net.geoprism.dashboard.DashboardAttributes.form.edit.button" value="Edit" action="net.geoprism.dashboard.DashboardAttributesController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="net.geoprism.dashboard.DashboardAttributes.viewAll.link" action="net.geoprism.dashboard.DashboardAttributesController.viewAll.mojo">
  View All
</mjl:commandLink>
