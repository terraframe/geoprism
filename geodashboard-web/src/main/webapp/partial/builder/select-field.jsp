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

<div class="row-holder">
	 <div ng-if="field.writable">
	    <div class="label-holder">
    		<strong>{{field.label}}</strong>
    	</div>
    	<div class="holder">
	    	<div id="country-select-box" class="box">
			    
			    <!-- <label for="{{field.name}}" class="">{{field.label}}</label>  -->
			    <styled-basic-select class="['select-area']" options="field.options" model="model[field.name]" name="{{field.name}}"></styled-basic-select>               
			    	
<!-- 			      <select ng-model="model[field.name]" ng-required="field.required" name="{{field.name}}"> -->
<!-- 			        <option ng-repeat="option in field.options" value="{{option.value}}">{{option.displayLabel}}</option> -->
<!-- 			      </select>   -->
			
			    <div class="error-message">
			      <p ng-show="form[field.name].$error.required"><gdb:localize key="dashboard.Required"/></p>
			    </div>
			</div>
		</div>
	</div>
	<div ng-if="!field.writable && field.readable">
	    <label class="com-runwaysdk-ui-factory-runway-Label com-runwaysdk-ui-factory-runway-Widget">{{field.label}}</label>
	    <div ng-repeat="option in field.options">
	      <p ng-if="model[field.name] == option.value">{{option.displayLabel}}</p>
	    </div>
	</div>  
</div>
