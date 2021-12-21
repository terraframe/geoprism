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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<div>
	<div ng-if="show">
		<div class="modal-backdrop fade in"></div>
		<div id="location-manager-modal" style="display: block;" class="modal fade in" role="dialog" aria-hidden="false" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content" show-on-ready>
					<div class="modal-body">
						<form class="modal-form" name="ctrl.form">

							<div class="heading">
								<h1 ng-show="!entity.oid">
									<gdb:localize key="location.management.newTooltip" />
								</h1>
								<h1 ng-show="entity.oid">
									<gdb:localize key="location.management.editTooltip" />
								</h1>
							</div>
							<fieldset>
								<div class="row-holder" ng-show="errors.length > 0 && show">
									<div class="label-holder"></div>
									<div class="holder">
										<div class="alert alertbox" ng-repeat="error in errors track by $index">
											<p class="error-message">{{error}}</p>
										</div>
									</div>
								</div>

								<div class="row-holder">
									<div class="label-holder">
										<label><gdb:localize key="location.management.label" /></label>
									</div>
									<div class="holder">
										<span class="text"> <input type="text" ng-model="entity.displayLabel" name="label" required="required"
											placeholder="<gdb:localize key="location.management.labelPlaceholder"/>">
										</span>
									</div>
								</div>
								<div class="row-holder">
									<div class="label-holder">
										<label><gdb:localize key="location.management.geoId" /></label>
									</div>
									<div class="holder">
										<span class="text"> <input type="text" ng-model="entity.geoId" name="geoId"
											placeholder="<gdb:localize key="location.management.geoIdPlaceholder"/>">
										</span>
									</div>
								</div>
								<div class="row-holder">
									<div class="label-holder">
										<label><gdb:localize key="location.management.universal" /></label>
									</div>
									<div class="holder">
										<div class="select-box">
											<select class="method-select" ng-model="entity.universal" ng-options="opt.oid as opt.displayLabel for opt in universals"
												required="required">
												<option value=""></option>
											</select>
										</div>
									</div>
								</div>
								<div class="row-holder" fire-on-ready>
									<div class="label-holder"></div>
									<div class="holder">
										<div class="button-holder">
											<input type="button" value="<gdb:localize key="dataset.cancel"/>" class="btn btn-default" ng-click="ctrl.cancel()" /> <input type="button"
												value="<gdb:localize key="dataset.submit"/>" class="btn btn-primary" ng-click="ctrl.apply()" ng-disabled="ctrl.form.$invalid" />
										</div>
									</div>
								</div>
							</fieldset>
							<!-- 	         </div> -->
					</div>
				</div>
				</form>
			</div>
		</div>
	</div>
</div>
