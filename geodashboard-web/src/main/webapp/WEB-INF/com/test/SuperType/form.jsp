<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="testCharacter">
    <mjl:input param="testCharacter" type="text" />
  </mjl:dt>
</mjl:component>
