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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
  <!-- ngIf cannot be on the root node or processing doesn't work -->
  <div ng-if="show">
    <div class="modal-backdrop fade in"></div>
    <div id="reference-modal" style="display: block;" class="modal fade in" role="dialog" aria-hidden="false" data-backdrop="static" data-keyboard="false">
      <dl>
        <form class="modal-form" name="form">
          <div class="modal-dialog">
            <div class="modal-content">
              <div class="heading">
                <h1 ><gdb:localize key="DashboardReferenceLayer.form.heading"/></h1>          
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
                    <strong><gdb:localize key="DashboardThematicLayer.form.nameTheLayer"/></strong>
                  </div>
                  <div class="holder" >
                    <label class="none" >{{layerModel.name}}</label>
                    <span class="text">
                      <input type="text" disabled ng-model="layerModel.name" name="layer.name" required>
                    </span>
                  </div>
                </div>
                
                <layer-label></layer-label>
                
                <layer-types></layer-types>    
                
                <div class="row-holder">
                  <div class="label-holder">
                    <strong><gdb:localize key="DashboardThematicLayer.form.styleTheLayer"/></strong>
                  </div>
                  <div class="holder">
                    <div id="layer-type-styler-container" class="tab-content">
                      <!-- BASICPOINT -->
                      <basic-point></basic-point>
                      
                      <!-- BASICPOLYGON -->
                      <basic-polygon></basic-polygon>
                    </div>                            
                  </div>
                </div>
                
                <legend-options></legend-options>

                <div class="row-holder">
                  <div class="label-holder"></div>
                  <div class="holder">
                    <div class="button-holder">
                      <input type="button" ng-click="ctrl.apply()" value="<gdb:localize key="dashboardViewer.mapIt"/>" class="btn btn-primary"/>
                      <input type="button" ng-click="ctrl.cancel()" value="<gdb:localize key="dashboardViewer.cancel"/>" class="btn btn-default" />
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