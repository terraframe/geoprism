<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fieldset>
  <section class="form-container">
    <mjl:component param="dto" item="${item}">
      <div class="field-row clearfix">
        <label for="select-field">${item.fileFormatMd.displayLabel} <c:if test="${item.fileFormatMd.required}">*</c:if></label>
        <mjl:select param="fileFormat" items="${_fileFormat}" var="current" valueAttribute="enumName">
          <mjl:option selected="${mjl:contains(item.fileFormatEnumNames, current.enumName) ? 'selected' : 'false'}">
            ${item.fileFormatMd.enumItems[current.enumName]}
          </mjl:option>
        </mjl:select>
        <mjl:messages attribute="fileFormat" classes="error-message" />
      </div>
    </mjl:component>
  </section>
</fieldset>
