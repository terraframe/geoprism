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
<c:set var="page_title" scope="request" value="View a Thematic Attribute"/>
<dl>
  <mjl:form method="POST" name="net.geoprism.dashboard.DashboardThematicStyle.form.name" id="net.geoprism.dashboard.DashboardThematicStyle.form.id">
    <mjl:input param="id" type="hidden" value="${item.id}" />
    <mjl:component item="${item}" param="dto">
      <mjl:dt attribute="bubbleContinuousSize">
        ${item.bubbleContinuousSize ? item.bubbleContinuousSizeMd.positiveDisplayLabel : item.bubbleContinuousSizeMd.negativeDisplayLabel}
      </mjl:dt>
      <mjl:dt attribute="bubbleFill">
        ${item.bubbleFill}
      </mjl:dt>
      <mjl:dt attribute="bubbleMaxSize">
        ${item.bubbleMaxSize}
      </mjl:dt>
      <mjl:dt attribute="bubbleMinSize">
        ${item.bubbleMinSize}
      </mjl:dt>
      <mjl:dt attribute="bubbleOpacity">
        ${item.bubbleOpacity}
      </mjl:dt>
      <mjl:dt attribute="bubbleRotation">
        ${item.bubbleRotation}
      </mjl:dt>
      <mjl:dt attribute="bubbleSize">
        ${item.bubbleSize}
      </mjl:dt>
      <mjl:dt attribute="bubbleStroke">
        ${item.bubbleStroke}
      </mjl:dt>
      <mjl:dt attribute="bubbleStrokeOpacity">
        ${item.bubbleStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="bubbleStrokeWidth">
        ${item.bubbleStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="bubbleWellKnownName">
        ${item.bubbleWellKnownName}
      </mjl:dt>
      <mjl:dt attribute="categoryPointFillOpacity">
        ${item.categoryPointFillOpacity}
      </mjl:dt>
      <mjl:dt attribute="categoryPointSize">
        ${item.categoryPointSize}
      </mjl:dt>
      <mjl:dt attribute="categoryPointStroke">
        ${item.categoryPointStroke}
      </mjl:dt>
      <mjl:dt attribute="categoryPointStrokeOpacity">
        ${item.categoryPointStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="categoryPointStrokeWidth">
        ${item.categoryPointStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="categoryPointStyles">
        ${item.categoryPointStyles}
      </mjl:dt>
      <mjl:dt attribute="categoryPointWellKnownName">
        ${item.categoryPointWellKnownName}
      </mjl:dt>
      <mjl:dt attribute="categoryPolygonFillOpacity">
        ${item.categoryPolygonFillOpacity}
      </mjl:dt>
      <mjl:dt attribute="categoryPolygonStroke">
        ${item.categoryPolygonStroke}
      </mjl:dt>
      <mjl:dt attribute="categoryPolygonStrokeOpacity">
        ${item.categoryPolygonStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="categoryPolygonStrokeWidth">
        ${item.categoryPolygonStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="categoryPolygonStyles">
        ${item.categoryPolygonStyles}
      </mjl:dt>
      <mjl:dt attribute="gradientPointFillOpacity">
        ${item.gradientPointFillOpacity}
      </mjl:dt>
      <mjl:dt attribute="gradientPointMaxFill">
        ${item.gradientPointMaxFill}
      </mjl:dt>
      <mjl:dt attribute="gradientPointMinFill">
        ${item.gradientPointMinFill}
      </mjl:dt>
      <mjl:dt attribute="gradientPointSize">
        ${item.gradientPointSize}
      </mjl:dt>
      <mjl:dt attribute="gradientPointStroke">
        ${item.gradientPointStroke}
      </mjl:dt>
      <mjl:dt attribute="gradientPointStrokeOpacity">
        ${item.gradientPointStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="gradientPointStrokeWidth">
        ${item.gradientPointStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="gradientPointWellKnownName">
        ${item.gradientPointWellKnownName}
      </mjl:dt>
      <mjl:dt attribute="gradientPolygonFillOpacity">
        ${item.gradientPolygonFillOpacity}
      </mjl:dt>
      <mjl:dt attribute="gradientPolygonMaxFill">
        ${item.gradientPolygonMaxFill}
      </mjl:dt>
      <mjl:dt attribute="gradientPolygonMinFill">
        ${item.gradientPolygonMinFill}
      </mjl:dt>
      <mjl:dt attribute="gradientPolygonStroke">
        ${item.gradientPolygonStroke}
      </mjl:dt>
      <mjl:dt attribute="gradientPolygonStrokeOpacity">
        ${item.gradientPolygonStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="gradientPolygonStrokeWidth">
        ${item.gradientPolygonStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="secondaryAggregationType">
        <ul>
          <c:forEach var="enumName" items="${item.secondaryAggregationTypeEnumNames}">
            <li>
              ${item.secondaryAggregationTypeMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:dt>
      <mjl:dt attribute="secondaryAttribute">
        ${item.secondaryAttribute.keyName}
      </mjl:dt>
      <mjl:dt attribute="secondaryCategories">
        ${item.secondaryCategories}
      </mjl:dt>
      <mjl:dt attribute="basicPointSize">
        ${item.basicPointSize}
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
      <mjl:dt attribute="labelHaloWidth">
        ${item.labelHaloWidth}
      </mjl:dt>
      <mjl:dt attribute="labelSize">
        ${item.labelSize}
      </mjl:dt>
      <mjl:dt attribute="lineOpacity">
        ${item.lineOpacity}
      </mjl:dt>
      <mjl:dt attribute="lineStroke">
        ${item.lineStroke}
      </mjl:dt>
      <mjl:dt attribute="lineStrokeCap">
        ${item.lineStrokeCap}
      </mjl:dt>
      <mjl:dt attribute="lineStrokeWidth">
        ${item.lineStrokeWidth}
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
      <mjl:dt attribute="pointStroke">
        ${item.pointStroke}
      </mjl:dt>
      <mjl:dt attribute="pointStrokeOpacity">
        ${item.pointStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="pointStrokeWidth">
        ${item.pointStrokeWidth}
      </mjl:dt>
      <mjl:dt attribute="pointWellKnownName">
        ${item.pointWellKnownName}
      </mjl:dt>
      <mjl:dt attribute="polygonFill">
        ${item.polygonFill}
      </mjl:dt>
      <mjl:dt attribute="polygonFillOpacity">
        ${item.polygonFillOpacity}
      </mjl:dt>
      <mjl:dt attribute="polygonStroke">
        ${item.polygonStroke}
      </mjl:dt>
      <mjl:dt attribute="polygonStrokeOpacity">
        ${item.polygonStrokeOpacity}
      </mjl:dt>
      <mjl:dt attribute="polygonStrokeWidth">
        ${item.polygonStrokeWidth}
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
      <mjl:dt attribute="valueHaloWidth">
        ${item.valueHaloWidth}
      </mjl:dt>
      <mjl:dt attribute="valueSize">
        ${item.valueSize}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="net.geoprism.dashboard.DashboardThematicStyle.form.edit.button" action="net.geoprism.dashboard.DashboardThematicStyleController.edit.mojo" value="Edit" />
  </mjl:form>
</dl>
<dl>
</dl>
<mjl:commandLink name="net.geoprism.dashboard.DashboardThematicStyle.viewAll.link" action="net.geoprism.dashboard.DashboardThematicStyleController.viewAll.mojo">
  View All
</mjl:commandLink>
