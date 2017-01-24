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
      <div class="inline-value">
        <ol *ngIf="problem.context.length > 0">
          <li *ngFor="let context of problem.context">{{context.label}} ({{context.universal}})</li>        
        </ol>
      </div>
      <div class="inline-value error-message">{{problem.label}} ({{problem.universalLabel}})</div>
      <div *ngIf="!problem.resolved">      
        <div class="inline-combo">
          <input class="synonym" [name]="index + '-name'" type="text" placeholder="<gdb:localize key="dataUploader.synonymSearchPlaceholder"/>" required autocomplete="off" auto-complete [source]="source" (onDropdownSelect)="setSynonym($event)" />
        </div>
        <div class="inline-actions">
          <i aria-hidden="true" data-icon="&#xe900;" class="icon-synonym_icon" [ngClass]="{disabled: !hasSynonym}" (click)="createSynonym()" title="<gdb:localize key="dataUploader.createSynonymFromLocationTooltip"/>" ></i>
          <i aria-hidden="true" data-icon="&#xe901;" class="icon-new_location_icon" (click)="createEntity()" title="<gdb:localize key="dataUploader.createNewLocationTooltip"/>" ></i>
          <span class="fa-stack fa-lg" title="<gdb:localize key="dataUploader.ignoreAtLocationTooltip"/>" (click)="ignoreDataAtLocation()">
            <i class="fa fa-square fa-stack-2x"></i>
            <i class="fa fa-times fa-stack-1x"></i>
          </span>
        </div>
      </div>
      <div *ngIf="problem.resolved">
        <div class="inline-combo" *ngIf="problem.action.name == 'ENTITY'">
          <gdb:localize key="dataUploader.resolvedEntity"/>
        </div>      
        <div class="inline-combo" *ngIf="problem.action.name == 'SYNONYM'">
          <a (click)="toggle()">
            <i class="fa fa-caret-right" aria-hidden="true"></i>
            <i class="fa fa-caret-down" style="display:none;" aria-hidden="true"></i>
            <gdb:localize key="dataUploader.resolvedSynonym"/> [{{problem.action.label}}]
          </a>
  
          <span *ngIf="show">
            <ul>
              <gdb:localize key="dataUploader.locatedWithin"/>
              <li *ngFor="let ancestor of action.ancestors">{{ancestor.label}}</li>
            </ul>
          </span>
        </div> 
        <div class="inline-combo" *ngIf="problem.action.name == 'IGNOREATLOCATION'">
          <gdb:localize key="dataUploader.resolvedIgnoreAtLocation"/> [{{problem.action.label}}]
        </div> 
        <div class="inline-actions">    
          <i class="fa fa-undo" (click)="undoAction()" title="<gdb:localize key="dataUploader.undoFixedLocationTooltip"/>" ></i> 
        </div>
      </div>
    </form>
  </div>
</div>