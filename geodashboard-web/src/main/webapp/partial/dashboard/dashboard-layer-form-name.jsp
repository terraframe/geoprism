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
        
            <div class="row-holder" >
              <div class="label-holder">
                <strong><gdb:localize var="dl_form_nameTheLayer" key="DashboardThematicLayer.form.nameTheLayer"/>${dl_form_nameTheLayer}</strong>
              </div>
              <div class="holder" >
	                <label class="none" for="f312">{{thematicLayerModel.layerName}}</label>
	                <span class="text">
						<input type="text" id="layer.name" ng-model="thematicLayerModel.layerName" name="layer.name" required>
						
	                      <mjl:messages attribute="name" classes="error-message">
	                        <mjl:message />
	                      </mjl:messages>
	                </span>
              </div>
            </div>