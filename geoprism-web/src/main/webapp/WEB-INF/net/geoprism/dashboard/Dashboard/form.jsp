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
<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<c:if test="${errorMessage != null || errorMessageArray != null}">
	<div class="row-holder">
		<div class="label-holder"></div>
		<div class="holder">
			<%@include file="../../../../templates/inlineError.jsp"%>
		</div>
	</div>
</c:if>

<div id="Dashboard-mainDiv" class="modal-dialog">
	<div class="modal-content">
		<div class="heading">
<!-- 			<h1>Create a new Dashboard</h1> -->
			<h1><gdb:localize var="dashboard_form_heading" key="dashboard.form.heading"/>${dashboard_form_heading}</h1>
		</div>

		<fieldset>
			<div class="row-holder">
				<mjl:component param="dto" item="${dashboard}">
					<div class="label-holder">
						<label class="none" for="f312">${dashboard.displayLabelMd.displayLabel}</label>
					</div>
				</mjl:component>

				<div class="holder">
					<div class="row-holder">
						<mjl:component param="dto" item="${dashboard}">
							<span class="text"> <input type="text" id="dto.displayLabel"
								value="${dashboard.displayLabel.value}" name="dto.displayLabel" />
								<mjl:messages attribute="displayLabel" classes="error-message">
									<mjl:message />
								</mjl:messages>
							</span>
						</mjl:component>
					</div>
					
					<div class="button-holder">
						<mjl:command
							name="net.geoprism.dashboard.Dashboard.form.create.button"
							value="Create New"
							action="net.geoprism.dashboard.DashboardController.create.mojo"
							classes="btn btn-primary" />
						<mjl:command
							name="net.geoprism.dashboard.Dashboard.form.cancel.button"
							value="Cancel"
							action="net.geoprism.dashboard.DashboardController.cancel.mojo"
							classes="btn btn-default" />
					</div>
				</div>
			</div>
		</fieldset>
	</div>


</div>


