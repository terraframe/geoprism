<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="active">
    <mjl:boolean param="active" />
  </mjl:dt>
  <mjl:dt attribute="agriculture">
    <mjl:boolean param="agriculture" />
  </mjl:dt>
  <mjl:dt attribute="name">
    <mjl:input param="name" type="text" />
  </mjl:dt>
  <mjl:dt attribute="phoneNumber">
    <mjl:input param="phoneNumber" type="text" />
  </mjl:dt>
  <mjl:dt attribute="workingArea">
    <mjl:select param="workingArea" items="${_workingArea}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="workingStation">
    <mjl:select param="workingStation" items="${_workingStation}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
</mjl:component>
