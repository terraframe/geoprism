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
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>

<head>

  <gdb:localize var="page_title" key="dataset.title"/>

  <!-- Datasets Javascript -->
  <jwr:script src="/bundles/datasets.js" useRandomParam="false"/>

  <script type="text/javascript">${js}</script>
  
</head>


<div id="app-container" class="container" ng-app="data-set" ng-controller="DatasetController as ctrl">

  <h2> <gdb:localize key="dataset.title"/> </h2>
  
  <div ng-if="errors.length > 0" class="error-container">
    <div class="label-holder">
      <strong style="color: #8c0000;"><gdb:localize key='dashboard.errorsLabel'/></strong>
    </div>
    <div class="holder">
      <div ng-repeat="error in errors">
        <p class="error-message">{{error}}</p>
      </div>
    </div>
  </div>

  <table class="table table-bordered table-striped">    
    <thead>
      <tr>
        <td>
        </td>      
        <td>
          <gdb:localize key="dataset.label"/>
        </td>
      </tr>
    </thead>
    
    <tbody>
      <tr ng-repeat="dataset in datasets">
        <td>
          <a href="#" class="fa fa-times ico-remove" ng-click="ctrl.remove(dataset)" title="<gdb:localize key="dataset.removeTooltip"/>"></a>           
        </td>
        <td>{{ dataset.label }}</td>
      </tr>
    </tbody>    
  </table>
  
  <div class="drop-box-container" ngf-drag-over-class="'drop-active'" ngf-select="ctrl.uploadFile($files)" ngf-drop="ctrl.uploadFile($files)" ngf-multiple="false" ngf-drop-available="dropAvailable" fire-on-ready>
    <div class="drop-box">
      <div class="inner-drop-box">
        <i class="fa fa-cloud-upload">
          <p class="upload-text"><gdb:localize key="dashboardbuilder.uploadDataSet"/></p>
        </i>
      </div>
    </div>
  </div>
  
  <uploader-dialog></uploader-dialog>  
</div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
</script>