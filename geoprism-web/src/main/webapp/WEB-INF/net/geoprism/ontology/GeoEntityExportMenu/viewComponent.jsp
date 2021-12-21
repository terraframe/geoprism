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
<c:set var="page_title" scope="request" value="View a Export GeoEntities"/>
<dl>
  <mjl:form id="net.geoprism.ontology.GeoEntityExportMenuController.form.id" name="net.geoprism.ontology.GeoEntityExportMenu.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="fileFormat">
        <ul>
          <c:forEach items="${item.fileFormatEnumNames}" var="enumName">
            <li>
              ${item.fileFormatMd.enumItems[enumName]}
            </li>
          </c:forEach>
        </ul>
      </mjl:dt>
      <mjl:dt attribute="includeGIS">
        ${item.includeGIS ? item.includeGISMd.positiveDisplayLabel : item.includeGISMd.negativeDisplayLabel}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="net.geoprism.ontology.GeoEntityExportMenu.form.edit.button" value="Edit" action="net.geoprism.ontology.GeoEntityExportMenuController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="net.geoprism.ontology.GeoEntityExportMenu.viewAll.link" action="net.geoprism.ontology.GeoEntityExportMenuController.viewAll.mojo">
</mjl:commandLink>
