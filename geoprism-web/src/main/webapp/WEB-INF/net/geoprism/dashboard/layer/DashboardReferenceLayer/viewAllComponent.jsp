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
<c:set var="page_title" scope="request" value="View all Layer"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="net.geoprism.dashboard.layer.DashboardReferenceLayerController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="BBoxIncluded">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="activeByDefault">
    </mjl:attributeColumn>
    <mjl:structColumn attributeName="dashboardLegend">
      <mjl:header>
        
      </mjl:header>
      <mjl:attributeColumn attributeName="groupedInLegend">
      </mjl:attributeColumn>
      <mjl:attributeColumn attributeName="legendXPosition">
      </mjl:attributeColumn>
      <mjl:attributeColumn attributeName="legendYPosition">
      </mjl:attributeColumn>
    </mjl:structColumn>
    <mjl:attributeColumn attributeName="dashboardMap">
      <mjl:row>
        ${item.dashboardMap.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="displayInLegend">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="geoEntity">
      <mjl:row>
        ${item.geoEntity.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="lastPublishDate">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="layerEnabled">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="layerType">
      <mjl:row>
        <ul>
          <c:forEach items="${item.layerTypeEnumNames}" var="enumName">
            <li>
              ${item.layerTypeMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="name">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="universal">
      <mjl:row>
        ${item.universal.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="viewName">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="virtual">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="net.geoprism.dashboard.layer.DashboardReferenceLayerController.view.mojo">
          View
          <mjl:property name="id" value="${item.id}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
  </mjl:columns>
  <mjl:pagination>
    <mjl:page />
  </mjl:pagination>
</mjl:table>
<br />
<mjl:commandLink name="DashboardReferenceLayerController.newInstance" action="net.geoprism.dashboard.layer.DashboardReferenceLayerController.newInstance.mojo">
  Create a new Layer
</mjl:commandLink>
