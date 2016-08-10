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

<div id="app-container" class="container">

  <h2> <gdb:localize key="dataset.title"/> </h2>
  
  <div ng-if="errors.length > 0" class="error-container" ng-cloak>
    <div class="label-holder">
      <strong style="color: #8c0000;"><gdb:localize key='dashboard.errorsLabel'/></strong>
    </div>
    <div class="holder">
      <div ng-repeat="error in errors" >
        <p class="error-message">{{error}}</p>
      </div>
    </div>
  </div>
  
  <div ng-if="datasets === null"><gdb:localize key='dataset.loadingData'/></div>

  <table id="manage-datasets-table" class="table table-bordered table-striped">        
    <tbody>
      <tr ng-repeat="dataset in datasets" ng-cloak>
        <td class="button-column">
          <a class="fa fa-trash-o ico-remove" ng-click="ctrl.remove(dataset)" title="<gdb:localize key="dataset.removeTooltip"/>"></a>           
        </td>
        <td class="submit-form">
          <input class="dataset-list-input" type="text" name="datasetListInput.{{$index}}" ng-model="dataset.label" value="{{ dataset.label }}" ng-attr-title="{{ datasetListInputTitle }}" ng-mouseover="ctrl.datasetElementHover($event)" ng-click="ctrl.setDatasetState(dataset)" ng-readonly="!dataset.editMode">
            <i class="fa fa-pencil ico-edit" ng-show="!dataset.editMode"></i>
          </input>
          <button type="button" class="btn btn-primary btn" role="button" aria-disabled="false" ng-show="dataset.editMode" ng-click="ctrl.apply(dataset)"><gdb:localize key="dataset.submit"/></button>
          <button type="button" class="btn btn-default" role="button" aria-disabled="false" ng-show="dataset.editMode" ng-click="ctrl.cancelDatasetEdit(dataset)"><gdb:localize key="dataset.cancel"/></button>
        </td>
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
