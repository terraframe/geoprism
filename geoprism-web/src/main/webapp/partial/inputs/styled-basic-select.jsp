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
<div class="select-holder" ng-class="selectClass">

	<select class="method-select" ng-model="model">
     	<option ng-repeat="opt in options() track by $index" ng-style="{'font-family' : opt}" value="{{opt}}">{{opt}}</option>
   	</select>
   	
<!--    	<select class="method-select" ng-options="opt as opt for opt in options() track by $index" ng-model="model"></select> -->

</div>