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

  <form #form="ngForm" class="modal-form" (ngSubmit)="form.valid && validName && onSubmit()">    
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1><gdb:localize key="dataset.editTooltip"/></h1>
        </div>      
        <fieldset>            
          <div class="row-holder">
            <div class="label-holder">
              <label><gdb:localize key="dataset.label"/></label>
            </div>          
            <div class="holder" >
              <span class="text">
                <input type="text" [ngClass]="{ 'ng-invalid' : !validName }" [(ngModel)]="dataset.label" name="label" required (blur)="validateName($event.target.value)">
              </span>
              <div *ngIf="!validName" class="inline-error-message">
                <gdb:localize key="dataUploader.unique"/>
              </div>              
            </div>
          </div>
          <div class="row-holder">
            <div class="label-holder">
              <label><gdb:localize key="dataset.source"/></label>
            </div>          
            <div class="holder" >
              <span class="text">                  
                <textarea [(ngModel)]="dataset.source" name="source"></textarea>
              </span>
            </div>
          </div>
          <div class="row-holder">
            <div class="label-holder">
              <label><gdb:localize key="dataset.description"/></label>
            </div>          
            <div class="holder" >
              <span class="text">                  
                <textarea [(ngModel)]="dataset.description" name="description"></textarea>
              </span>
            </div>
          </div>
          <div class="row-holder">
            <div class="label-holder">
              <label><gdb:localize key="dataset.attributes"/></label>
            </div>          
            <div class="holder" >
              <table class="list-table table table-bordered table-striped">
                <tbody>
                  <tr *ngFor="let attribute of dataset.attributes" class="fade-ngRepeat-item">
                    <td class="submit-form">
                      <dl>
                        <dd>
                          <input type="text" name="{{attribute.label}}" [(ngModel)]="attribute.label" required>
                        </dd>
                        <dd *ngIf="attribute.type == 'Category'">
                          <gdb:localize key="dataset.category"/> <a (click)="open(attribute.root, $event)" title="<gdb:localize key="category.management.editThisCategoryTooltip"/>">{{attribute.root.label}}</a>
                        </dd>
                      </dl>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>      
          <div class="row-holder">
            <div class="label-holder">
            </div>                    
            <div class="holder">
              <div class="button-holder">
                <input type="button" value="<gdb:localize key="dataset.cancel"/>" class="btn btn-default" (click)="cancel()" />              
                <input type="submit" value="<gdb:localize key="dataset.submit"/>" class="btn btn-primary" [disabled]="!(form.valid  && validName)" />
              </div>
            </div>
          </div>
        </fieldset>
      </div>
    </div>
  </form>
</div>
