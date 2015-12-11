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
	 <div ng-if="field.writable">
	    	<div id="country-select-box" class="box">
			    <styled-select class="['select-area']" options="field.options" model="model[field.name]" label="displayLabel" name="{{field.name}}" ng-required="field.required"></styled-select>               
			    <div class="error-message">
			      <p ng-show="form[field.name].$error.required"><gdb:localize key="dashboard.Required"/></p>
			    </div>
			</div>
	</div>
	<div ng-if="!field.writable && field.readable">
	    	<div id="country-select-box" class="box">
	    		<h4 class="disabled">{{model.countryDisplayLabel}}</h4>
				<!-- NOTE using basic text until we sort out our select widgets so I can effectively disable it -->
				<!-- <styled-select class="['select-area']" options="field.options" model="model[field.name]" name="{{field.name}}" ng-disabled="true" ng-required="field.required"></styled-select>                -->
			</div>
	</div>  
</div>
