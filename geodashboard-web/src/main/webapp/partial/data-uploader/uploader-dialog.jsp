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
  <modal-dialog ng-if="show">
    <div id="builder-div" role="dialog" class="ng-modal-content modal-content">
      <div class="heading">
        <h1 class="ui-dialog-title"><gdb:localize key="dataUploader.title"/></h1>
      </div>
      <form name="form" class="modal-form">
        <div ng-if="errors.length > 0" class="error-container">
          <div class="label-holder">
            <strong style="color: #8c0000;"><gdb:localize key='dashboard.errorsLabel'/></strong>
          </div>
          <div class="holder">
            <div ng-repeat="error in errors">
              <p class="error-message">{{error}}</p>
            </div>
          </div>
        </div>
        <div class="" style="">
          <fieldset class="">
            <section class="form-container">
              <div>
                <div ng-repeat="attribute in attributes">
                  <div class="holder">
                    <div class="row-holder">{{attribute.name}}</div>
                  </div>
                </div>
              </div>
            </section>
            
            <div class="row-holder">
              <div class="label-holder"></div>
              <div class="holder">
                <div class="button-holder">
                  <input
                    type="button"
                    value="<gdb:localize key="dashboard.Ok"/>"
                    class="btn btn-primary" 
                    ng-click="ctrl.persist()"
                    ng-disabled="form.$invalid || busy"
                  />
                  <input
                    type="button"
                    value="<gdb:localize key="dashboard.Cancel"/>"
                    class="btn btn-default" 
                    ng-click="ctrl.cancel()"
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