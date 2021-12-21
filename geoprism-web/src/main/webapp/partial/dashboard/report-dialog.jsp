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
  <modal-dialog modal="builder-div" overlay="ng-modal-overlay" ng-if="show">
    <div role="dialog" class="ng-modal-content modal-content" style="display: none;" show-on-ready>
      <div class="heading">
        <h1 class="ui-dialog-title"><gdb:localize key="dashboardbuilder.title"/></h1>
      </div>
      <form name="form" class="modal-form" id="report-form">
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
            <div ng-if="!report.newInstance" class="label-holder">
              <strong><gdb:localize key='report.file'/></strong>
            </div>          
            <div ng-if="!report.newInstance" class="holder">
              <div class="row-holder">
                {{report.reportName}}
              </div>
            </div>
            
            <div class="label-holder">
              <strong><gdb:localize key='report.design'/></strong>
            </div>          
            <div class="holder">
              <div class="row-holder">
                <input type="file" file="ctrl.onFileSelect" required />
                <div id="design-error" class="error-message"></div>
              </div>
            </div>
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
          <div fire-on-ready></div>                 
        </div>
      </form>
    </div>
  </modal-dialog>       
</div>