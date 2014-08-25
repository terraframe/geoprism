<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="legendXPosition">
    <mjl:input param="legendXPosition" type="text" />
  </mjl:dt>
  <mjl:dt attribute="legendYPosition">
    <mjl:input param="legendYPosition" type="text" />
  </mjl:dt>
</mjl:component>
