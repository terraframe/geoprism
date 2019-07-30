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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>

<gdb:localize var="page_title" key="useraccounts.title"/>

<!-- User account CSS -->
<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
<jwr:style src="/net/geoprism/userstable/UsersTable.css" useRandomParam="false"/>  


<!-- User account Javascript -->
<jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
<jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
<jwr:script src="/bundles/account.js" useRandomParam="false"/>
<script type="text/javascript">${js}</script>

</head>

<div id="usersTable"></div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  $(document).ready(function() {
  		var ut = new com.runwaysdk.ui.userstable.UsersTable({"bAutoWidth":false});  
  		ut.render("#usersTable");
  });
  
</script>
