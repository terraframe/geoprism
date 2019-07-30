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
<%@ taglib uri="../../WEB-INF/tlds/geoprism.tld" prefix="gdb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

       
<div class="row-holder">
  <div class="label-holder style02">
    <strong><gdb:localize key="DashboardThematicLayer.form.labelsAndValues"/></strong>
  </div>
  <div class="holder">
    <div class="row-holder">
      <styled-check-box ng-if="styleModel.enableValue != null" model="styleModel.enableValue" name="style.enableValue" label="<gdb:localize key="DashboardLayer.form.enableValue"/>"></styled-check-box>
      <styled-check-box model="styleModel.enableLabel" name="style.enableLabel" label="<gdb:localize key="DashboardLayer.form.enableLabel"/>"></styled-check-box>
    </div>
    <div class="row-holder">
      <div class="cell style02">
        <label><gdb:localize key="DashboardLayer.form.font"/></label>
        <styled-basic-select style="true" options="dynamicDataModel.availableFonts" model="styleModel.labelFont" class="font-select"></styled-basic-select>
      </div>
      <div class="cell">
        <label><gdb:localize key="DashboardLayer.form.labelSize"/></label>
        <div class="select-holder">
          <select class="size-select" id="f95" name="style.labelSize" ng-model="styleModel.labelSize" convert-to-number>
            <c:forEach begin="1" end="30" var="size">
              <option value="${size}">${size}</option>
            </c:forEach>          
          </select>
        </div>
      </div>
                
      <div class="cell">
        <span><gdb:localize key="DashboardLayer.form.labelColor"/></span>
        <styled-color-picker model="styleModel.labelColor" scroll="#layer-modal"></styled-color-picker>
      </div>
                
      <div class="cell">
        <span><gdb:localize key="DashboardLayer.form.labelHalo"/></span>
        <styled-color-picker model="styleModel.labelHalo" scroll="#layer-modal"></styled-color-picker> 
      </div>
                
      <div class="cell">
        <label><gdb:localize key="DashboardLayer.form.haloWidth"/></label>
        <div class="select-holder">
          <select class="size-select" name="style.haloWidth" id="f54" ng-model="styleModel.labelHaloWidth" convert-to-number>
            <c:forEach begin="0" end="15" var="size">
              <option value="${size}">${size}</option>
            </c:forEach>            
          </select>          
        </div>        
      </div>
    </div>
  </div>
</div>
