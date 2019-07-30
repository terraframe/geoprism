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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>

  <gdb:localize var="page_title" key="dataset.title"/>

  <!-- Datasets Javascript -->
  <jwr:script src="/bundles/category-icon.js" useRandomParam="false"/>

  <script type="text/javascript">${js}</script>
  
</head>


<div ng-app="category-icon" ng-controller="CategoryIconController as ctrl">
  <ng-include src="'/partial/data/importer/icons.jsp'"></ng-include>
</div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
</script>