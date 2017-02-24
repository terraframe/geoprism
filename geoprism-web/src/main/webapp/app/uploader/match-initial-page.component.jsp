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

<form #form="ngForm" name="form" class="modal-form" (ngSubmit)="form.valid && !busy && onSubmit()">
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
    <strong><localize key="dataUploader.createNewLocationOrUpdateExistingLabel"></localize></strong>
  </div>
  <div class="holder">
      <div class="large-icon-stack-wrapper">
        <i class="fa-stack fa-3x" (click)="next('MATCH', 'MATCH-INITIAL')" [title]="'dataUploader.updateExistingDatasetButtonTooltip' | localize">
      <i class="fa fa-table fa-stack-2x"></i>
        <i class="fa-stack-3x fa-stack-text file-text fa fa-pencil-square"></i>
        </i>
        <h4><localize key="dataUploader.updateExistingDatasetButtonLabel"></localize></h4>
      </div>
      <div class="large-icon-stack-wrapper">
        <i class="fa-stack fa-3x" (click)="next('BEGINNING-INFO', 'MATCH-INITIAL')" [title]="'dataUploader.createNewDatasetButtonTooltip' | localize">
      <i class="fa fa-table fa-stack-2x"></i>
        <i class="fa-stack-3x fa-stack-text file-text fa fa-plus-square"></i>
        </i>
        <h4><localize key="dataUploader.createNewDatasetButtonLabel"></localize></h4>
      </div>
  </div> 
</div>
      
      </section>        
    </fieldset>    
  </div>
</form>