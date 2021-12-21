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
<c:set var="page_title" scope="request" value="Edit an existing Name"/>
<dl>
  <mjl:form method="POST" name="net.geoprism.dashboard.layer.DashboardLayerNameLabel.form.name" id="net.geoprism.dashboard.layer.DashboardLayerNameLabel.form.id">
    <%@include file="form.jsp" %>
    <mjl:command name="net.geoprism.dashboard.layer.DashboardLayerNameLabel.form.update.button" action="net.geoprism.dashboard.layer.DashboardLayerNameLabelController.update.mojo" value="Update" />
    <mjl:command name="net.geoprism.dashboard.layer.DashboardLayerNameLabel.form.delete.button" action="net.geoprism.dashboard.layer.DashboardLayerNameLabelController.delete.mojo" value="Delete" />
    <mjl:command name="net.geoprism.dashboard.layer.DashboardLayerNameLabel.form.cancel.button" action="net.geoprism.dashboard.layer.DashboardLayerNameLabelController.cancel.mojo" value="Cancel" />
  </mjl:form>
</dl>
