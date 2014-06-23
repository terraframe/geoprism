<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="dateOfBirth">
    <mjl:input param="dateOfBirth" type="text" />
  </mjl:dt>
  <mjl:dt attribute="education">
    <mjl:select param="education" items="${_education}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="firstName">
    <mjl:input param="firstName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="gender">
    <mjl:select param="gender" items="${_gender}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="lastName">
    <mjl:input param="lastName" type="text" />
  </mjl:dt>
  <mjl:dt attribute="phoneNumber">
    <mjl:input param="phoneNumber" type="text" />
  </mjl:dt>
  <mjl:dt attribute="role">
    <mjl:select param="role" items="${_role}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="startDate">
    <mjl:input param="startDate" type="text" />
  </mjl:dt>
</mjl:component>
