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
  
  <form class="modal-form" #form="ngForm" novalidate (ngSubmit)="form.valid && onSubmit()">    
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="heading">
          <h1><localize key="category.management.optionTitle"></localize></h1>
        </div>
        <fieldset>
          <div class="row-holder">
            <div class="label-holder">
              <label><localize key="category.management.option.label"></localize></label>
            </div>    
            <div class="holder" >
              <span class="text">
                <input type="text" [(ngModel)]="category.label" name="label" required>
              </span>
            </div>
          </div>
          <div class="row-holder" *ngIf="category.synonyms.length > 0">
            <div class="label-holder">
              <label><localize key="category.management.synonyms"></localize></label>
            </div>    
            <div class="holder" >
              <table class="list-table table table-bordered table-striped">
                <tbody>
                  <tr *ngFor="let synonym of category.synonyms" class="fade-ngRepeat-item">
                    <td class="button-column">
                      <a class="fa fa-undo ico-edit" [title]="'category.management.restoreTooltip' | localize"
                         confirm-modal 
                         [message]="'category.management.restoreConfirm' | localize"
                         (onConfirm)="restore(synonym)"></a>                      
                    </td>
                    <td class="label-column">
                      {{synonym.label}}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="row-holder">
            <div class="label-holder">
            </div>
            <div class="holder" >
              <div class="box">
                <label><localize key="category.management.createSynonym"></localize></label>
                <div class="select-box">
                  <select class="method-select" [(ngModel)]="action.synonym" name="synonym">
                    <option value=""></option>
                    <option *ngFor="let option of category.siblings" [value]="option.id">{{option.label}}</option>
                  </select>
                </div>
              </div>          
            </div>
          </div>
          <div class="row-holder">
            <div class="label-holder">
            </div>  
            <div class="holder">
              <div class="button-holder">
                <input type="button" [value]="'dataset.cancel' | localize" class="btn btn-default" (click)="cancel()" />              
                <input type="submit" [value]="'dataset.submit' | localize" class="btn btn-primary" [disabled]="!form.valid" />
              </div>
            </div>
          </div>
        </fieldset>
      </div>
    </div>
  </form>
</div>
