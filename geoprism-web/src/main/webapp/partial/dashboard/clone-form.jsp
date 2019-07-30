<%--

    Copyright (c) 2015 TerraFrame, Inc. All rights reserved.

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
  <div ng-if="show">
    <div class="modal-backdrop fade in"></div>
    <div id="clone-modal" style="display: block;" class="modal fade in" role="dialog" aria-hidden="false" data-backdrop="static" data-keyboard="false">
      <dl>
        <form class="modal-form" name="form">
          <div class="modal-dialog">
            <div class="modal-content" style="display: none;" show-on-ready>
              <div class="heading">
                <h1><gdb:localize key="dashboard.clone.label"/> [{{dashboard.label}}]</h1>
              </div>
              <fieldset>
                <div class="row-holder" ng-show="errors.length > 0">
                  <div class="label-holder"></div>      
                  <div class="holder">
                    <div class="alert alertbox" ng-repeat="error in errors track by $index">
                      <p >{{error}}</p>
                    </div>
                  </div>
                </div>
                <div class="row-holder">
                  <div class="label-holder">
                    <strong><gdb:localize key="dashboard.label"/></strong>
                  </div>
                  <div class="holder">
                    <span class="text">
                      <input type="text" ng-model="dashboard.name" name="dashboard.name" required fire-on-ready>
                    </span>
                  </div>
                </div>
                <div class="row-holder">
                  <div class="label-holder"></div>
                  <div class="holder">
                    <div class="button-holder">
                      <input type="button" class="btn btn-primary" ng-click="ctrl.submit()" ng-disabled="form.$invalid" value="<gdb:localize key="dashboard.Ok"/>" />
                      <input type="button" class="btn btn-default" ng-click="ctrl.cancel()" value="<gdb:localize key="dashboard.Cancel"/>"/>
                    </div>
                  </div>
                </div>                
              </fieldset>
            </div>
          </div>
        </form>    
      </dl>
    </div>
  </div>
</div>