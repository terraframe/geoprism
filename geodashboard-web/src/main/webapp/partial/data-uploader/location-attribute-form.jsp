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

<ng-form name="ctrl.attributeForm" isolate-form>
  <div class="label-holder">
    <strong><gdb:localize key="dataUploader.locationFieldBuilder"/></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <div class="text">
        <input ng-model="attribute.label" name="label" ng-required="true" type="text" validate-unique validator="ctrl.isUniqueLabel"></input>
      </div>
    </div>
    <div class="error-message">
      <p ng-show="ctrl.attributeForm.label.$error.unique">
        <gdb:localize key="dataUploader.unique"/>
      </p>    
    </div>    
    <div class="row-holder">
      <ul>
        <li ng-repeat="universal in universals" ng-if="attribute.fields[universal.value] != null">
          {{attribute.fields[universal.value].label}}            
        </li>
      </ul>       
      <div class="error-message">
        <p ng-show="ctrl.attributeForm.$error.size"><gdb:localize key="dataUploader.notEnoughFields"/></p>    
      </div>          
    </div>
    <div class="row-holder">
      <div class="button-holder">
        <input type="button" value="+" class="btn btn-primary"  ng-click="ctrl.newAttribute()" ng-disabled="ctrl.attributeForm.$invalid" />
      </div>
    </div>    
  </div>
  <div>
    <div class="label-holder">
      <strong><gdb:localize key="dataUploader.locationFieldAssignment"/></strong>
    </div>
    <div class="holder">
      <div class="row-holder" style="padding-top: 15px;">
        <ul style="list-style: none;">
          <li ng-repeat="universal in universals">
            {{universal.label}}
            <ul style="list-style: none;">
              <li ng-repeat="field in sheet.fields" ng-if="field.type == 'LOCATION' && field.universal == universal.value">
                <div class="inline-check-block">
                  <div ng-click="ctrl.toggleField(universal, field)" ng-class="{'chk-checked' : (attribute.fields[universal.value] != null && attribute.fields[universal.value].name == field.name)}" class="jcf-unselectable chk-area">
                    <span></span>
                  </div>
                  <label>{{field.label}}</label>
                  <span ng-if="field.selected">Selected</span>                  
                </div>            
              </li>
            </ul>       
          </li>    
        </ul>
      </div>
    </div>
  </div>
</ng-form>  
