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
      <p><localize key="dataUploader.attributeConfiguration.heading.paragraph"></localize></p>
    </div>
  </div>
  
  <div class="wide-holder">
    <div class="row-holder" *ngFor="let field of sheet.fields; let i = index;">
      <div class="inline-text">
        <label><localize key="dataUploader.label"></localize></label>
        <input [(ngModel)]="field.label" #label="ngModel" [name]="i + '-name'" [ngClass]="{textInputDisabled : field.type == 'IGNORE'}" required type="text" funcValidator [validator]="this" config="label" />
      </div>
      <div class="inline-box" *ngIf="field.columnType == 'TEXT'">
        <label><localize key="dataUploader.type"></localize></label>
        <select class="select-area" [(ngModel)]="field.type" [ngClass]="{selectInputDisabled : field.type == 'IGNORE'}" [name]="i + '-type'" required (change)="accept(field)">
          <option value="LOCATION"><localize key="dataUploader.location"></localize></option>
          <option value="CATEGORY"><localize key="dataUploader.category"></localize></option>
          <option value="TEXT"><localize key="dataUploader.text"></localize></option>
          <option value="IGNORE"><localize key="dataUploader.ignore"></localize></option>
        </select>      
      </div>
      <div class="inline-box" *ngIf="field.columnType == 'NUMBER'">
        <label><localize key="dataUploader.type"></localize></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (change)="accept(field)">
          <option value="LONG"><localize key="dataUploader.long"></localize></option>
          <option value="DOUBLE"><localize key="dataUploader.double"></localize></option>
          <option value="LATITUDE"><localize key="dataUploader.latitude"></localize></option>
          <option value="LONGITUDE"><localize key="dataUploader.longitude"></localize></option>
          <option value="TEXT"><localize key="dataUploader.text"></localize></option>     
          <option value="IGNORE"><localize key="dataUploader.ignore"></localize></option>
        </select>      
      </div>      
      <div class="inline-box" *ngIf="field.columnType == 'DATE'">
        <label><localize key="dataUploader.type"></localize></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (change)="accept(field)">
          <option value="DATE"><localize key="dataUploader.date"></localize></option>
          <option value="IGNORE"><localize key="dataUploader.ignore"></localize></option>
        </select>      
      </div>      
      <div class="inline-box" *ngIf="field.columnType == 'BOOLEAN'">
        <label><localize key="dataUploader.type"></localize></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (change)="accept(field)">
          <option value="BOOLEAN"><localize key="dataUploader.boolean"></localize></option>
          <option value="IGNORE"><localize key="dataUploader.ignore"></localize></option>
        </select>      
      </div>      
      <div class="inline-box" *ngIf="field.columnType == ''">
        <label><localize key="dataUploader.type"></localize></label>
        <select class="select-area" [(ngModel)]="field.type" [name]="i + '-type'" required (change)="accept(field)">
          <option value="IGNORE"><localize key="dataUploader.ignore"></localize></option>
        </select>      
      </div>      
      <div class="inline-box fade-ngIf" *ngIf="field.type == 'LOCATION'">
        <label><localize key="dataUploader.locationType"></localize></label>
        <select class="select-area" [(ngModel)]="field.universal" [name]="i + '-universal'" required>
          <option value=""></option>
          <option *ngFor="let opt of universals" [value]="opt.value">{{opt.label}}</option>
        </select>
      </div>      
      <div class="inline-box fade-ngIf" *ngIf="field.type == 'CATEGORY'">
        <label><localize key="dataUploader.domainRoot"></localize></label>
        <select class="select-area" [(ngModel)]="field.root" [name]="i + '-root'">
          <option value=""><localize key="dataUploader.new"></localize></option>          
          <option *ngFor="let opt of info.classifiers" [value]="opt.value">{{opt.label}}</option>          
        </select>
      </div>      
      <div class="inline-text fade-ngIf" *ngIf="field.type == 'CATEGORY' && field.root == ''">
        <label><localize key="dataUploader.categoryLabel"></localize></label>
        <input [(ngModel)]="field.categoryLabel" #categoryLabel="ngModel" [name]="i + '-categoryLabel'" required type="text" asyncValidator [validator]="this" config="category" />
      </div>      
      
      <div class="inline-error-message pull-right">
        <p *ngIf="label.invalid">
          <localize key="dataUploader.unique"></localize>
        </p>    
        <p *ngIf="categoryLabel && categoryLabel.invalid">
          <localize key="dataUploader.uniqueCategory"></localize>
        </p>    
      </div>      
    </div>
  </div> 
  <div class="wide-holder">
    <div class="error-message">
      <p *ngIf="coordinateMismatch"><localize key="dataUploader.coordinateMismatch"></localize></p>
      <p *ngIf="coordinateText"><localize key="dataUploader.coordinateNoLabel"></localize></p>
    </div>          
  </div>    
</div>

      </section>        
    </fieldset>   
    
    <paging [form]="form" [page]="page" [global]="!(coordinateMismatch || coordinateText)"></paging>
  </div>
</form>
