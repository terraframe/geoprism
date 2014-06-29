<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="name">
    <mjl:input param="name" type="text" />
  </mjl:dt>
  <mjl:dt attribute="project">
    <mjl:select param="project" items="${_project}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="url">
    <mjl:input param="url" type="text" />
  </mjl:dt>
</mjl:component>
