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

<div class="fill-block">
  <strong class="title"><gdb:localize key="DashboardThematicLayer.form.fill"/></strong>
  <div class="cell-holder">
    <div class="cell">
      <span><gdb:localize key="DashboardLayer.form.color"/></span>
      <div class="color-holder">
        <a href="#" class="color-choice" color-picker model="fill" element='#modal01'>
          <span class="ico" style="background:{{fill}}">icon</span>
          <span class="arrow">arrow</span>
        </a>
      </div>
    </div>
    <div class="cell">
      <label><gdb:localize key="DashboardLayer.form.opacity"/></label>
      <div class="text">
        <select class="tab-select" ng-model="opacity" convert-to-number>
          <c:forEach step="5" begin="0" end="100" var="size">
            <fmt:formatNumber value="${size/100}" maxFractionDigits="2" type="number" var="potentialValue"/>                              
            <option value="${potentialValue}">${size}</option>
          </c:forEach>        
        </select>
      </div>
    </div>
  </div>
</div>

