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


<div class="row-holder" >
  <div class="label-holder"></div>      
  <div [ngClass]="page.layout">
    <div class="button-holder">
      <input
        type="button"
        value="<gdb:localize key="dashboard.Cancel"/>"
        class="btn btn-default" 
        (click)="cancel()"
        [disabled]="busy"              
      />
      <input
        *ngIf="page.prev != null"            
        type="button"
        value="<gdb:localize key="dataUploader.previous"/>"
        class="btn btn-primary" 
        [disabled]="busy"
        confirm-modal 
        message="<gdb:localize key="dataUploader.prevDialogContent"/>"
        (onConfirm)="prev()"
        [enabled]="page.confirm" />        
      <input
        *ngIf="page.hasNext"      
        type="button"
        value="<gdb:localize key="dataUploader.next"/>"
        class="btn btn-primary" 
        (click)="next()"
        [disabled]="!form.valid || !global"
      />
      <input 
        *ngIf="page.isReady"
        type="button"
        value="<gdb:localize key="dataUploader.import"/>"
        class="btn btn-primary" 
        (click)="ready()"        
        [disabled]="!form.valid || !global" />      
    </div>
  </div>
</div>      
