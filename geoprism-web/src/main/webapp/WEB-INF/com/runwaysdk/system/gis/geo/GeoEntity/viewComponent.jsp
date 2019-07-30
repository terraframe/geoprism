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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<gdb:localize var="page_title" key="geoEntity.viewTitle"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.GeoEntity.form.id" name="com.runwaysdk.system.gis.geo.GeoEntity.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="entityName">
        ${item.entityName}
      </mjl:dt>
      <mjl:dt attribute="geoId">
        ${item.geoId}
      </mjl:dt>
      <mjl:dt attribute="geoMultiPolygon">
        ${item.geoMultiPolygon}
      </mjl:dt>
      <mjl:dt attribute="geoPoint">
        ${item.geoPoint}
      </mjl:dt>
      <mjl:dt attribute="universal">
        ${item.universal.keyName}
      </mjl:dt>
      <mjl:dt attribute="wkt">
        ${item.wkt}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.system.gis.geo.GeoEntity.form.edit.button" value="Edit" action="com.runwaysdk.system.gis.geo.GeoEntityController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
</dl>
<mjl:commandLink name="com.runwaysdk.system.gis.geo.GeoEntity.viewAll.link" action="com.runwaysdk.system.gis.geo.GeoEntityController.viewAll.mojo">
  <gdb:localize key="viewAll"/>
</mjl:commandLink>
