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
<c:set var="page_title" scope="request" value="View all "/>
<mjl:table var="item" query="${query}">
  <mjl:context action="net.geoprism.EmailSettingController.viewPage.mojo" />
  <mjl:columns>
    <mjl:attributeColumn attributeName="from">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="password">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="port">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="server">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="to">
    </mjl:attributeColumn>
    <mjl:attributeColumn attributeName="username">
    </mjl:attributeColumn>
    <mjl:freeColumn>
      <mjl:header>
        
      </mjl:header>
      <mjl:row>
        <mjl:commandLink name="view.link" action="net.geoprism.EmailSettingController.view.mojo">
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
<mjl:commandLink name="EmailSettingController.newInstance" action="net.geoprism.EmailSettingController.newInstance.mojo">
  Create a new 
</mjl:commandLink>
