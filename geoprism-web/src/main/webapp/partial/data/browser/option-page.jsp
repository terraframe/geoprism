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

<form class="modal-form" name="ctrl.form">    
  <div class="modal-dialog">
    <div class="modal-content" show-on-ready>
      <div class="heading">
        <h1><gdb:localize key="category.management.optionTitle"/></h1>
      </div>
      <fieldset>
        <div class="row-holder" ng-show="errors.length > 0 && show">
          <div class="label-holder">
          </div>
          <div class="holder">
            <div class="alert alertbox" ng-repeat="error in errors track by $index">
              <p class="error-message">{{error}}</p>
            </div>
          </div>
        </div>
        <div class="row-holder">
          <div class="label-holder">
            <label><gdb:localize key="category.management.option.label"/></label>
          </div>    
          <div class="holder" >
            <span class="text">
              <input type="text" ng-model="option.label" name="label" required>
            </span>
          </div>
        </div>
        <div class="row-holder" ng-show="option.synonyms.length > 0">
          <div class="label-holder">
            <label><gdb:localize key="category.management.synonyms"/></label>
          </div>    
          <div class="holder" >
            <table class="list-table table table-bordered table-striped">
              <tbody>
                <tr ng-repeat="synonym in option.synonyms" class="fade-ngRepeat-item">
                  <td class="button-column">
                    <a class="fa fa-undo ico-edit" ng-click="ctrl.restore(synonym)" title="<gdb:localize key="category.management.restoreTooltip"/>"></a>
                  </td>
                  <td>
                    {{synonym.label}}
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
        <div class="row-holder">
          <div class="label-holder">
          </div>    
          <div class="holder" >
            <div class="box">
              <label><gdb:localize key="category.management.createSynonym"/></label>
              <div class="select-box">
                <select class="method-select" ng-options="opt.id as opt.label for opt in category.descendants | filter:ctrl.filterOption()" ng-model="actions.synonym">
                  <option value=""></option>
                </select>
              </div>
            </div>          
          </div>
        </div>
        <div class="row-holder" fire-on-ready>
          <div class="label-holder">
          </div>  
          <div class="holder">
            <div class="button-holder">
              <input type="button" value="<gdb:localize key="dataset.cancel"/>" class="btn btn-default" ng-click="ctrl.cancel()" />              
              <input type="button" value="<gdb:localize key="dataset.submit"/>" class="btn btn-primary" ng-click="ctrl.apply()" ng-disabled="ctrl.form.$invalid" />
            </div>
          </div>
        </div>
      </fieldset>
    </div>
  </div>
</form>