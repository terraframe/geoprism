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
  <modal-dialog ng-show="ctrl.dashboard != null" width='1000px'>
    <div id="builder-div" role="dialog" class="ng-modal-content modal-content">
      <div class="heading">
        <h1 class="ui-dialog-title"><gdb:localize key="dashboardbuilder.title"/></h1>
      </div>
      <form name="form" class="modal-form">
<!--       	<div class="row-holder"> -->
	        <div class="" style="">
	          <fieldset class="">
		          <section class="form-container">
		              <div ng-repeat="field in ctrl.fields">
		                <div ng-if="field.readable" ng-switch on="field.type">
		                  <select-field ng-switch-when="select" field="field" model="ctrl.dashboard"></select-field>
		                  <text-field ng-switch-when="text" field="field" model="ctrl.dashboard"></text-field>
		                </div>          
		              </div>
		          </section>
	          
		          <div class="panel" ng-if="ctrl.dashboard.options.users != null && ctrl.dashboard.options.users.length > 0">
		            <h3 class="panel-title">
		              <a class="opener-link" data-toggle="collapse" href="#user-field-row"><gdb:localize key='dashboardViewer.addDashboardUsersLabel'/></a>
		            </h3>          
		            <div id="user-field-row" class="collapse in">
		              <div ng-repeat="user in ctrl.dashboard.options.users">
		                <div>
		                  <input type="checkbox" ng-model="user.hasAccess"></input>
		                  {{user.firstName + ' ' + user.lastName}}
		                </div>
		              </div>
		            </div>
		          </div>
	        
		          <div class="panel" ng-if="ctrl.dashboard.options.types != null && ctrl.dashboard.options.types.length > 0">
		            <h3 class="panel-title">
		              <a class="opener-link" data-toggle="collapse" href="#type-field-row"><gdb:localize key="dashboardbuilder.configureTypes"/></a>
		            </h3>          
		            <div id="type-field-row" class="collapse in">
		              <div ng-repeat="type in ctrl.dashboard.options.types">
		                <h4 class="panel-title">
		                  <input type="checkbox" ng-model="type.value"></input>              
		                  <a class="opener-link" data-toggle="collapse" ng-href="#type{{$index}}">{{type.label}}</a>
		                </h4>          
		                <div id="type{{$index}}" class="collapse">
		                  <type-attribute ng-repeat="attribute in type.attributes" attribute="attribute" type="type"></type-attribute>
		                </div>              
		              </div>
		            </div>
		          </div>
	          </fieldset>
	        </div> 
<!-- 	    </div>      -->
          
        <div class="ui-dialog-buttonpane ui-widget-content ui-helper-clearfix">
          <div class="ui-dialog-buttonset">
            <button ng-disabled="form.$invalid" ng-click="ctrl.persist()" aria-disabled="false" role="button" class="btn btn-primary ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" primary="true" type="button">
              <span class="ui-button-text"><gdb:localize key="dashboard.Ok"/></span>
            </button>
            <button ng-click="ctrl.cancel()" aria-disabled="false" role="button" class="btn ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" primary="true" type="button">
              <span class="ui-button-text"><gdb:localize key="dashboard.Cancel"/></span>
            </button>
          </div>      
        </div>
      </form>
    </div>
  </modal-dialog>       
</div>