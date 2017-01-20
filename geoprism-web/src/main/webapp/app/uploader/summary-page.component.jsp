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
      <p><gdb:localize key="dataUploader.summary.heading.paragraph"/></p>
    </div>
  </div>
  <div *ngIf="texts.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.summaryTextLabel"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">
         <table class="table table-bordered" style="font-size:inherit;"> 
            <thead> 
              <tr>
                <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let field of texts">
                <td>{{field.label}}</td>
              </tr>
            </tbody>
           </table>
      </div>
    </div>
  </div>

  <div *ngIf="categories.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.summaryCategoryLabel"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">         
           <table class="table table-bordered" style="font-size:inherit;"> 
            <thead> 
              <tr>
                <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let field of categories">
                <td>{{field.label}}</td>
              </tr>
            </tbody>
           </table>
      </div>
    </div>
  </div>
  
  <div *ngIf="numbers.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.summaryNumberLabel"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">    
           <table class="table table-bordered" style="font-size:inherit;"> 
            <thead> 
              <tr>
                <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
                <th><gdb:localize key="dataUploader.summary.tableHeading.type.label"/></th>
              </tr>
            </thead>
            <tbody> 
              <tr *ngFor="let field of numbers">
                <td>{{field.label}}</td>
                <td [ngSwitch]="field.type">
                    <div *ngSwitchCase="'LONG'"><gdb:localize key="dataUploader.long"/></div>
                    <div *ngSwitchCase="'DOUBLE'"><gdb:localize key="dataUploader.double"/></div>
                  </td>          
              </tr>
            </tbody>
           </table>
        </div>
    </div>
  </div>
   
  <div *ngIf="booleans.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.summaryBooleanLabel"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">   
           <table class="table table-bordered" style="font-size:inherit;"> 
            <thead> 
              <tr>
                <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
              </tr>
            </thead>
            <tbody> 
              <tr *ngFor="let field of booleans">
                <td>{{field.label}}</td>
              </tr>
            </tbody>
           </table>
      </div>
    </div>
  </div>

  <div *ngIf="dates.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.summaryDateLabel"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">            
           <table class="table table-bordered" style="font-size:inherit;"> 
            <thead> 
              <tr>
                <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
              </tr>
            </thead>
            <tbody>
              <tr *ngFor="let field of dates">
                <td>{{field.label}}</td>
              </tr>
            </tbody>
           </table>
      </div>
    </div>
  </div>
   
  <div *ngIf="hasFieldType('LOCATION')">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.summaryTextLocationLabel"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">         
           <table class="table table-bordered" style="font-size:inherit;"> 
            <thead> 
              <tr>
                <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
                <th><gdb:localize key="dataUploader.summary.tableHeading.textLocRefFields.label"/></th>
              </tr>
            </thead>
            <tbody> 
            
              <!-- TEXT BASED LOCATION FIELDS -->
              <tr *ngFor="let key of sheet.attributes.values | keys">
                  <td>{{key.value.label}}</td>
                  <td>
                    <table class="table table-bordered location-summary-sub-table"> 
                      <tbody>
                        <tr *ngFor="let universal of universalMap[key.value.id]">
                          <td>{{key.value.fields[universal.value]}}</td>
                        </tr> 
                      </tbody>
                    </table>
                  </td>
              </tr>  
            </tbody>
           </table>
        </div>
     </div>
  </div>
    
  <div *ngIf="hasFieldType('LATITUDE') && hasFieldType('LONGITUDE')">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.summaryCoordinateLabel"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">        
           <table class="table table-bordered" style="font-size:inherit;"> 
            <thead> 
              <tr>
                <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
                <th><gdb:localize key="dataUploader.summary.tableHeading.coordLocRefFields.label"/></th>
              </tr>
            </thead>
            <tbody> 
            
          <!-- COORDINATE LOCATION FIELDS -->
              <tr *ngFor="let coordinate of sheet.coordinates">
                  <td>{{coordinate.label}}</td>
                  <td> 
                    <table class="table table-bordered location-summary-sub-table"> 
                    <thead> 
                      <tr>
                        <th><gdb:localize key="dataUploader.summary.tableHeading.label"/></th>
                        <th><gdb:localize key="dataUploader.summary.tableHeading.refField.label"/></th>
                      </tr>
                    </thead>
                      <tbody>
                        <tr><td><gdb:localize key="dataUploader.latitude"/></td><td>{{coordinate.latitude}}</td></tr>
                        <tr><td><gdb:localize key="dataUploader.longitude"/></td><td>{{coordinate.longitude}}</td></tr>
                        <tr><td><gdb:localize key="dataUploader.featureLabel"/></td><td>{{coordinate.featureLabel}}</td></tr>
                        <tr *ngIf="coordinate.location != 'DERIVE'"><td><gdb:localize key="dataUploader.locationAttribute"/></td><td>{{coordinate.location}}</td></tr>
                        <tr *ngIf="coordinate.location == 'DERIVE'"><td><gdb:localize key="dataUploader.locationAttribute"/></td><td><gdb:localize key="dataUploader.deriveLocation"/></td></tr>
                        <tr *ngIf="coordinate.location == 'DERIVE'"><td><gdb:localize key="dataUploader.associatedUniversal"/></td><td>{{labels[coordinate.universal]}}</td></tr>
                      </tbody>
                    </table>
                </td>      
              </tr>        
            </tbody>
           </table>
        </div>
    </div>
  </div>
</div>


      </section>        
    </fieldset>   
    
    <paging [form]="form" [page]="page"></paging>
  </div>
</form>

