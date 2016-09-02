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
    <strong> </strong>
  </div>
  <div class="holder">
    <div class="row-holder">
    	<p><gdb:localize key="dataUploader.titleUploadToExistingOrNewSubtitle"/></p>
    </div>
  </div>
  
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.createNewLocationOrUpdateExistingLabel"/></strong>
  </div>
  <div class="holder">
  		<div class="large-icon-stack-wrapper">
    		<i class="fa-stack fa-3x" ng-click="ctrl.next('MATCH', 'MATCH-INITIAL')" title="<gdb:localize key="dataUploader.updateExistingDatasetButtonTooltip"/>">
    			<i class="fa fa-table fa-stack-2x"></i>
      			<i class="fa-stack-3x fa-stack-text file-text fa fa-pencil-square"></i>
  			</i>
  			<h4><gdb:localize key="dataUploader.updateExistingDatasetButtonLabel"/></h4>
  		</div>
  		<div class="large-icon-stack-wrapper">
  			<i class="fa-stack fa-3x" ng-click="ctrl.next('BEGINNING-INFO', 'MATCH-INITIAL')" title="<gdb:localize key="dataUploader.createNewDatasetButtonTooltip"/>">
    			<i class="fa fa-table fa-stack-2x"></i>
      			<i class="fa-stack-3x fa-stack-text file-text fa fa-plus-square"></i>
  			</i>
  			<h4><gdb:localize key="dataUploader.createNewDatasetButtonLabel"/></h4>
  		</div>
  </div> 
</div>