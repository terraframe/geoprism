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
  
  <form #form="ngForm" novalidate class="modal-form" (ngSubmit)="form.valid && validName && onSubmit()">    
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1><localize key="category.management.editTooltip"></localize></h1>
        </div>
        <fieldset>
          <div class="row-holder">
            <div class="label-holder">
              <label><localize key="category.management.label"></localize></label>
            </div>    
            <div class="holder" >
              <span class="text">
                <input type="text" [(ngModel)]="category.label" [ngClass]="{ 'ng-invalid' : !validName }"  name="label" required (blur)="validateName($event.target.value)">
              </span>
              <div *ngIf="!validName" class="inline-error-message">
                <p>
                  <localize key="dataUploader.unique"></localize>
                </p>
              </div>         
            </div>
          </div>
          
          <div class="row-holder">
            <div class="label-holder">
              <label><localize key="category.management.descendants"></localize></label>
            </div>    
            <div class="holder" >
              <div class="list-table-wrapper">
                <table class="list-table table table-bordered table-striped">
                  <tbody>
                    <tr *ngFor="let descendant of category.descendants" class="fade-ngRepeat-item">
                      <td class="button-column">
                        <a class="fa fa-pencil ico-edit" (click)="edit(descendant)" [title]="'category.management.editTooltip' | localize"></a>
                        <a class="fa fa-trash-o ico-remove" [title]="'category.management.removeTooltip' | localize"
                           confirm-modal 
                           [message]="'category.management.removeOptionConfirm' | localize"
                           (onConfirm)="remove(descendant)"></a>                                   
                      </td>
                      <td class="label-column">{{descendant.label}}</td>
                    </tr>
                    <tr>
                      <td class="button-column">
                        <a class="fa fa-plus" *ngIf="!instance.active" (click)="newInstance()" [title]="'category.management.createCategoryOptionTooltip' | localize"></a>
                      </td>
                      <td class="submit-form" *ngIf="instance.active">
                        <form #childForm="ngForm" novalidate (ngSubmit)="childForm.valid && create()">
                          <input class="list-table-input" type="text" [(ngModel)]="instance.label" name="descendant-label" (keyup.esc)="cancel()" required />
                        </form>                      
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
                <input type="submit" [value]="'category.management.done' | localize" class="btn btn-primary" [disabled]="!(form.valid  && validName)" />
              </div>
            </div>
          </div>
        </fieldset>
      </div>
    </div>    
  </form>
</div>
