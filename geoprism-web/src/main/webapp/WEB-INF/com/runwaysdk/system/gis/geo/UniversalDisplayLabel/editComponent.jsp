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
<c:set var="page_title" scope="request" value="Edit an existing Display label"/>
<dl>
  <mjl:form id="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.id" name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.UniversalDisplayLabelController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.UniversalDisplayLabelController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.UniversalDisplayLabel.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.UniversalDisplayLabelController.cancel.mojo" />
  </mjl:form>
</dl>
