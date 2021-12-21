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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<gdb:localize var="page_title" key="universal.editTitle"/>
  <mjl:form classes="submit-form" id="com.runwaysdk.system.gis.geo.Universal.form.id" name="com.runwaysdk.system.gis.geo.Universal.form.name" method="POST">
    <%@include file="form.jsp" %>
    <!--
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.update.button" value="Update" action="com.runwaysdk.system.gis.geo.UniversalController.update.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.delete.button" value="Delete" action="com.runwaysdk.system.gis.geo.UniversalController.delete.mojo" />
    <mjl:command name="com.runwaysdk.system.gis.geo.Universal.form.cancel.button" value="Cancel" action="com.runwaysdk.system.gis.geo.UniversalController.cancel.mojo" />
    -->
  </mjl:form>
