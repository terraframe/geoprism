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
<c:set var="page_title" scope="request" value="View a Has Layer"/>
<dl>
  <mjl:form id="net.geoprism.dashboard.layer.HasLayer.form.id" name="net.geoprism.dashboard.layer.HasLayer.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <dt>
        <label>
          Map
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="net.geoprism.dashboard.DashboardMap.form.view.link" action="net.geoprism.dashboard.DashboardMapController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <dt>
        <label>
          Layer
        </label>
      </dt>
      <dd>
        <mjl:commandLink name="net.geoprism.dashboard.layer.DashboardLayer.form.view.link" action="net.geoprism.dashboard.layer.DashboardLayerController.view.mojo">
          ${item.parent.keyName}
          <mjl:property name="id" value="${item.parentId}" />
        </mjl:commandLink>
      </dd>
      <mjl:dt attribute="layerIndex">
        ${item.layerIndex}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="net.geoprism.dashboard.layer.HasLayer.form.edit.button" value="Edit" action="net.geoprism.dashboard.layer.HasLayerController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="net.geoprism.dashboard.layer.HasLayer.viewAll.link" action="net.geoprism.dashboard.layer.HasLayerController.viewAll.mojo">
  View All
</mjl:commandLink>
