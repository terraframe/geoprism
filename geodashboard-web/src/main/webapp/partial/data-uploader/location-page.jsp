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
  <location-attribute-form sheet="sheet" universals="universals"></location-attribute-form>

  <div ng-if="sheet.attributes.ids.length > 0">
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.attributes"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder">
        <ul>
          <li ng-repeat="id in sheet.attributes.ids" ng-init="attribute = sheet.attributes.values[id]">
            {{attribute.label}}   
            <span>            
              <a href="#" ng-click="ctrl.remove(attribute)">Remove</a>
              <a href="#" ng-click="ctrl.edit(attribute)">Edit</a>
            </span>          
            <ul>
              <li ng-repeat="universal in universals" ng-if="attribute.fields[universal.value] != null">
                {{attribute.fields[universal.value].label}}            
              </li>
            </ul>       
          </li>    
        </ul>
      </div>
    </div>    
  </div>
</div>
