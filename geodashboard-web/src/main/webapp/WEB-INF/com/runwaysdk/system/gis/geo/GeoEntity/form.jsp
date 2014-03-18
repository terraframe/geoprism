<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fieldset>
	<section class="form-container">
		<mjl:component param="dto" item="${item}">
		  <div class="field-row clearfix">
        <label for="select-field">${item.universalMd.displayLabel} <c:if test="${item.universalMd.required}">*</c:if></label>
		    <mjl:select id="select-field" param="universal" items="${_universal}" var="current" valueAttribute="id">
		      <mjl:option>
		        ${current.displayLabel.value}
		      </mjl:option>
		    </mjl:select>
		  </div>
		  <div class="field-row clearfix">
		    <label for="displayLabel">${item.displayLabelMd.displayLabel} <c:if test="${item.displayLabelMd.required}">*</c:if></label>
		    <mjl:input id="displayLabel" param="displayLabel" type="text" />
		    <mjl:messages attribute="displayLabel" classes="error-message" />
		  </div>
      <div class="field-row clearfix">
        <label for="geoId">${item.geoIdMd.displayLabel} <!--<c:if test="${item.geoIdMd.required}">*</c:if>--></label>
        <mjl:input id="geoId" param="geoId" type="text" />
        <mjl:messages attribute="geoId" classes="error-message" />
      </div>
		  <div class="field-row clearfix">
        <label for="GeoEntityField3">${item.wktMd.displayLabel} <c:if test="${item.wktMd.required}">*</c:if></label>
        <mjl:input id="GeoEntityField3" param="wkt" type="text" />
        <mjl:messages attribute="wkt" classes="error-message" />
      </div>
		</mjl:component>
	</section>
</fieldset>
