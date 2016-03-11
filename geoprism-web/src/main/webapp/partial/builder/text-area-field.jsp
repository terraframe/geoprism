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
  <div ng-if="field.writable">
    	<span class="text">
    		<input ng-model="model[field.name]" ng-required="field.required" ng-minlength="field.minlength" ng-maxlength="field.maxlength" placeholder="{{field.placeholdertext}}" name="{{field.name}}" class="" type="textarea">
    	</span>
    	<div class="error-message">
      		<p ng-show="form[field.name].$error.required"><gdb:localize key="dashboard.Required"/></p>    
      		<p ng-show="form[field.name].$error.maxlength"><gdb:localize key="dashboard.TextInputExceedMaxLimit"/></p>
      		<p ng-show="form[field.name].$error.minlength"><gdb:localize key="dashboard.TextInputExceedMinLimit"/></p>
    	</div>
  </div>
  <div ng-if="!field.writable && field.readable">
    	<span class="text">
    		<input ng-model="model[field.name]" ng-required="field.required" ng-minlength="field.minlength" ng-maxlength="field.maxlength" name="{{field.name}}" class="" type="textarea" ng-disabled="true">
    	</span>
  </div>
</div>
