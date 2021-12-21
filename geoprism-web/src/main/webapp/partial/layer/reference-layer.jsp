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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
  <!-- ngIf cannot be on the root node or processing doesn't work -->
  <div ng-if="show">
    <div class="modal-backdrop fade in"></div>
    <div id="layer-modal" style="display: block;" class="modal fade in" role="dialog" aria-hidden="false" data-backdrop="static" data-keyboard="false">
      <dl>
        <form class="modal-form" name="form">
          <div class="modal-dialog">
            <div class="modal-content" style="display: none;" show-on-ready>
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
                
                <layer-name-input ng-if="!layerModel.newInstance" layer-model="layerModel" disabled="true"></layer-name-input>
                
                <div ng-if="layerModel.newInstance" class="row-holder">
                  <div class="label-holder">
                    <strong><gdb:localize key="DashboardReferenceLayer.form.nameTheLayer"/></strong>
                  </div>
                  <div class="holder">
                    <div class="universal-select-container">
                      <select class="universal-select" ng-options="opt.value as opt.label for opt in dynamicDataModel.universals" ng-model="layerModel.universalId"></select>
                    </div>
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
                
                <form-action-buttons persist="ctrl.apply()" cancel="ctrl.cancel()"></form-action-buttons>
              </fieldset>
            </div>
          </div>
        </form>
      </dl>
    </div>      
  </div>
</div>