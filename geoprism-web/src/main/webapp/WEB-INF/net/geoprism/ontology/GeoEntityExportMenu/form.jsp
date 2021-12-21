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
        <label for="fileFormat">${item.fileFormatMd.displayLabel} <c:if test="${item.fileFormatMd.required}">*</c:if></label>
        <mjl:select param="fileFormat" items="${_fileFormat}" var="current" valueAttribute="enumName">
          <mjl:option selected="${mjl:contains(item.fileFormatEnumNames, current.enumName) ? 'selected' : 'false'}">
            ${item.fileFormatMd.enumItems[current.enumName]}
          </mjl:option>
        </mjl:select>
        <mjl:messages attribute="fileFormat" classes="error-message" />
      </div>
      <%-- <div class="field-row clearfix">
        <label for="inclueGIS">${item.includeGISMd.displayLabel} <c:if test="${item.includeGISMd.required}">*</c:if></label>
			  <mjl:boolean id="includeGIS" param="includeGIS" />
			  <mjl:messages attribute="includeGIS" classes="error-message" />
		  </div> --%>
    </mjl:component>
  </section>
</fieldset>
