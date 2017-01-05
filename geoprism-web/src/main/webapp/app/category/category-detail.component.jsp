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

  <error-message [error]="error"></error-message>
  
  <form #form="ngForm" novalidate class="modal-form" (ngSubmit)="form.valid && onSubmit()">    
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1><gdb:localize key="category.management.editTooltip"/></h1>
        </div>
        <fieldset>
          <div class="row-holder">
            <div class="label-holder">
              <label><gdb:localize key="category.management.label"/></label>
            </div>    
            <div class="holder" >
              <span class="text">
                <input type="text" [(ngModel)]="category.label" name="label" required>
              </span>
              <div class="inline-error-message">
<!-- 
                <p *ngIf="!form.controls.label.valid">
                  <gdb:localize key="dataUploader.unique"/>
                </p>
 -->              
              </div>         
            </div>
          </div>
          
          <div class="row-holder">
            <div class="label-holder">
              <label><gdb:localize key="category.management.descendants"/></label>
            </div>    
            <div class="holder" >
              <div class="list-table-wrapper">
                <table class="list-table table table-bordered table-striped">
                  <tbody>
                    <tr *ngFor="let descendant of category.descendants" class="fade-ngRepeat-item">
                      <td class="button-column">
                        <a class="fa fa-pencil ico-edit" ng-click="edit(descendant)" title="<gdb:localize key="category.management.editTooltip"/>"></a>
                        <a class="fa fa-trash-o ico-remove" ng-click="remove(descendant)" title="<gdb:localize key="category.management.removeTooltip"/>"></a>                               
                      </td>
                      <td class="label-column">{{descendant.label}}</td>
                    </tr>
                    <tr>
                      <td class="button-column">
                        <a class="fa fa-plus" *ngIf="!instance.active" (click)="newInstance()" title="<gdb:localize key="category.management.createCategoryOptionTooltip"/>"></a>
                      </td>
                      <td class="submit-form" *ngIf="instance.active">
                        <input class="list-table-input" type="text" [(ngModel)]="instance.label" (keyup)="onKey($event)" />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
                 
          <div class="row-holder">
            <div class="label-holder">
            </div>  
            <div class="holder">
              <div class="button-holder">
                <input type="submit" value="<gdb:localize key="category.management.done"/>" class="btn btn-primary" [disabled]="!form.valid" />
              </div>
            </div>
          </div>
        </fieldset>
      </div>
    </div>    
  </form>
</div>
