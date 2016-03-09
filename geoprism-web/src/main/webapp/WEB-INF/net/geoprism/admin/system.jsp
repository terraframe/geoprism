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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>

<head>

<gdb:localize var="page_title" key="system.title"/>

<!-- User account CSS -->
<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
<jwr:style src="/net/geoprism/userstable/UsersTable.css" useRandomParam="false"/>  

<!-- User account Javascript -->
<jwr:script src="/bundles/system.js" useRandomParam="false"/>
<script type="text/javascript">${js}</script>


</head>

<body>

<div id="systemForm"></div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  var emailSetting = new net.geoprism.EmailSetting(${emailSetting}.returnValue[0]);
  var user = new net.geoprism.GeoprismUser(${user}.returnValue[0]);
  
  var sf = new net.geoprism.system.SystemForm(emailSetting, user);  
  sf.render("#systemForm");
</script>

</body>
