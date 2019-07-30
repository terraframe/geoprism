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
<c:set var="page_title" scope="request" value="View all Equals"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="net.geoprism.dashboard.condition.DashboardNotEqualController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="comparisonValue">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="definingMdAttribute">
      <mjl:row>
        ${item.definingMdAttribute.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="parentCondition">
      <mjl:row>
        ${item.parentCondition.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="rootCondition">
      <mjl:row>
        ${item.rootCondition.keyName}
      </mjl:row>
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="net.geoprism.dashboard.condition.DashboardNotEqualController.view.mojo">
          View
          <mjl:property name="id" value="${item.id}" />
        </mjl:commandLink>
      </mjl:row>
      <mjl:footer>
        
      </mjl:footer>
    </mjl:freeColumn>
  </mjl:columns>
  <mjl:pagination>
    <mjl:page />
  </mjl:pagination>
</mjl:table>
<br />
<mjl:commandLink name="DashboardNotEqualController.newInstance" action="net.geoprism.dashboard.condition.DashboardNotEqualController.newInstance.mojo">
  Create a new Equals
</mjl:commandLink>
