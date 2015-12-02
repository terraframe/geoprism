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
<%@ taglib uri="../../WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
       
		<div class="row-holder" style="display:none">
            <div class="label-holder style02">
              <strong><gdb:localize key="DashboardThematicLayer.form.labelsAndValues"/></strong>
            </div>
            <div class="holder">
              <div class="row-holder">
                <div class="check-block">
          	 		<input id="f51" type="checkbox" name="style.enableValue" ng-model="thematicStyleModel.enableValue"></input>
					<label for="f51"><gdb:localize key="DashboardLayer.form.enableValue"/></label>
                </div>
                <div class="check-block">
        	 		<input id="f94" type="checkbox" name="style.enableLabel" ng-model="thematicStyleModel.enableLabel"></input>
                  <label for="f94"><gdb:localize key="DashboardLayer.form.enableLabel"/></label>
                </div>              
              </div>
              <div class="row-holder">
                <div class="cell style02">
                  <label for="f55"><gdb:localize key="DashboardLayer.form.font"/></label>
                  <div class="select-holder">
                  		<select class="font-select" name="style.labelFont" id="f55" 
                  			ng-options="item for item in availableFonts track by item" ng-model="thematicStyleModel.labelFont">
                  		</select>
                  </div>
                </div>
                
                <div class="cell">
                  <label for="f95"><gdb:localize key="DashboardLayer.form.labelSize"/></label>
                  <div class="select-holder">
                  	<select class="size-select" id="f95" name="style.labelSize" ng-model="thematicStyleModel.labelSize" ng-options="n for n in [] | intrange:1:31" ></select>
                  </div>
                </div>
                
                <div class="cell">
                  <span><gdb:localize key="DashboardLayer.form.labelColor"/></span>
                  <div id="label-text-color" class="color-holder">
                    <a href="#" class="color-choice">
                      <span class="ico" style="background:{{thematicStyleModel.labelColor}};">icon</span>
                      <span class="arrow">arrow</span>
                    </a>
                  </div>
                </div>
                
                <div class="cell">
                  <span><gdb:localize key="DashboardLayer.form.labelHalo"/></span>
                  <div id="label-halo-color" class="color-holder">
                    <a href="#" class="color-choice">
                      <span class="ico" style="background:{{thematicStyleModel.labelHalo}};">icon</span>
                      <span class="arrow">arrow</span>
                    </a>
                  </div>
                </div>
                
                
                <div class="cell">
                <label for="f54"><gdb:localize var="dl_form_haloWidth" key="DashboardLayer.form.haloWidth"/>${dl_form_haloWidth}</label>
                  <div class="select-holder">
<!--                     <select ng-init="thematicStyleModel.labelHaloWidth = 2" class="size-select" id="f54" name="style.haloWidth" ng-model="thematicStyleModel.labelHaloWidth" ng-options="n for n in [] | intrange:0:15" ></select> -->
					<select class="size-select" name="style.haloWidth" id="f54"
					  ng-model="thematicStyleModel.labelHaloWidth" ng-options="n for n in [] | intrange:1:16" >
					</select>
                  </div>
                </div>
              </div>
            </div>
          </div>
