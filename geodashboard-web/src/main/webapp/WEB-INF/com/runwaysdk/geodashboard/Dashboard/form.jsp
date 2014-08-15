<%@ taglib uri="/WEB-INF/tlds/runwayLib.tld" prefix="mjl"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/geodashboard.tld" prefix="gdb"%>

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
							name="com.runwaysdk.geodashboard.Dashboard.form.create.button"
							value="Create New"
							action="com.runwaysdk.geodashboard.DashboardController.create.mojo"
							classes="btn btn-primary" />
						<mjl:command
							name="com.runwaysdk.geodashboard.Dashboard.form.cancel.button"
							value="Cancel"
							action="com.runwaysdk.geodashboard.DashboardController.cancel.mojo"
							classes="btn btn-default" />
					</div>
				</div>
			</div>
		</fieldset>
	</div>


</div>


