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
<%@ taglib uri="../../WEB-INF/tlds/geoprism.tld" prefix="gdb"%>


<div class="row-holder">
	<div class="label-holder"></div>
	<div class="holder">
		<div class="button-holder">
			<input
				type="button"
				value="<gdb:localize key="dashboardViewer.cancel"/>"
				class="btn btn-default" 
				ng-click="cancel()()"
				/>
			<input
				type="button"
				value="<gdb:localize key="dashboardViewer.mapItButton"/>"
				class="btn btn-primary" 
				ng-click="persist()()"
        ng-disabled="form.$invalid"
				/>
		</div>
	</div>
</div>
