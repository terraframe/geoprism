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

<div>
  <error-message></error-message>  

  <form #form="ngForm" class="modal-form" (ngSubmit)="form.valid && onSubmit()">    
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1 *ngIf="icon.id != null"><gdb:localize key="category.icon.editHeader"/></h1>
          <h1 *ngIf="icon.id == null"><gdb:localize key="category.icon.addHeader"/></h1>
        </div>      
        <fieldset>
          <div class="row-holder">
            <div class="label-holder">
              <label><gdb:localize key="category.icon.label"/></label>
            </div>          
            <div class="holder" >
              <span class="text">
                <input type="text" [(ngModel)]="icon.label" name="label" required />
              </span>
            </div>
          </div>
          <div class="row-holder">
            <div class="label-holder">
            </div>          
            <div class="holder">
              <span class="text">
              
              
                <div *ngIf="!(file || icon.filePath)" class="drop-box-container" ng2FileDrop [ngClass]="{'drop-active': dropActive}" (fileOver)="fileOver($event)" [uploader]="uploader" (click)=fileInput.click()>
                  <div class="drop-box">
                    <div class="inner-drop-box">
                      <i class="fa fa-cloud-upload">
                        <p class="upload-text"><gdb:localize key="dashboardbuilder.uploadDataSet"/></p>
                      </i>
                    </div>
                  </div>
                  <input type="file" #uploadEl ng2FileSelect #fileInput [uploader]="uploader" id="uploader-input" style="display:none" />
                </div>
                
                <div *ngIf="file || icon.filePath">
                  <a style="font-size:25px;vertical-align:middle;" class="fa fa-trash-o ico-remove" (click)="clear()" title="<gdb:localize key="category.icon.removeFile"/>"></a>           
                      
                  <!-- For display only when editing an icon-->
                  <img *ngIf="!file" style="width:42px;height:42px;margin-left:10px;" [src]="'${pageContext.request.contextPath}/iconimage/getCategoryIconImage?id=' + icon.id" class="thumb">
                      
                  <!-- Actual uploaded file preview -->
                  <img *ngIf="file" style="width:42px;height:42px;margin-left:10px;" [src]="file" class="thumb">
                </div>          
              </span>
            </div>
          </div>
          
          <div class="row-holder">
            <div class="label-holder">
            </div>                    
            <div class="holder">
              <div class="button-holder">
                <input type="button" value="<gdb:localize key="dataset.cancel"/>" class="btn btn-default" (click)="cancel()" />              
                <input type="submit" value="<gdb:localize key="category.icon.ok"/>" class="btn btn-primary" (disabled)="form.invalid" />                
              </div>
            </div>
          </div>
        </fieldset>  
      </div>
    </div>
  </form>
</div>