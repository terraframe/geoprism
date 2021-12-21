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
	 <div ng-if="field.writable">
	 
	    	<div id="country-select-box" class="box">
			    <styled-select class="select-area" options="field.options" model="model[field.name]" label="displayLabel" name="{{field.name}}" ng-required="field.required"></styled-select>               
			    <div class="error-message">
			      <p ng-show="form[field.name].$error.required"><gdb:localize key="dashboard.Required"/></p>
			    </div>
			</div>
	</div>
	<div ng-if="!field.writable && field.readable">
	    	<div id="country-select-box" class="box">
				<styled-select class="select-area" options="field.options" model="model[field.name]" label="displayLabel" name="{{field.name}}" ng-disabled="true" ng-required="field.required"></styled-select>               
			</div>
	</div>  
</div>
