<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="amount">
    <mjl:input param="amount" type="text" />
  </mjl:dt>
  <mjl:dt attribute="endPeriod">
    <mjl:input param="endPeriod" type="text" />
  </mjl:dt>
  <mjl:dt attribute="geoLocation">
    <mjl:select param="geoLocation" items="${_geoLocation}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="name">
    <mjl:input param="name" type="text" />
  </mjl:dt>
  <mjl:dt attribute="startPeriod">
    <mjl:input param="startPeriod" type="text" />
  </mjl:dt>
</mjl:component>
