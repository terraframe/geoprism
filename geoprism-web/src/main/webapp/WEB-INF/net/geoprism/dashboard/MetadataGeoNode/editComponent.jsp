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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="page_title" scope="request" value="Edit an existing Wrapper Geo node"/>
<dl>
  <mjl:form id="net.geoprism.dashboard.MetadataGeoNode.form.id" name="net.geoprism.dashboard.MetadataGeoNode.form.name" method="POST">
    <%@include file="form.jsp" %>
    <mjl:command name="net.geoprism.dashboard.MetadataGeoNode.form.update.button" value="Update" action="net.geoprism.dashboard.MetadataGeoNodeController.update.mojo" />
    <mjl:command name="net.geoprism.dashboard.MetadataGeoNode.form.delete.button" value="Delete" action="net.geoprism.dashboard.MetadataGeoNodeController.delete.mojo" />
    <mjl:command name="net.geoprism.dashboard.MetadataGeoNode.form.cancel.button" value="Cancel" action="net.geoprism.dashboard.MetadataGeoNodeController.cancel.mojo" />
  </mjl:form>
</dl>
