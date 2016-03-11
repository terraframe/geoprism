<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

    This file is part of Runway SDK(tm).

    Runway SDK(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Runway SDK(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<gdb:localize var="page_title" key="geoEntity.viewTitle"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.Universal.form.id" name="com.runwaysdk.system.gis.geo.Universal.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="description">
        ${item.description}
      </mjl:dt>
      <mjl:dt attribute="displayLabel">
        ${item.displayLabel}
      </mjl:dt>
      <mjl:dt attribute="name">
        ${item.name}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.edit.button" value="Edit" action="com.runwaysdk.system.gis.geo.UniversalController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
</dl>
<mjl:commandLink name="com.runwaysdk.system.gis.geo.Universal.viewAll.link" action="com.runwaysdk.system.gis.geo.UniversalController.viewAll.mojo">
  <gdb:localize key="viewAll"/>
</mjl:commandLink>
