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
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.label"/></strong>
  </div>
  <div class="holder">
    <span class="text">
      <input [(ngModel)]="sheet.label" name="label" type="text" required asyncValidator [remoteValidator]="this" #label="ngModel" />
    </span>
    <div class="inline-error-message">
      <p *ngIf="!label.valid">
        <gdb:localize key="dataUploader.unique"/>
      </p>    
    </div>      
  </div> 
  <div class="label-holder">
    <label><gdb:localize key="dataset.source"/></label>
  </div>          
  <div class="holder" >
    <span class="text">
      <textarea [(ngModel)]="sheet.source" name="source"></textarea>
    </span>
  </div>
  <div class="label-holder">
    <label><gdb:localize key="dataset.description"/></label>
  </div>          
  <div class="holder" >
    <span class="text">
      <textarea [(ngModel)]="sheet.description" name="description"></textarea>
    </span>
  </div>
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.country"/></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <div class="box">
        <select class="select-area" [(ngModel)]="sheet.country" name="country" required>
          <option value=""></option>
          <option *ngFor="let opt of options.countries" [value]="opt.value">{{opt.label}}</option>
        </select>      
      </div>
    </div>
  </div> 
</div>