<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fieldset>
  <section class="form-container">
		<mjl:component param="dto" item="${item}">
		  <div class="field-row clearfix">
		    <label for="displayLabel">${item.displayLabelMd.displayLabel} <c:if test="${item.displayLabelMd.required}">*</c:if></label>
		    <mjl:input id="displayLabel" param="displayLabel" type="text" />
		    <mjl:messages attribute="displayLabel" classes="error-message" />
		  </div>
		  <div class="field-row clearfix">
        <label for="name">${item.nameMd.displayLabel} <!--<c:if test="${item.nameMd.required}">*</c:if>--></label>
        <mjl:input id="name" param="name" type="text" />
        <mjl:messages attribute="name" classes="error-message" />
      </div>
		  <div class="field-row clearfix">
		    <label for="description">${item.descriptionMd.description} <c:if test="${item.descriptionMd.required}">*</c:if></label>
		    <mjl:input id="description" param="description" type="text" />
		    <mjl:messages attribute="description" classes="error-message" />
		  </div>
		</mjl:component>
	</section>
</fieldset>
