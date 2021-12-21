<%--

    Copyright (c) 2022 TerraFrame, Inc. All rights reserved.

    This file is part of Geoprism(tm).

    Geoprism(tm) is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Geoprism(tm) is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public
    License along with Geoprism(tm).  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<div>
  <div class="wide-holder">
    <div class="row-holder">    
      <div class="inline-value">
        <label><gdb:localize key="dataUploader.locationContext"/></label>   
        <div class="label-help-ico-container">
       		<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.locationContextHelpToolTip"/>"></i>
      	</div>   
      </div>
      <div class="inline-value">
      	<label><gdb:localize key="dataUploader.unknownLocation"/></label>
      	<div class="label-help-ico-container">
       		<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.unknownLocationNameHelpToolTip"/>"></i>
     	</div>   
      </div>
      <div class="inline-combo">
      	<label><gdb:localize key="dataUploader.synonymn"/></label>
      	<div class="label-help-ico-container">
       		<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.synonymSearchHelpToolTip"/>"></i>
     	</div> 
      </div>
      <div class="inline-actions">
      	<label><gdb:localize key="dataUploader.actions"/></label>
      	<div class="label-help-ico-container">
       		<i class="fa fa-question-circle help-info-ico" title="<gdb:localize key="dataUploader.locationProblemActionsHelpToolTip"/>"></i>
     	</div> 
      </div>
    </div>  
    
    <div ng-repeat="problem in problems.locations">
      <geo-validation-problem problem="problem"></geo-validation-problem>
    </div>
  </div>
  <div class="wide-holder">
    <div class="error-message">
      <p ng-show="form.$error.size"><gdb:localize key="dataUploader.existingProblems"/></p>
    </div>          
    <div>
      <p ng-show="!form.$error.size"><gdb:localize key="dataUploader.noLocationProblem"/></p>
    </div>          
  </div>  
</div>
