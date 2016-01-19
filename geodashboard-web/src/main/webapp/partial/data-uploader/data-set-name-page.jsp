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
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>


<div>
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.label"/></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <div class="text">
        <input ng-model="sheet.label" name="label" ng-required="true" type="text"></input>
      </div>
      <div class="error-message">
        <p ng-show="form.label.$error.required"><gdb:localize key="dashboard.Required"/></p>    
      </div>
    </div>
  </div> 
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.country"/></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <div class="box">
        <select class="select-area" ng-model="sheet.country" name="country" ng-options="opt.value as opt.label for opt in options.countries" ng-required="true"></select>      
      </div>
      <div class="error-message">
        <p ng-show="form.country.$error.required"><gdb:localize key="dashboard.Required"/></p>    
      </div>
    </div>
  </div> 
</div>