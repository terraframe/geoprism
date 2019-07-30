<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

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

<mjl:form id="net.geoprism.data.importer.Excel.form.id" name="form.name" classes="submit-form" method="POST" enctype="multipart/form-data">
  <fieldset class="net-geoprism-FormList">
    <section class="form-container">
      <div class="field-row clearfix">
        <label for="country">* <gdb:localize key="excel.country"/> </label>
        <select id="country" name="country">
          <c:forEach items="${countries}" var="country">
            <option value="${country.value}">${country.label}</option>            
          </c:forEach>
        </select>
        <div id="design-error" class="error-message"></div>
      </div>      
    </section>
  </fieldset>
</mjl:form>
