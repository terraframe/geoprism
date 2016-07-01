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
  <jwr:script src="/bundles/category-icon.js" useRandomParam="false"/>

  <script type="text/javascript">${js}</script>
  
</head>


<div id="app-container" class="container" ng-app="category-icon" ng-controller="CategoryIconController as ctrl">

  <h2> <gdb:localize key="category.icon.title"/> </h2>
  
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
  
  <table id="manage-datasets-table" class="table table-bordered table-striped">        
    <tbody>
      <tr ng-repeat="icon in icons">
        <td class="button-column">
          <span>
            <a href="#" class="fa fa-trash-o ico-remove" ng-click="ctrl.remove(icon)" title="<gdb:localize key="category.icon.removeTooltip"/>"></a>           
            <a href="#" class="fa fa-pencil ico-edit" ng-click="ctrl.edit(icon)" title="<gdb:localize key="category.icon.editTooltip"/>"></a>                     
          </span>
        </td>
        <td>
        	{{ icon.label }}
        </td>
        <td>
<!--          <img style="margin-left:20px;width:42px;height:42px;" alt="{{ icon.label }}" src="{{icon.filePath}}" class="thumb"> -->
<!--            REMOVED:  onerror="if (this.src != 'net/geoprism/images/dashboard_icon_small.png') this.src = 'net/geoprism/images/dashboard_icon_small.png';" -->
          <img style="margin-left:20px;width:42px;height:42px;" class="thumb" ng-src="/iconimage/getCategoryIconImage?iconId={{ icon.id }}" alt="Icon">                  
        </td>
      </tr>      
    </tbody>    
  </table>
  
  <form class="modal-form" name="ctrl.form">
    <div class="modal-dialog">
      <div class="modal-content">
        <fieldset>
          <div class="row-holder">
            <div class="holder" >
              <label><gdb:localize key="category.icon.label"/></label>
              <span class="text">
                <input type="text" ng-model="icon.label" name="label" required>
              </span>
            </div>
          </div>
          <div class="row-holder">
            <div class="holder">
              <span class="text">
                <div class="drop-box-container" ng-show="!icon.file" accept="image/png" ngf-drag-over-class="'drop-active'" ngf-select="ctrl.setFile($files)" ngf-drop="ctrl.setFile($files)" ngf-multiple="false" ngf-drop-available="dropAvailable">
                  <div class="drop-box">
                    <div class="inner-drop-box">
                      <i class="fa fa-cloud-upload">
                        <p class="upload-text"><gdb:localize key="category.icon.uploadIcon"/></p>
                      </i>
                    </div>
                  </div>
                </div>
                <div ng-show="icon.file">
                  <a href="#" style="font-size:25px;vertical-align:middle;" class="fa fa-trash-o ico-remove" ng-click="icon.file = null" title="<gdb:localize key="category.icon.removeFile"/>"></a>           
                  <img style="width:42px;height:42px;margin-left:10px;" ngf-thumbnail="icon.file" class="thumb">
                  <span ng-show="ctrl.form.$error.file" style="float: right;">
                    <p class="error-message"><gdb:localize key="category.icon.badFileType"/></p>
                  </span>
                </div>				  
              </span>
            </div>
          </div>
          <div class="row-holder">
            <div class="holder">
              <div class="button-holder">
                <input type="button" value="<gdb:localize key="category.icon.ok"/>" class="btn btn-primary" ng-click="ctrl.create()" ng-disabled="ctrl.form.$invalid" />
              </div>
            </div>
          </div>
        </fieldset>  
      </div>
    </div>
  </form>
  
  <div ng-if="icons === null"><gdb:localize key='dataset.loadingData'/></div>
  
</div>

<script type="text/javascript">
  com.runwaysdk.ui.Manager.setFactory("JQuery");
</script>