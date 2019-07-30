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
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<gdb:localize var="cloneLabel" key="dashboard.clone.label"/>
<gdb:localize var="dashboardLabel" key="dashboard.label"/>

<div id="dashboard-dialog" class="modal-content" title="${cloneLabel} ${dashboard.displayLabel}">
  <form class="submit-form clone-form" action="#">
    <input id="clone-dashboard-id" type="hidden" value="${dashboard.id}">
    <fieldset>
      <section class="form-container">
        <div class="field-row clearfix" id="clone-label-field-row" >
          <label for="clone-label">${dashboardLabel} *</label>
          <input id="clone-label" type="text" placeholder="${dashboardLabel}">
          <div class="error-message" id="clone-label-error" ></div>
        </div>
      </section>
    </fieldset>
  </form>
</div>


