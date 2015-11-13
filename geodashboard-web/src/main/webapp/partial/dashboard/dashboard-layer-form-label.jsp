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
<%@ taglib uri="../../WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="../../WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://jawr.net/tags" prefix="jwr" %>
       
		<div class="row-holder">
            <div class="label-holder style02">
              <strong><gdb:localize var="dl_form_labelsAndValues" key="DashboardThematicLayer.form.labelsAndValues"/>${dl_form_labelsAndValues}</strong>
            </div>
            <div class="holder">
              <div class="row-holder">
                <div class="check-block">
          	 		<input id="f51" type="checkbox" name="style.enableValue" checked></input>
					<label for="f51"><gdb:localize var="dl_form_enableVal" key="DashboardLayer.form.enableValue"/>${dl_form_enableVal}</label>
                  <mjl:messages attribute="style.enableVal" classes="error-message">
                    <mjl:message />
                  </mjl:messages>
                </div>
                <div class="check-block">
        	 		<input id="f94" type="checkbox" name="style.enableLabel" checked></input>
                  <label for="f94"><gdb:localize var="dl_form_enableLabel" key="DashboardLayer.form.enableLabel"/>${dl_form_enableLabel}</label>
                </div>              
              </div>
              <div class="row-holder">
                <div class="cell style02">
                  <label for="f55"><gdb:localize var="dl_form_font" key="DashboardLayer.form.font"/>${dl_form_font}</label>
                  <div class="select-holder">
                  		<select class="font-select" name="style.labelFont" id="f55" 
                  			ng-options="item as item.value for item in availableFonts track by item.value" ng-model="thematicStyleModel.labelFont"
                  			ng-init="thematicStyleModel.labelFont = availableFonts[0]">
                  		</select>
                  </div>
                </div>
                
                
                <div class="cell">
                  <label for="f95"><gdb:localize var="dl_form_labelSize" key="DashboardLayer.form.labelSize"/>${dl_form_labelSize}</label>
                  <div class="select-holder">
                  
                  	<select ng-init="thematicStyleModel.labelSize = 12" class="size-select" id="f95" name="style.labelSize" ng-model="thematicStyleModel.labelSize" ng-options="n for n in [] | range:1:31" ></select>
                  
<!--                     <select class="size-select" id="f95" name="style.labelSize" ng-model="thematicStyleModel.labelSize" ng-init="thematicStyleModel.labelSize = 12"> -->
<%--                       <c:forEach begin="0" end="30" var="size"> --%>
<%--                         <c:choose> --%>
<%--                           <c:when test="{{thematicStyleModel.labelSize}} == ${size}"> --%>
<%--                             <option selected="selected" value=${size}>${size}</option> --%>
<%--                           </c:when> --%>
<%--                           <c:otherwise> --%>
<%--                             <option value="${size}">${size}</option> --%>
<%--                           </c:otherwise> --%>
<%--                         </c:choose> --%>
<%--                       </c:forEach> --%>
<!--                     </select> -->
                    
                  </div>
                </div>
                
                
                <div class="cell">
                  <span><gdb:localize var="dl_form_labelColor" key="DashboardLayer.form.labelColor"/>${dl_form_labelColor}</span>
                  <div class="color-holder">
                    <a href="#" class="color-choice">
                      <span class="ico" style="background:{{thematicStyleModel.labelColor}};">icon</span>
                      <span class="arrow">arrow</span>
                      <input type="hidden" class="color-input" name="style.labelColor" value="{{thematicStyleModel.labelColor}}" ng-model="thematicStyleModel.labelColor" />
                    </a>
                  </div>
                </div>
                
                <div class="cell">
                  <span><gdb:localize var="dl_form_labelHalo" key="DashboardLayer.form.labelHalo"/>${dl_form_labelHalo}</span>
                  <div class="color-holder">
                    <a href="#" class="color-choice">
                      <span class="ico" style="background:{{thematicStyleModel.labelHalo}};">icon</span>
                      <span class="arrow">arrow</span>
                      <input type="hidden" class="color-input" name="style.labelHalo" value="{{thematicStyleModel.labelHalo}}" ng-model="thematicStyleModel.labelHalo"/>
                    </a>
                  </div>
                </div>
                
                
                <div class="cell">
                <label for="f54"><gdb:localize var="dl_form_haloWidth" key="DashboardLayer.form.haloWidth"/>${dl_form_haloWidth}</label>
                  <div class="select-holder">
                    
<!--                     <select ng-init="thematicStyleModel.labelHaloWidth = 2" class="size-select" id="f54" name="style.haloWidth" ng-model="thematicStyleModel.labelHaloWidth" ng-options="n for n in [] | range:0:15" ></select> -->
                    
                    <select class="size-select" name="style.haloWidth" id="f54" ng-model="thematicStyleModel.labelHaloWidth" >
                      <c:forEach begin="0" end="15" var="size">
                        <c:choose>
                          <c:when test="{{thematicStyleModel.labelHaloWidth}} == ${size}">
                            <option selected="selected" value="${size}">${size}</option>
                          </c:when>
                          <c:otherwise>
                            <option value="${size}">${size}</option>
                          </c:otherwise>
                        </c:choose>
                      </c:forEach>
                    </select>
                    
                  </div>
                </div>
              </div>
            </div>
          </div>
