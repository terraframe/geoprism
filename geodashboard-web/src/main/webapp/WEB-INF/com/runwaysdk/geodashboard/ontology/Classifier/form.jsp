<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="classifierId">
    <mjl:input param="classifierId" type="text" />
  </mjl:dt>
  <mjl:dt attribute="classifierPackage">
    <mjl:input param="classifierPackage" type="text" />
  </mjl:dt>
  <mjl:dt attribute="displayLabel">
    <mjl:input param="displayLabel" type="text" />
  </mjl:dt>
</mjl:component>
