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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<div id="app-container" class="container">

  <h2> <gdb:localize key="category.management.title"/> </h2>
  
  <div ng-if="errors.length > 0" class="error-container" ng-cloak>
    <div class="label-holder">
      <strong><gdb:localize key='dashboard.errorsLabel'/></strong>
    </div>
    <div class="holder">
      <div ng-repeat="error in errors" >
        <p class="error-message">{{error}}</p>
      </div>
    </div>
  </div>
  
  <div ng-if="category.management === null"><gdb:localize key='category.management.loadingData'/></div>
  <div class="datasets-table-wrapper">
    <table class="list-table table table-bordered table-striped">        
      <tbody>
        <tr ng-repeat="category in categories" class="fade-ngRepeat-item" ng-cloak>
          <td class="button-column">
            <a class="fa fa-tasks ico-edit" ng-click="ctrl.edit(category)" title="<gdb:localize key="category.management.editTooltip"/>"></a>                             
          </td>
          <td class="label-column">
            {{category.label}}
          </td>
        </tr>
      </tbody>    
    </table>
   </div>  
   
   <category-modal></category-modal>
</div>
