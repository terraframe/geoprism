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
<c:set scope="request" var="page_title" value="View_ReportItem" />
<mjl:messages>
  <mjl:message />
</mjl:messages>
<dl>
  <mjl:form id="dss.vector.solutions.report.ReportItem.form.id" name="dss.vector.solutions.report.ReportItem.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="reportLabel">
        ${item.reportLabel}
      </mjl:dt>
      <mjl:dt attribute="dashboard">
        ${item.dashboard.displayLabel.value}
      </mjl:dt>
      <mjl:dt attribute="reportName">
        ${item.reportName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="dss.vector.solutions.report.ReportItem.form.edit.button" value="Edit" action="dss.vector.solutions.report.ReportItemController.edit.mojo" />
  </mjl:form>
</dl>
<mjl:commandLink name="dss.vector.solutions.report.ReportItem.viewAll.link" action="dss.vector.solutions.report.ReportItemController.viewAll.mojo">
  <gdb:localize key="View_All" />
</mjl:commandLink>
