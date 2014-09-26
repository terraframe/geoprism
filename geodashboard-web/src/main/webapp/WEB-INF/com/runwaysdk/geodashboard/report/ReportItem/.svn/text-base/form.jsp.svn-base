<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="mdss" uri="/WEB-INF/tlds/mdssLib.tld" %>
<mjl:component param="dto" item="${item}">
  <mjl:dt attribute="reportLabel">
    <mjl:input param="reportLabel" type="text" />
  </mjl:dt>
  <mjl:dt attribute="outputFormat">
    <mjl:select param="outputFormat" items="${outputFormat}" var="current" valueAttribute="enumName">
      <mjl:option selected="${mjl:contains(item.outputFormatEnumNames, current.enumName) ? 'selected' : 'false'}">
        ${item.outputFormatMd.enumItems[current.enumName]}
      </mjl:option>
    </mjl:select>
  </mjl:dt>
  <mjl:dt attribute="cacheDocument">
    <mjl:boolean param="cacheDocument"/>
  </mjl:dt>
  <mjl:dt attribute="reportName">
    ${item.reportName}
  </mjl:dt>  
</mjl:component>
<dt>
  <label>* ${item.designMd.displayLabel}</label>
</dt>
<dd>
  <mjl:input param="design" type="file" />
  <mjl:messages attribute="design">
    <mjl:message />
  </mjl:messages>  
</dd>
