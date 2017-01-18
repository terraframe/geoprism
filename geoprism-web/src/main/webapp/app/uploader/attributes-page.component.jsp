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

<form #form="ngForm" name="form" class="modal-form">
  <div>
    <fieldset>
      <section class="form-container">


<div>
  <div class="wide-holder">
    <div class="row-holder">
      <p><gdb:localize key="dataUploader.attributeConfiguration.heading.paragraph"/></p>
    </div>
  </div>
  
  <div class="wide-holder">
    <div class="row-holder" *ngFor="let field of sheet.fields; let i = index;">
      <div class="inline-text">
        <label><gdb:localize key="dataUploader.label"/></label>
        <input [(ngModel)]="field.label" #label="ngModel" [name]="i + '-name'" [ngClass]="{textInputDisabled : field.type == 'IGNORE'}" required type="text" funcValidator [validator]="this" config="label" />
      </div>
      <div class="inline-box" *ngIf="field.columnType == 'TEXT'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" [(ngModel)]="field.type" [ngClass]="{selectInputDisabled : field.type == 'IGNORE'}" [name]="i + '-type'" required (blur)="accept(field)">
          <option value="LOCATION"><gdb:localize key="dataUploader.location"/></option>
          <option value="CATEGORY"><gdb:localize key="dataUploader.category"/></option>
          <option value="TEXT"><gdb:localize key="dataUploader.text"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
        </select>      
      </div>
      <div class="inline-box" *ngIf="field.columnType == 'NUMBER'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (blur)="accept(field)">
          <option value="LONG"><gdb:localize key="dataUploader.long"/></option>
          <option value="DOUBLE"><gdb:localize key="dataUploader.double"/></option>
          <option value="LATITUDE"><gdb:localize key="dataUploader.latitude"/></option>
          <option value="LONGITUDE"><gdb:localize key="dataUploader.longitude"/></option>
          <option value="TEXT"><gdb:localize key="dataUploader.text"/></option>     
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
        </select>      
      </div>      
      <div class="inline-box" *ngIf="field.columnType == 'DATE'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (blur)="accept(field)">
          <option value="DATE"><gdb:localize key="dataUploader.date"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
        </select>      
      </div>      
      <div class="inline-box" *ngIf="field.columnType == 'BOOLEAN'">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (blur)="accept(field)">
          <option value="BOOLEAN"><gdb:localize key="dataUploader.boolean"/></option>
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
        </select>      
      </div>      
      <div class="inline-box" *ngIf="field.columnType == ''">
        <label><gdb:localize key="dataUploader.type"/></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (blur)="accept(field)">
          <option value="IGNORE"><gdb:localize key="dataUploader.ignore"/></option>
        </select>      
      </div>      
      <div class="inline-box fade-ngIf" *ngIf="field.type == 'LOCATION'">
        <label><gdb:localize key="dataUploader.locationType"/></label>
        <select class="select-area" [(ngModel)]="field.universal" [name]="i + '-universal'" required>
          <option value=""></option>
          <option *ngFor="let opt of universals" [value]="opt.value">{{opt.label}}</option>
        </select>
      </div>      
      <div class="inline-box fade-ngIf" *ngIf="field.type == 'CATEGORY'">
        <label><gdb:localize key="dataUploader.domainRoot"/></label>
        <select class="select-area" [(ngModel)]="field.root" [name]="i + '-root'">
          <option value=""><gdb:localize key="dataUploader.new"/></option>          
          <option *ngFor="let opt of info.classifiers" [value]="opt.value">{{opt.label}}</option>          
        </select>
      </div>      
      <div class="inline-text fade-ngIf" *ngIf="field.type == 'CATEGORY' && field.root == ''">
        <label><gdb:localize key="dataUploader.categoryLabel"/></label>
        <input [(ngModel)]="field.categoryLabel" #categoryLabel="ngModel" [name]="i + '-categoryLabel'" required type="text" asyncValidator [validator]="this" config="category" />
      </div>      
      
      <div class="inline-error-message pull-right">
        <p *ngIf="label.invalid">
          <gdb:localize key="dataUploader.unique"/>
        </p>    
        <p *ngIf="categoryLabel && categoryLabel.invalid">
          <gdb:localize key="dataUploader.uniqueCategory"/>
        </p>    
      </div>      
    </div>
  </div> 
<!--  
  <div class="wide-holder">
    <div class="error-message">
      <p *ngIf="form.$error.coordinate"><gdb:localize key="dataUploader.coordinateMismatch"/></p>
      <p *ngIf="form.$error.coordinateText"><gdb:localize key="dataUploader.coordinateNoLabel"/></p>
    </div>          
  </div>    
 -->
</div>

      </section>        
    </fieldset>   
    
    <paging [form]="form" [page]="page"></paging>
  </div>
</form>
