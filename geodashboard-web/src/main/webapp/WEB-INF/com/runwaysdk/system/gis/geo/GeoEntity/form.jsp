<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="universal">
    <mjl:select param="universal" items="${_universal}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="displayLabel">
    <mjl:input param="displayLabel" type="text" />
  </mjl:dt>
  <mjl:dt attribute="entityName">
    <mjl:input param="entityName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="geoMultiPolygon">
    <mjl:input param="geoMultiPolygon" type="text" />
  </mjl:dt>
  <mjl:dt attribute="geoPoint">
    <mjl:input param="geoPoint" type="text" />
  </mjl:dt>
  <mjl:dt attribute="wkt">
    <mjl:input param="wkt" type="text" />
  </mjl:dt>
</mjl:component>
