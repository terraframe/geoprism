<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
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
    <mjl:select param="dashboardMap" items="${_dashboardMap}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="displayInLegend">
    <mjl:boolean param="displayInLegend" />
  </mjl:dt>
  <mjl:dt attribute="geoEntity">
    <mjl:select param="geoEntity" items="${_geoEntity}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="lastPublishDate">
    <mjl:input param="lastPublishDate" type="text" />
  </mjl:dt>
  <mjl:dt attribute="layerEnabled">
    <mjl:boolean param="layerEnabled" />
  </mjl:dt>
  <mjl:dt attribute="layerType">
    <mjl:select param="layerType" items="${_layerType}" var="current" valueAttribute="enumName">
      <mjl:option selected="${mjl:contains(item.layerTypeEnumNames, current.enumName) ? 'selected' : 'false'}">
        ${item.layerTypeMd.enumItems[current.enumName]}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="name">
    <mjl:input param="name" type="text" />
  </mjl:dt>
  <mjl:dt attribute="universal">
    <mjl:select param="universal" items="${_universal}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="viewName">
    <mjl:input param="viewName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="virtual">
    <mjl:boolean param="virtual" />
  </mjl:dt>
</mjl:component>
