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
		<div id="modal-div" style="display: block;" class="modal fade in" role="dialog" aria-hidden="false" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content" show-on-ready>
					<div class="modal-body">
						<form class="modal-form" name="ctrl.form">
							<div class="heading">
								<h1 ng-show="entity.id">
									<gdb:localize key="location.management.synonymModalHeader" />
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

								<!-- 
              <div class="row-holder" ng-repeat="synonym in data.syonynms">
                <div class="label-holder">
                  <label><gdb:localize key="location.management.synonymLabel"/></label>
                </div>    
                <div class="holder">
                  <span class="text">
                    <input type="text" ng-model="synonym.displayLabel" name="label" required="required" placeholder="<gdb:localize key="location.management.labelPlaceholder"/>">
                  </span>
                </div>
              </div>
              -->
								<div class="label-holder"></div>
								<div class="holder">
									<table id="manage-synonyms-table" class="list-table table table-bordered table-striped">
										<tbody>
											<tr ng-repeat="synonym in synonyms" class="fade-ngRepeat-item" ng-cloak>
												<td class="button-column"><a class="fa fa-trash-o ico-remove" ng-click="ctrl.removeSynonym(synonym)"
													title="<gdb:localize key="location.management.removeSynonymTooltip"/>"></a></td>
												<td class="label-column"><input type="text" ng-model="synonym.displayLabel" name="label" required="required"
													placeholder="<gdb:localize key="location.management.synonymLabelPlaceholder"/>"></td>
											</tr>
											<tr>
												<td class="button-column"><a class="fa fa-plus" ng-click="ctrl.newSynonym()"
													title="<gdb:localize key="location.management.createSynonymTooltip"/>"></a></td>
												<td ng-show="data.synonyms.size > 0" class="submit-form">
													<form name="ctrl.form">
														<input class="list-table-input" type="text" ng-model="synonym.displayLabel" press-esc="ctrl.cancel()" press-enter="ctrl.apply()"
															focus-on-show></input>
													</form>
												</td>
											</tr>
										</tbody>
									</table>
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
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
