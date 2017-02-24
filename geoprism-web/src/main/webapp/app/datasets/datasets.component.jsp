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

<div id="app-container" class="container">

  <error-message ></error-message>

  <h2> <localize key="dataset.title"></localize> </h2>
  
  <div *ngIf="datasets === null">
    <localize key='dataset.loadingData'></localize>
  </div>
  <div class="list-table-wrapper">
	  <table id="manage-datasets-table" class="list-table table table-bordered table-striped">        
	    <thead>
	      <tr>
	        <th></th>
	        <th class="label-column"><localize key='dataset.label'></localize></th>
	        <th class="label-column"><localize key='dataset.description'></localize></th>
	      </tr>
	    </thead>
	    <tbody>
	      <tr *ngFor="let dataset of datasets" class="fade-ngRepeat-item">
	        <td class="button-column">
	          <a class="fa fa-pencil ico-edit" (click)="edit(dataset, $event)" [title]="'dataset.editTooltip' | localize"></a>                             
	          <a class="fa fa-trash-o ico-remove" [title]="'dataset.removeTooltip' | localize"
	             confirm-modal 
	             [message]="'dataset.removeContent' | localize"
	             (onConfirm)="remove(dataset, $event)"></a>           
	        </td>
	        <td class="label-column"> {{ dataset.label }} </td>
	        <td class="description-column"> {{dataset.description}} </td>
	      </tr>
	    </tbody>    
	  </table>
   </div>
   
  <div class="drop-box-container" ng2FileDrop [ngClass]="{'drop-active': dropActive}" (fileOver)="fileOver($event)" [uploader]="uploader" (click)=fileInput.click()>
    <div class="drop-box">
      <div class="inner-drop-box">
        <i class="fa fa-cloud-upload">
          <p class="upload-text"><localize key="dashboardbuilder.uploadDataSet"></localize></p>
        </i>
      </div>
    </div>
    <input type="file" #uploadEl ng2FileSelect #fileInput [uploader]="uploader" id="uploader-input" style="display:none" />
  </div>
  
  <upload-wizard (onSuccess)="onSuccess($event)"></upload-wizard>
</div>
