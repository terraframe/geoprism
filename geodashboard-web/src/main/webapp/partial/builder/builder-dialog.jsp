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
	        <div class="" style="">
	          <fieldset class="">
				  <!-- Basic dashboard settings (i.e.Name, label, and country) -->
		          <section class="form-container">
		              <div ng-repeat="field in ctrl.fields">
		                <div ng-if="field.readable" ng-switch on="field.type">
		                  	<select-field ng-switch-when="select" field="field" model="ctrl.dashboard"></select-field>
		                  	<text-field ng-switch-when="text" field="field" maxlength="255" model="ctrl.dashboard"></text-field>
		                </div>          
		              </div>
		          </section>
	          
	          	  <!-- Dashboard users -->
	          	  <div class="label-holder">
    			  	<strong>Add users</strong>
    			  </div>
	          	  <div class="holder">
		          	  <div class="row-holder vertical-checkbox-list">
				          <div class="" ng-if="ctrl.dashboard.options.users != null && ctrl.dashboard.options.users.length > 0">
<!-- 				            <h3 class="panel-title"> -->
<%-- 				              <a class="opener-link" data-toggle="collapse" href="#user-field-row"><gdb:localize key='dashboardViewer.addDashboardUsersLabel'/></a> --%>
<!-- 				            </h3>           -->
				            <div id="user-field-row" class="collapse in">
				              <div ng-repeat="user in ctrl.dashboard.options.users">
				              	<styled-check-box model="user.hasAccess" name="user_{{$index}}" label="{{user.firstName + ' ' + user.lastName}}"></styled-check-box>
				              </div>
				            </div>
				          </div>
				      </div>
				  </div>
	        
	        	  <!-- Dashboard types and attributes -->
	        	  <div class="label-holder">
    				<strong>Add types and attributes</strong>
   	 			  </div>
	        	  <div class="holder">
		        	  <div class="vertical-checkbox-list">
				          <div class="" ng-if="ctrl.dashboard.options.types != null && ctrl.dashboard.options.types.length > 0">
<!-- 				            <h3 class="panel-title"> -->
<%-- 				              <a class="opener-link" data-toggle="collapse" href="#type-field-row"><gdb:localize key="dashboardbuilder.configureTypes"/></a> --%>
<!-- 				            </h3>           -->
<!-- 				            <div id="type-field-row" class="collapse in"> -->
				              <div ng-repeat="type in ctrl.dashboard.options.types">
<!-- 				                  <input type="checkbox" ng-model="type.value"></input>       -->
								<div class="vertical-checkbox-container">
				                	<styled-check-box model="type.value" name="type_{{$index}}" label=""></styled-check-box>
				                	<a class="opener-link checkbox-label" data-toggle="collapse" ng-href="#type{{$index}}">{{type.label}}</a>
				                </div>
<!-- 								<a class="opener-link checkbox-label" data-toggle="collapse" ng-href="#type{{$index}}">{{type.label}}</a> -->
								
				                
				                <div id="type{{$index}}" class="collapse">
				                  <type-attribute ng-repeat="attribute in type.attributes" attribute="attribute" type="type"></type-attribute>
				                </div>              
				              </div>
<!-- 				            </div> -->
				          </div>
				      </div>
				   </div>
          
				   <div class="row-holder">
						<div class="label-holder"></div>
						<div class="holder">
							<div class="button-holder">
								<input
									name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.create.button"
									type="button"
									value="<gdb:localize key="dashboard.Ok"/>"
									class="btn btn-primary" 
									ng-click="ctrl.persist()"
									ng-disabled="form.$invalid"
									/>
								<input
									name="com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.form.cancel.button"
									type="button"
									value="<gdb:localize key="dashboard.Cancel"/>"
									class="btn btn-default" 
									ng-click="ctrl.cancel()"
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