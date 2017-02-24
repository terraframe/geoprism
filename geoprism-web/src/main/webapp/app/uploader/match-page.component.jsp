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
      <p><localize key="dataUploader.titleUploadToExistingOrNewSubtitle"></localize></p>
    </div>
  </div>
  
  <div class="label-holder">
    <strong><localize key="dataUploader.existingDataset"></localize></strong>
  </div>
  <div class="holder">
    <ul id="match-datasets-list" class="list-group">   
        <li class="list-group-item" *ngFor="let match of sheet.matches">
          {{match.label}}            
      
          <div class="medium-icon-wrapper">
          <i class="fa fa-plus-square" (click)="select(match, false)" [title]="'dataUploader.selectDataset' | localize"></i>
        </div>
        <div class="medium-icon-wrapper">
          <i class="fa fa-minus-square" (click)="select(match, true)" [title]="'dataUploader.replaceDataset' | localize"></i>
        </div>
      </li>
    </ul>
  </div> 
</div>

      </section>        
    </fieldset>    
  </div>
</form>