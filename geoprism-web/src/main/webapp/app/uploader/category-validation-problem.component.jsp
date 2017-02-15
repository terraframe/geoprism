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

<div class="row-holder">    
  <div>
    <form #problemForm="ngForm">
      <div class="inline-value">{{problem.attributeLabel}}</div>    
      <div class="inline-value error-message">{{problem.label}}</div>
      <div *ngIf="!problem.resolved">      
        <div class="inline-combo">
          <select class="select-area" [(ngModel)]="problem.synonym" (change)="setSynonym()" name="synonym">
            <option value=""></option>          
            <option *ngFor="let opt of options" [value]="opt.id">{{opt.label}}</option>
          </select>
        </div>
        <div class="inline-actions">
          <i aria-hidden="true" data-icon="&#xe900;" class="icon-synonym_icon" (ngClass)="{disabled: !hasSynonym}" (click)="createSynonym()" title="<gdb:localize key="dataUploader.createSynonymCategoryTooltip"/>" ></i>
          <i aria-hidden="true" data-icon="&#xe901;" class="icon-new_location_icon" (click)="createOption()" title="<gdb:localize key="dataUploader.createNewOptionTooltip"/>" ></i>          
          <span class="fa-stack fa-lg" title="<gdb:localize key="dataUploader.ignoreCategoryTooltip"/>" (click)="ignoreValue()">
            <i class="fa fa-square fa-stack-2x"></i>
            <i class="fa fa-times fa-stack-1x"></i>
          </span>
        </div>
      </div>
      <div *ngIf="problem.resolved">
        <div class="inline-combo" *ngIf="problem.action.name == 'OPTION'">
          <gdb:localize key="dataUploader.resolvedCategoryOption"/>
        </div>            
        <div class="inline-combo" *ngIf="problem.action.name == 'SYNONYM'">
          <gdb:localize key="dataUploader.resolvedSynonym"/> [{{problem.action.label}}]
        </div> 
        <div class="inline-combo" *ngIf="problem.action.name == 'IGNORE'">
          <gdb:localize key="dataUploader.resolvedIgnoreCategory"/> [{{problem.label}}]
        </div> 
        <div class="inline-actions">    
          <i class="fa fa-undo" (click)="undoAction()" title="<gdb:localize key="dataUploader.undoFixedCategoryTooltip"/>" ></i> 
        </div>
      </div>
    </form>
  </div>
</div>