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
<c:set var="page_title" scope="request" value="View a Layer"/>
<dl>
  <mjl:form id="net.geoprism.dashboard.layer.DashboardReferenceLayer.form.id" name="net.geoprism.dashboard.layer.DashboardReferenceLayer.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="BBoxIncluded">
        ${item.BBoxIncluded ? item.BBoxIncludedMd.positiveDisplayLabel : item.BBoxIncludedMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="activeByDefault">
        ${item.activeByDefault ? item.activeByDefaultMd.positiveDisplayLabel : item.activeByDefaultMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="dashboardLegend">
        <mjl:struct param="dashboardLegend">
          <mjl:dt attribute="groupedInLegend">
            ${item.dashboardLegend.groupedInLegend ? item.dashboardLegend.groupedInLegendMd.positiveDisplayLabel : item.dashboardLegend.groupedInLegendMd.negativeDisplayLabel}
          </mjl:dt>
          <mjl:dt attribute="legendXPosition">
            ${item.dashboardLegend.legendXPosition}
          </mjl:dt>
          <mjl:dt attribute="legendYPosition">
            ${item.dashboardLegend.legendYPosition}
          </mjl:dt>
        </mjl:struct>
      </mjl:dt>
      <mjl:dt attribute="dashboardMap">
        ${item.dashboardMap.keyName}
      </mjl:dt>
      <mjl:dt attribute="displayInLegend">
        ${item.displayInLegend ? item.displayInLegendMd.positiveDisplayLabel : item.displayInLegendMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="geoEntity">
        ${item.geoEntity.keyName}
      </mjl:dt>
      <mjl:dt attribute="lastPublishDate">
        ${item.lastPublishDate}
      </mjl:dt>
      <mjl:dt attribute="layerEnabled">
        ${item.layerEnabled ? item.layerEnabledMd.positiveDisplayLabel : item.layerEnabledMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="layerType">
        <ul>
          <c:forEach items="${item.layerTypeEnumNames}" var="enumName">
            <li>
              ${item.layerTypeMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
      <mjl:dt attribute="universal">
        ${item.universal.keyName}
      </mjl:dt>
      <mjl:dt attribute="viewName">
        ${item.viewName}
      </mjl:dt>
      <mjl:dt attribute="virtual">
        ${item.virtual ? item.virtualMd.positiveDisplayLabel : item.virtualMd.negativeDisplayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="net.geoprism.dashboard.layer.DashboardReferenceLayer.form.edit.button" value="Edit" action="net.geoprism.dashboard.layer.DashboardReferenceLayerController.edit.mojo" />
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
        <mjl:commandLink name="net.geoprism.dashboard.HasStyle.parentQuery.link" action="net.geoprism.dashboard.HasStyleController.parentQuery.mojo">
          
          <mjl:property name="parentId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
  <dt>
    <label>
      Child Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="net.geoprism.dashboard.layer.HasLayer.childQuery.link" action="net.geoprism.dashboard.layer.HasLayerController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="net.geoprism.dashboard.layer.DashboardReferenceLayer.viewAll.link" action="net.geoprism.dashboard.layer.DashboardReferenceLayerController.viewAll.mojo">
  View All
</mjl:commandLink>
