<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="comparisonValue">
    <mjl:input param="comparisonValue" type="text" />
  </mjl:dt>
  <mjl:dt attribute="definingMdAttribute">
    <mjl:select param="definingMdAttribute" items="${_definingMdAttribute}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="parentCondition">
    <mjl:select param="parentCondition" items="${_parentCondition}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="rootCondition">
    <mjl:select param="rootCondition" items="${_rootCondition}" var="current" valueAttribute="id">
      <mjl:option>
        ${current.keyName}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
</mjl:component>
