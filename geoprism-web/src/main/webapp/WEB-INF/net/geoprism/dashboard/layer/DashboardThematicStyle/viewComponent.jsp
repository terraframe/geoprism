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
<c:set var="page_title" scope="request" value="View a Thematic Attribute"/>
<dl>
  <mjl:form id="net.geoprism.dashboard.DashboardThematicStyle.form.id" name="net.geoprism.dashboard.DashboardThematicStyle.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="mdAttribute">
        ${item.mdAttribute.keyName}
      </mjl:dt>
      <mjl:dt attribute="styleCondition">
        ${item.styleCondition.keyName}
      </mjl:dt>
      <mjl:dt attribute="enableLabel">
        ${item.enableLabel ? item.enableLabelMd.positiveDisplayLabel : item.enableLabelMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="enableValue">
        ${item.enableValue ? item.enableValueMd.positiveDisplayLabel : item.enableValueMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="labelColor">
        ${item.labelColor}
      </mjl:dt>
      <mjl:dt attribute="labelFont">
        ${item.labelFont}
      </mjl:dt>
      <mjl:dt attribute="labelHalo">
        ${item.labelHalo}
      </mjl:dt>
      <mjl:dt attribute="labelSize">
        ${item.labelSize}
      </mjl:dt>
      <mjl:dt attribute="labelWidth">
        ${item.labelWidth}
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
      <mjl:dt attribute="pointFill">
        ${item.pointFill}
      </mjl:dt>
      <mjl:dt attribute="pointOpacity">
        ${item.pointOpacity}
      </mjl:dt>
      <mjl:dt attribute="pointRotation">
        ${item.pointRotation}
      </mjl:dt>
      <mjl:dt attribute="pointSize">
        ${item.pointSize}
      </mjl:dt>
      <mjl:dt attribute="pointStroke">
        ${item.pointStroke}
      </mjl:dt>
      <mjl:dt attribute="pointStrokeWidth">
        ${item.pointStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="polygonFill">
        ${item.polygonFill}
      </mjl:dt>
      <mjl:dt attribute="polygonStroke">
        ${item.polygonStroke}
      </mjl:dt>
      <mjl:dt attribute="polygonStrokeWidth">
        ${item.polygonStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="polygoneFillOpacity">
        ${item.polygoneFillOpacity}
      </mjl:dt>
      <mjl:dt attribute="polygoneStrokeOpacity">
        ${item.polygoneStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="valueColor">
        ${item.valueColor}
      </mjl:dt>
      <mjl:dt attribute="valueFont">
        ${item.valueFont}
      </mjl:dt>
      <mjl:dt attribute="valueHalo">
        ${item.valueHalo}
      </mjl:dt>
      <mjl:dt attribute="valueSize">
        ${item.valueSize}
      </mjl:dt>
      <mjl:dt attribute="valueWidth">
        ${item.valueWidth}
      </mjl:dt>
      <mjl:dt attribute="wellKnownName">
        ${item.wellKnownName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="net.geoprism.dashboard.DashboardThematicStyle.form.edit.button" value="Edit" action="net.geoprism.dashboard.DashboardThematicStyleController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
  <dt>
    <label>
      Child Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="net.geoprism.dashboard.HasStyle.childQuery.link" action="net.geoprism.dashboard.HasStyleController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="net.geoprism.dashboard.DashboardThematicStyle.viewAll.link" action="net.geoprism.dashboard.DashboardThematicStyleController.viewAll.mojo">
  View All
</mjl:commandLink>
