<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<fieldset>
	<section class="form-container">
		<mjl:component param="dto" item="${item}">
		  <div class="field-row clearfix">
        <label for="select-field">${item.universalMd.displayLabel}</label>
		    <mjl:select id="select-field" param="universal" items="${_universal}" var="current" valueAttribute="id">
		      <mjl:option>
		        ${current.displayLabel.value}
		      </mjl:option>
		    </mjl:select>
		  </div>
		  
		  <div class="field-row clearfix">
		    <label for="GeoEntityField2">${item.displayLabelMd.displayLabel}</label>
		    <mjl:input id="GeoEntityField2" param="displayLabel" type="text" />
		  </div>
		  <div class="field-row clearfix">
        <label for="GeoEntityField3">${item.wktMd.displayLabel}</label>
        <mjl:input id="GeoEntityField3" param="wkt" type="text" />
      </div>
		</mjl:component>
	</section>
</fieldset>
