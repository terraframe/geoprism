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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<mjl:form id="form.id" name="form.name" classes="submit-form" method="POST" enctype="multipart/form-data">
	<fieldset class="net-geoprism-FormList">
	  <section class="form-container">
	      <c:if test="${not empty bannerFilePath}" >
	        <div class="field-row clearfix">
	          <label for="preview"><gdb:localize key="net.geoprism.system.SystemForm.logo.bannerUploadPreviewLabel" /></label>
	          <img id="preview" src="${bannerFilePath}" alt="Uploaded Banner" />
	          <div id="preview-error" class="error-message"></div>
	        </div>
	      </c:if>
	      <c:if test="${not empty bannerFileName}" >
          <div class="field-row clearfix">
            <label for="bannerNameLabel"><gdb:localize key="net.geoprism.system.SystemForm.logo.bannerFileNameLabel" /></label>
            <label id="bannerName">${bannerFileName}</label>
            <div id="bannerName-error" class="error-message"></div>
          </div>
        </c:if>
	    <div class="field-row clearfix">
	      <label for="bannerImage">* <gdb:localize key="net.geoprism.system.SystemForm.logo.bannerImageLabel" /></label>
	      <mjl:input param="design" type="file" id="bannerImage" />
	      <div id="bannerImage-error" class="error-message"></div>
	    </div>      
	  </section>
	</fieldset>
</mjl:form>
