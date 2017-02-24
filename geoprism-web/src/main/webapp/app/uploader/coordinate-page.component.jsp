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
  <div class="label-holder">
    <strong> </strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <p><localize key="dataUploader.coordinateLocation.heading.paragraph"></localize></p>
    </div>
  </div>
  
  <div *ngFor="let coordinate of sheet.coordinates; let i = index;">
    <div class="label-holder">
      <strong *ngIf="i == 0"><localize key="dataUploader.coordinateCreatorWidgetLabel"></localize></strong>
    </div>
    <div class="holder">
      <div class="location-selector-container">
        <span class="text">
          <input [(ngModel)]="coordinate.label" #label="ngModel" [name]="i + '-label'" required type="text" funcValidator [validator]="this" config="label" [placeholder]="'dataUploader.latFieldLabelPlaceholder' | localize" />
        </span>
        <div class="error-message">
          <p *ngIf="label.invalid">
            <localize key="dataUploader.unique"></localize>
          </p>    
        </div>   
                
        <div class="row-holder">
          <div class="label-help-ico-container">
            <i class="fa fa-question-circle help-info-ico" [title]="'dataUploader.latFieldHelpToolTip' | localize"></i>
            <p class="select-label"><localize key="dataUploader.latitude"></localize></p>
          </div>
          <div class="inline-text" style="padding-right: 26px">
            <input [(ngModel)]="coordinate.latitude" [name]="i + '-latitude'" required type="text" />
          </div>
        </div>
  
        <div class="row-holder">
          <div class="label-help-ico-container">
              <i class="fa fa-question-circle help-info-ico" [title]="'dataUploader.longFieldHelpToolTip' | localize"></i>
            <p class="select-label"><localize key="dataUploader.longitude"></localize></p>
          </div>
          <div class="box" *ngIf="longitudes.length != 1">
            <select class="select-area" [name]="i + '-longitude'" [(ngModel)]="coordinate.longitude" required>
              <option value=""></option>
              <option *ngFor="let opt of longitudes" [value]="opt.value">{{opt.label}}</option>              
            </select>
          </div>
          <div class="inline-text" style="padding-right: 26px" *ngIf="longitudes.length == 1">
            <input [(ngModel)]="coordinate.longitude" [name]="i + '-longitude'" required type="text" />
          </div>
        </div>
  
        <div class="row-holder">
          <div class="label-help-ico-container">
              <i class="fa fa-question-circle help-info-ico" [title]="'dataUploader.featureLabelFieldHelpToolTip' | localize"></i> 
            <p class="select-label"><localize key="dataUploader.featureLabel"></localize></p>
          </div>
          <div class="box">
            <select class="select-area" [name]="i + '-featureLabel'" [(ngModel)]="coordinate.featureLabel" required>
              <option value=""></option>
              <option *ngFor="let opt of featureLabels" [value]="opt.label">{{opt.label}}</option>              
            </select>
          </div> 
        </div>
        
        <div class="row-holder">
          <div class="label-help-ico-container">
              <i class="fa fa-question-circle help-info-ico" [title]="'dataUploader.locAttrFieldHelpToolTip' | localize"></i> 
            <p class="select-label"><localize key="dataUploader.locationAttribute"></localize></p>
          </div>
          <div class="box">
            <select class="select-area" [(ngModel)]="coordinate.location" [name]="i + '-location'" (change)="coordinate.universal = ''" required>
              <option value=""></option>          
              <option value="DERIVE"><localize key="dataUploader.deriveLocation"></localize></option>
              <option *ngFor="let location of locations" [value]="location.label">{{location.label}}</option>          
            </select>          
          </div> 
        </div>              
        
        <div class="row-holder fade-ngShow" *ngIf="coordinate.location == 'DERIVE'">
          <div class="label-help-ico-container">
            <i class="fa fa-question-circle help-info-ico" [title]="'dataUploader.assocUniversalieldHelpToolTip' | localize"></i>
            <p class="select-label"><localize key="dataUploader.associatedUniversal"></localize></p>
          </div>
          <div class="box">
            <select class="select-area" [(ngModel)]="coordinate.universal" [name]="i + '-universal'" [required]="coordinate.location == 'DERIVE'">
              <option value=""></option>          
              <option *ngFor="let opt of universals" [value]="opt.value">{{opt.label}}</option>              
            </select>
          </div>   
        </div>                
     </div>  <!-- end location-selector-container -->
    </div>  <!-- end holder -->
  </div>
</div>


      </section>        
    </fieldset>   
    
    <paging [form]="form" [page]="page"></paging>
  </div>
</form>