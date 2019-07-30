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

<article id="reporticng-container" class="reporticng-container report-panel-closed">
  <div ng-if="ctrl.canEdit() || hasReport" id="report-toolbar">
    <div id="report-toggle-container">
      <i ng-show="ctrl.state != 'min'" ng-click="ctrl.collapse()" id="report-collapse-toggle" class="fa fa-angle-double-down report-height-toggle"></i>
      <i ng-show="ctrl.state != 'max'" ng-click="ctrl.expand()" id="report-expand-toggle" class="fa fa-angle-double-up report-height-toggle"></i>
    </div>
      
    <span ng-if="ctrl.canEdit()">
      <a ng-click="ctrl.upload()" href="#" id="report-upload" title="<gdb:localize key='dashboardViewer.uploadReportTooltip'/>" ><gdb:localize key="dashboardViewer.upload"/></a>
      <a ng-show="hasReport" href="#" ng-click="ctrl.remove()" title="<gdb:localize key='dashboardViewer.removeReportTooltip'/>" ><gdb:localize key="dashboardViewer.remove"/></a>
      <a ng-show="hasReport" href="#" ng-click="ctrl.exportReport('rptdesign')" title="<gdb:localize key='dashboardViewer.exportReportTooltip'/>" ><gdb:localize key="report.rptdesign"/></a>
    </span>
     
    <span ng-show="hasReport" id="report-export-container">      
      <a href="#" ng-click="ctrl.exportReport('docx')" title="<gdb:localize key='dashboardViewer.exportReportTooltip'/>" ><gdb:localize key="report.docx"/></a>
      <a href="#" ng-click="ctrl.exportReport('xlsx')" title="<gdb:localize key='dashboardViewer.exportReportTooltip'/>" ><gdb:localize key="report.xlsx"/></a>
      <a href="#" ng-click="ctrl.exportReport('pdf')" title="<gdb:localize key='dashboardViewer.exportReportTooltip'/>" ><gdb:localize key="report.pdf"/></a>
    </span>
  </div>
  <div id="report-viewport">    
    <div id="report-content">
    </div>   
  </div>
</article>
