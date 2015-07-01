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
<c:set var="page_title" scope="request" value="View a Attribute Wrapper"/>
<dl>
  <mjl:form id="com.runwaysdk.geodashboard.AttributeWrapper.form.id" name="com.runwaysdk.geodashboard.AttributeWrapper.form.name" method="POST">
    <mjl:input param="id" value="${item.id}" type="hidden" />
    <mjl:component param="dto" item="${item}">
      <mjl:dt attribute="wrappedMdAttribute">
        ${item.wrappedMdAttribute.keyName}
      </mjl:dt>
    </mjl:component>
    <mjl:command name="com.runwaysdk.geodashboard.AttributeWrapper.form.edit.button" value="Edit" action="com.runwaysdk.geodashboard.AttributeWrapperController.edit.mojo" />
  </mjl:form>
</dl>
<dl>
  <dt>
    <label>
      Child Relationships
    </label>
  </dt>
  <dd>
    <ul>
      <li>
        <mjl:commandLink name="com.runwaysdk.geodashboard.DashboardAttributes.childQuery.link" action="com.runwaysdk.geodashboard.DashboardAttributesController.childQuery.mojo">
          
          <mjl:property name="childId" value="${item.id}" />
        </mjl:commandLink>
      </li>
    </ul>
  </dd>
</dl>
<mjl:commandLink name="com.runwaysdk.geodashboard.AttributeWrapper.viewAll.link" action="com.runwaysdk.geodashboard.AttributeWrapperController.viewAll.mojo">
  View All
</mjl:commandLink>
