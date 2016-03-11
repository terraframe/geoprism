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

<gdb:localize var="page_title" key="universal.viewAllTitle"/>
<mjl:table var="item" query="${query}">
  <mjl:context action="com.runwaysdk.system.gis.geo.UniversalController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="description">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="displayLabel">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="name">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="com.runwaysdk.system.gis.geo.UniversalController.view.mojo">
          <gdb:localize key="view"/>
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
<mjl:commandLink name="UniversalController.newInstance" action="com.runwaysdk.system.gis.geo.UniversalController.newInstance.mojo">
  <gdb:localize key="universal.createLink"/>
</mjl:commandLink>
