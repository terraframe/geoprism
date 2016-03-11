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

<head>

<gdb:localize var="page_title" key="reports.title"/>

<!-- Report CSS -->
<jwr:style src="/bundles/datatable.css" useRandomParam="false"/>  
<jwr:style src="/net/geoprism/report/ReportTable.css" useRandomParam="false"/>  

<!-- Report Javascript -->
<jwr:script src="/bundles/datatablejquery.js" useRandomParam="false"/>
<jwr:script src="/bundles/datatable.js" useRandomParam="false"/>
<jwr:script src="/bundles/runway-controller.js" useRandomParam="false"/>

<!-- 
<jwr:script src="/bundles/report.js" useRandomParam="false"/>

<jwr:script src="/net/geoprism/report/ReportItem.js" useRandomParam="false"/>
<jwr:script src="/net/geoprism/report/ReportItemView.js" useRandomParam="false"/>
 -->


</head>

<div id="reportTable"></div>

<!-- 
<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
  
  $(document).ready(function() {
  		var ut = new net.geoprism.report.ReportTable({"bAutoWidth":false});  
  		ut.render("#reportTable");
  });  
</script>
 -->
