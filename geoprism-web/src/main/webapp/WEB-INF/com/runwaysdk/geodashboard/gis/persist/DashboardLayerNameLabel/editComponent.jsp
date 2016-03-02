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
<c:set var="page_title" scope="request" value="Edit an existing Name"/>
<dl>
  <mjl:form method="POST" name="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabel.form.name" id="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabel.form.id">
    <%@include file="form.jsp" %>
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabel.form.update.button" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabelController.update.mojo" value="Update" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabel.form.delete.button" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabelController.delete.mojo" value="Delete" />
    <mjl:command name="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabel.form.cancel.button" action="com.runwaysdk.geodashboard.gis.persist.DashboardLayerNameLabelController.cancel.mojo" value="Cancel" />
  </mjl:form>
</dl>
