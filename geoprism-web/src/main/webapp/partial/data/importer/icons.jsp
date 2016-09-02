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

<div id="app-container" class="container" >

  <div>
    <span>
    	<h2> <gdb:localize key="category.icon.title"/> </h2>
    </span>
  </div>
  
  <div ng-if="errors.length > 0 && !show" class="error-container">
    <div class="label-holder">
      <strong style="color: #8c0000;"><gdb:localize key='dashboard.errorsLabel'/></strong>
    </div>
    <div class="holder">
      <div ng-repeat="error in errors">
        <p class="error-message">{{error}}</p>
      </div>
    </div>
  </div>
  
  <div class="list-table-wrapper">
	  <table id="manage-icons-table" class="list-table table table-bordered table-striped">        
      <thead>
        <tr>
          <th></th>
          <th class="label-column"><gdb:localize key='category.icon.label'/></th>
          <th class="label-column"><gdb:localize key='category.icon.preview'/></th>
        </tr>
      </thead>	  
	    <tbody>
	      <tr ng-repeat="icon in icons" class="fade-ngRepeat-item">
	        <td class="button-column">
	          <span>
	            <a class="fa fa-pencil ico-edit" ng-click="ctrl.edit(icon)" title="<gdb:localize key="category.icon.editTooltip"/>"></a>                     
	            <a class="fa fa-trash-o ico-remove" ng-click="ctrl.remove(icon)" title="<gdb:localize key="category.icon.removeTooltip"/>"></a>           
	          </span>
	        </td>
	        <td class="label-column">{{ icon.label }}</td>
	        <td class="icon-thumbnail-column">
	<!--      REMOVED:  onerror="if (this.src != 'net/geoprism/images/dashboard_icon_small.png') this.src = 'net/geoprism/images/dashboard_icon_small.png';" -->
	          <img ng-if="icon.id && icon.id.length > 0" style="width:42px;height:42px;" class="thumb" ng-src="/iconimage/getCategoryIconImage?iconId={{ icon.id }}&{{ icon.timeStamp }}" alt="Icon">                  
	        </td>
	      </tr>      
        <tr>
          <td class="button-column">
            <a class="fa fa-plus" ng-click="ctrl.add()" title="<gdb:localize key="category.icon.addTooltip"/>"></a>
          </td>                 
          <td colspan="2">
          </td>                 
        </tr>	      
	    </tbody>    
	  </table>
  </div>
  
  <div ng-if="icons === null"><gdb:localize key='dataset.loadingData'/></div>
  <div ng-if="icons.length === 0">
    <p><gdb:localize key='category.icon.emtpy'/></p>
  </div>
  
  <div ng-show="show">
    <div class="modal-backdrop fade in"></div>
      <div style="display: block;" class="modal fade in" role="dialog" aria-hidden="false" data-backdrop="static" data-keyboard="false">
        <dl>      
		  <form class="modal-form" name="ctrl.form">
		    <div class="modal-dialog" ng-if="show">
		      <div class="modal-content" show-on-ready>
		             <div class="heading">
		                <h1 ng-if="editIcon"><gdb:localize key="category.icon.editHeader"/></h1>
		                <h1 ng-if="!editIcon"><gdb:localize key="category.icon.addHeader"/></h1>
		             </div>      
		        <fieldset>
		          <div class="row-holder" ng-show="errors.length > 0 && show">
		            <div class="label-holder">
		            </div>      
		            <div class="holder">
		              <div class="alert alertbox" ng-repeat="error in errors track by $index">
		                <p >{{error}}</p>
		              </div>
		            </div>
		          </div>            
		          <div class="row-holder">
		            <div class="label-holder">
		              <label><gdb:localize key="category.icon.label"/></label>
		            </div>          
		            <div class="holder" >
		              <span class="text">
		                <input type="text" ng-model="icon.label" name="label" required>
		              </span>
		            </div>
		          </div>
		          <div class="row-holder">
		            <div class="label-holder">
		            </div>          
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
		                  <a style="font-size:25px;vertical-align:middle;" class="fa fa-trash-o ico-remove" ng-click="icon.file = null" title="<gdb:localize key="category.icon.removeFile"/>"></a>           
		                  
		                  <!-- For display only when editing an icon-->
		                  <img ng-if="icon.id && icon.id.length > 0 && editIcon && icon.file.filePath" style="width:42px;height:42px;margin-left:10px;" ng-src="/iconimage/getCategoryIconImage?iconId={{ editIcon }}&''" class="thumb">
		                  
		                  <!-- Actual uploaded file preview -->
		                  <img style="width:42px;height:42px;margin-left:10px;" ngf-thumbnail="icon.file" class="thumb">
		                  
		                  <span ng-show="ctrl.form.$error.file" style="float: right;">
		                    <p class="error-message"><gdb:localize key="category.icon.badFileType"/></p>
		                  </span>
		                </div>          
		              </span>
		            </div>
		          </div>
		          <div class="row-holder" fire-on-ready>
		            <div class="label-holder">
		            </div>                    
		            <div class="holder">
		              <div class="button-holder">
		                <input type="button" value="<gdb:localize key="dataset.cancel"/>" class="btn btn-default" ng-click="ctrl.cancel()" />              
		                <input ng-show="!editIcon" type="button" value="<gdb:localize key="category.icon.ok"/>" class="btn btn-primary" ng-click="ctrl.create()" ng-disabled="ctrl.form.$invalid" />                
		                <input ng-show="editIcon" type="button" value="<gdb:localize key="category.icon.ok"/>" class="btn btn-primary" ng-click="ctrl.apply()" ng-disabled="ctrl.form.$invalid" />
		              </div>
		            </div>
		          </div>
		        </fieldset>  
		      </div>
		    </div>
		  </form>
      </dl>
    </div>
  </div> <!-- end modal wrapper -->
<!-- 
  <div class="row-holder" fire-on-ready>
    <div class="label-holder"></div>                    
    <div class="holder">
      <div class="button-holder">
        <button type="submit" class="btn btn-primary" ng-click="ctrl.add(icon)" >
        	<gdb:localize key="category.icon.addTooltip"/>
            &nbsp;
        	<i class="fa fa-plus"></i>
        </button>
      </div>
    </div>
  </div>
 -->  
  
</div>
