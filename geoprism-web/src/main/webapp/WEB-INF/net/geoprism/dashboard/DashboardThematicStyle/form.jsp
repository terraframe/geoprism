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
  <mjl:dt attribute="bubbleContinuousSize">
    <mjl:boolean param="bubbleContinuousSize" />
  </mjl:dt>
  <mjl:dt attribute="bubbleFill">
    <mjl:input param="bubbleFill" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleMaxSize">
    <mjl:input param="bubbleMaxSize" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleMinSize">
    <mjl:input param="bubbleMinSize" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleOpacity">
    <mjl:input param="bubbleOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleRotation">
    <mjl:input param="bubbleRotation" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleSize">
    <mjl:input param="bubbleSize" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleStroke">
    <mjl:input param="bubbleStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleStrokeOpacity">
    <mjl:input param="bubbleStrokeOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleStrokeWidth">
    <mjl:input param="bubbleStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="bubbleWellKnownName">
    <mjl:input param="bubbleWellKnownName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPointFillOpacity">
    <mjl:input param="categoryPointFillOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPointSize">
    <mjl:input param="categoryPointSize" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPointStroke">
    <mjl:input param="categoryPointStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPointStrokeOpacity">
    <mjl:input param="categoryPointStrokeOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPointStrokeWidth">
    <mjl:input param="categoryPointStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPointStyles">
    <mjl:input param="categoryPointStyles" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPointWellKnownName">
    <mjl:input param="categoryPointWellKnownName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPolygonFillOpacity">
    <mjl:input param="categoryPolygonFillOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPolygonStroke">
    <mjl:input param="categoryPolygonStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPolygonStrokeOpacity">
    <mjl:input param="categoryPolygonStrokeOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPolygonStrokeWidth">
    <mjl:input param="categoryPolygonStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="categoryPolygonStyles">
    <mjl:input param="categoryPolygonStyles" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointFillOpacity">
    <mjl:input param="gradientPointFillOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointMaxFill">
    <mjl:input param="gradientPointMaxFill" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointMinFill">
    <mjl:input param="gradientPointMinFill" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointSize">
    <mjl:input param="gradientPointSize" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointStroke">
    <mjl:input param="gradientPointStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointStrokeOpacity">
    <mjl:input param="gradientPointStrokeOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointStrokeWidth">
    <mjl:input param="gradientPointStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPointWellKnownName">
    <mjl:input param="gradientPointWellKnownName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPolygonFillOpacity">
    <mjl:input param="gradientPolygonFillOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPolygonMaxFill">
    <mjl:input param="gradientPolygonMaxFill" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPolygonMinFill">
    <mjl:input param="gradientPolygonMinFill" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPolygonStroke">
    <mjl:input param="gradientPolygonStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPolygonStrokeOpacity">
    <mjl:input param="gradientPolygonStrokeOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gradientPolygonStrokeWidth">
    <mjl:input param="gradientPolygonStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="secondaryAggregationType">
    <mjl:select valueAttribute="enumName" param="secondaryAggregationType" var="current" items="${_secondaryAggregationType}">
      <mjl:option selected="${mjl:contains(item.secondaryAggregationTypeEnumNames, current.enumName) ? 'selected' : 'false'}">
        ${item.secondaryAggregationTypeMd.enumItems[current.enumName]}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="secondaryAttribute">
    <mjl:select valueAttribute="id" param="secondaryAttribute" var="current" items="${_secondaryAttribute}">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="secondaryCategories">
    <mjl:input param="secondaryCategories" type="text" />
  </mjl:dt>
  <mjl:dt attribute="basicPointSize">
    <mjl:input param="basicPointSize" type="text" />
  </mjl:dt>
  <mjl:dt attribute="enableLabel">
    <mjl:boolean param="enableLabel" />
  </mjl:dt>
  <mjl:dt attribute="enableValue">
    <mjl:boolean param="enableValue" />
  </mjl:dt>
  <mjl:dt attribute="labelColor">
    <mjl:input param="labelColor" type="text" />
  </mjl:dt>
  <mjl:dt attribute="labelFont">
    <mjl:input param="labelFont" type="text" />
  </mjl:dt>
  <mjl:dt attribute="labelHalo">
    <mjl:input param="labelHalo" type="text" />
  </mjl:dt>
  <mjl:dt attribute="labelHaloWidth">
    <mjl:input param="labelHaloWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="labelSize">
    <mjl:input param="labelSize" type="text" />
  </mjl:dt>
  <mjl:dt attribute="lineOpacity">
    <mjl:input param="lineOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="lineStroke">
    <mjl:input param="lineStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="lineStrokeCap">
    <mjl:input param="lineStrokeCap" type="text" />
  </mjl:dt>
  <mjl:dt attribute="lineStrokeWidth">
    <mjl:input param="lineStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="name">
    <mjl:input param="name" type="text" />
  </mjl:dt>
  <mjl:dt attribute="pointFill">
    <mjl:input param="pointFill" type="text" />
  </mjl:dt>
  <mjl:dt attribute="pointOpacity">
    <mjl:input param="pointOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="pointRotation">
    <mjl:input param="pointRotation" type="text" />
  </mjl:dt>
  <mjl:dt attribute="pointStroke">
    <mjl:input param="pointStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="pointStrokeOpacity">
    <mjl:input param="pointStrokeOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="pointStrokeWidth">
    <mjl:input param="pointStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="pointWellKnownName">
    <mjl:input param="pointWellKnownName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="polygonFill">
    <mjl:input param="polygonFill" type="text" />
  </mjl:dt>
  <mjl:dt attribute="polygonFillOpacity">
    <mjl:input param="polygonFillOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="polygonStroke">
    <mjl:input param="polygonStroke" type="text" />
  </mjl:dt>
  <mjl:dt attribute="polygonStrokeOpacity">
    <mjl:input param="polygonStrokeOpacity" type="text" />
  </mjl:dt>
  <mjl:dt attribute="polygonStrokeWidth">
    <mjl:input param="polygonStrokeWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="valueColor">
    <mjl:input param="valueColor" type="text" />
  </mjl:dt>
  <mjl:dt attribute="valueFont">
    <mjl:input param="valueFont" type="text" />
  </mjl:dt>
  <mjl:dt attribute="valueHalo">
    <mjl:input param="valueHalo" type="text" />
  </mjl:dt>
  <mjl:dt attribute="valueHaloWidth">
    <mjl:input param="valueHaloWidth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="valueSize">
    <mjl:input param="valueSize" type="text" />
  </mjl:dt>
</mjl:component>
