<%--

    Copyright (c) 2022 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
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
        <label for="classifierId">${item.classifierIdMd.displayLabel}</label>
        <mjl:input id="classifierId" param="classifierId" type="text" />
        <mjl:messages attribute="classifierId" classes="error-message" />
      </div>
      <div class="field-row clearfix">
        <label for="classifierPackage">${item.classifierPackageMd.displayLabel}</label>
        <mjl:input id="classifierPackage" param="classifierPackage" type="text" />
        <mjl:messages attribute="classifierPackage" classes="error-message" />
      </div>
    </mjl:component>
  </section>
</fieldset>
