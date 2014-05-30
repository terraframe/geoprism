<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="wrappedMdAttribute">
    <mjl:select param="wrappedMdAttribute" items="${_wrappedMdAttribute}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
</mjl:component>
