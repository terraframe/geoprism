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
  <modal-dialog modal="builder-div" overlay="ng-modal-overlay" ng-if="show" ng-show="!hidden">
    <div role="dialog" class="ng-modal-content modal-content" style="display: none;" show-on-ready>
      <div class="heading">
        <h1 class="ui-dialog-title"><gdb:localize key="dashboardbuilder.title"/></h1>
      </div>
      <form name="form" class="modal-form">
        <div ng-if="errors.length > 0 || fileErrors.length > 0" class="error-container">
          <div class="label-holder">
            <strong style="color: #8c0000;"><gdb:localize key='dashboard.errorsLabel'/></strong>
          </div>
          <div class="holder">
            <div ng-repeat="error in errors">
              <p class="error-message">{{error}}</p>
            </div>
            
            <div ng-repeat="error in fileErrors">
              <p class="error-message">{{error}}</p>
            </div>
          </div>
        </div>
        <div>
          <fieldset>
            <!-- Basic dashboard settings (i.e.Name and label) -->
            <section class="form-container">
              <div ng-repeat="field in fields">
                <div ng-if="field.readable && field.name != 'description' && field.name != 'country'" ng-switch on="field.type">
                  <div class="label-holder">
                    <strong>{{field.label}}</strong>
                  </div>
                  <select-field ng-switch-when="select" field="field" model="dashboard"></select-field>
                  <div class="holder">
                    <text-field ng-switch-when="text" field="field" maxlength="255" model="dashboard"></text-field>
                  </div>
                </div>          
              </div>
            </section>
            
            <div class="label-holder">
          <!-- <strong>Additional Settings</strong> -->
            </div>
            <div class="holder">
              <div class="row-holder">
                <div class="row builder-dialog-category-ico-container">
                  <div class="col-xs-4 col-md-4 builder-ico-container">
                    <a class="fa fa-list-alt" href="#" title="<gdb:localize key='dashboard.descriptionTooltip'/>" ng-click="ctrl.setCategoryWidgetType('DESCRIPTION')"></a>
                    <strong class="builder-dialog-ico-label"><gdb:localize key='dashboard.descriptionLabel'/></strong>
                  </div>
                  <div class="col-xs-4 col-md-4 builder-ico-container">
                    <a class="fa fa-users" href="#" title="<gdb:localize key='dashboard.usersTooltip'/>" ng-click="ctrl.setCategoryWidgetType('USERS')"></a>  
                    <strong class="builder-dialog-ico-label"><gdb:localize key='dashboard.usersLabel'/></strong>
                  </div>
                  <div class="col-xs-4 col-md-4 builder-ico-container">
                    <a class="fa fa-table" href="#" title="<gdb:localize key='dashboard.dataSetsTooltip'/>" ng-click="ctrl.setCategoryWidgetType('DATASETS')"></a>
                    <strong class="builder-dialog-ico-label"><gdb:localize key='dashboard.dataSetsLabel'/></strong>
                  </div>
                </div>
                <div id="builder-dialog-category-widget-container">
                
                  <div ng-show="showWidgetType == 'DESCRIPTION'" class="row-holder"> 
                    <text-area-field field="fields[1]" maxlength="255" placeholdertext="" model="dashboard"></text-area-field>
                  </div>
                
                  
                  <div ng-show="showWidgetType == 'USERS'" class="row-holder vertical-checkbox-list"> 
                    <div class="" ng-if="dashboard.options.users != null && dashboard.options.users.length > 0">
<!--                       <h3 class="panel-title"> -->
<%--                         <a class="" href="#user-field-row"><gdb:localize key='dashboardViewer.addDashboardUsersLabel'/></a> --%>
<!--                       </h3>           -->
                      <div id="user-field-row">
                        <div ng-repeat="user in dashboard.options.users">
                          <styled-check-box model="user.hasAccess" name="user_{{$index}}" label="{{user.firstName + ' ' + user.lastName}}"></styled-check-box>
                        </div>
                      </div>
                    </div>
                    <div class="" ng-if="dashboard.options.users != null && dashboard.options.users.length < 1">
                      <h4 class="user-notice"><gdb:localize key='dashboard.noUsersMsg'/></h4>
                    </div>
                  </div>
                  
                   <div ng-show="showWidgetType == 'DATASETS'" class="row-holder vertical-checkbox-list">
                    <div class="builder-data-set" ng-if="dashboard.options.types != null && dashboard.options.types.length > 0">
                      <div id="type-field-row" class="collapse in">
	                      <ul class="list-unstyled">
	                        <li ng-repeat="type in dashboard.options.types">
	                          <div class="vertical-checkbox-container">
	                            <styled-check-box model="type.value" name="type_{{$index}}" label=""></styled-check-box>
	                            <a class="opener-link checkbox-label" data-toggle="collapse" ng-href="#type{{$index}}">
	                            	<i class="fa fa-caret-right" aria-hidden="true"></i>
	  								            <i class="fa fa-caret-down" style="display:none;" aria-hidden="true"></i>
	                            	{{type.label}}
	                            </a>
	                          </div>
	                  
	                          <div id="type{{$index}}" class="collapse">
	                          	<ul class="builder-data-set-inner-list list-unstyled">
	                            	<type-attribute ng-repeat="attribute in type.attributes" attribute="attribute" type="type"></type-attribute>
	                          	</ul>
	                          </div>              
	                        </li>
	                      </ul>
                      </div>
                    </div>
                    <div class="" ng-show="dashboard.options.types != null && dashboard.options.types.length < 1">
                      <h4 class="user-notice"><gdb:localize key='dashboard.noDataSetsMsg'/></h4>
                    </div>
                    <div fire-on-ready></div>
                  </div>
                 
              </div> <!-- end widget container -->
              </div> <!-- end row-holder -->
            </div> <!-- end holder -->
            
            
           <div class="row-holder">
            <div class="label-holder"></div>
            <div class="holder">
              <div class="button-holder">
                <input
                  type="button"
                  value="<gdb:localize key="dashboard.Cancel"/>"
                  class="btn btn-default" 
                  ng-click="ctrl.cancel()"
                  ng-disabled="busy"                  
                  />
                <input
                  type="button"
                  value="<gdb:localize key="dashboard.Ok"/>"
                  class="btn btn-primary" 
                  ng-click="ctrl.persist()"
                  ng-disabled="form.$invalid || busy"
                  />
              </div>
            </div>
           </div>
          
          </fieldset>
        </div>
      </form>
    </div>
  </modal-dialog>       
</div>