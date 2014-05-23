<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="cacheDocument">
    <mjl:boolean param="cacheDocument" />
  </mjl:dt>
  <mjl:dt attribute="design">
    <mjl:input param="design" type="text" />
  </mjl:dt>
  <mjl:dt attribute="document">
    <mjl:input param="document" type="text" />
  </mjl:dt>
  <mjl:dt attribute="outputFormat">
    <mjl:select param="outputFormat" items="${_outputFormat}" var="current" valueAttribute="enumName">
      <mjl:option selected="${mjl:contains(item.outputFormatEnumNames, current.enumName) ? 'selected' : 'false'}">
        ${item.outputFormatMd.enumItems[current.enumName]}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="outputFormatIndex">
    <mjl:input param="outputFormatIndex" type="text" />
  </mjl:dt>
  <mjl:dt attribute="reportLabel">
    <mjl:input param="reportLabel" type="text" />
  </mjl:dt>
  <mjl:dt attribute="reportName">
    <mjl:input param="reportName" type="text" />
  </mjl:dt>
</mjl:component>
