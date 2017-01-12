///
/// Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
///
/// This file is part of Runway SDK(tm).
///
/// Runway SDK(tm) is free software: you can redistribute it and/or modify
/// it under the terms of the GNU Lesser General Public License as
/// published by the Free Software Foundation, either version 3 of the
/// License, or (at your option) any later version.
///
/// Runway SDK(tm) is distributed in the hope that it will be useful, but
/// WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU Lesser General Public License for more details.
///
/// You should have received a copy of the GNU Lesser General Public
/// License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
///
"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var uploader_model_1 = require("./uploader-model");
var localization_service_1 = require("../service/localization.service");
var upload_service_1 = require("../service/upload.service");
var UploadWizardComponent = (function () {
    function UploadWizardComponent(localizationService, uploadService) {
        this.localizationService = localizationService;
        this.uploadService = uploadService;
        this.onSuccess = new core_1.EventEmitter();
    }
    UploadWizardComponent.prototype.initialize = function (info) {
        this.info = JSON.parse(info);
        this.sheet = this.info.information.sheets[0];
        if (this.sheet.matches.length > 0) {
            this.page = new uploader_model_1.Page('MATCH-INITIAL');
        }
        else {
            this.page = new uploader_model_1.Page('BEGINNING-INFO');
        }
        this.initializeAttributes();
        this.refreshSteps();
    };
    UploadWizardComponent.prototype.initializeAttributes = function () {
        var fields = this.sheet.fields;
        var lats = [
            this.localizationService.localize("dataUploader", "attributeLatAbbreviation").toLowerCase(),
            this.localizationService.localize("dataUploader", "attributeLatitudeName").toLowerCase()
        ];
        var longs = [
            this.localizationService.localize("dataUploader", "attributeLngAbbreviation").toLowerCase(),
            this.localizationService.localize("dataUploader", "attributeLongAbbreviation").toLowerCase(),
            this.localizationService.localize("dataUploader", "attributeLongitudeName").toLowerCase()
        ];
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (field.columnType === "NUMBER") {
                var label = field.label.toLowerCase();
                for (var j = 0; j < lats.length; j++) {
                    if (label.includes(lats[j])) {
                        field.type = 'LATITUDE';
                    }
                }
                for (var j = 0; j < lats.length; j++) {
                    if (label.includes(lats[j])) {
                        field.type = 'LONGITUDE';
                    }
                }
            }
        }
    };
    UploadWizardComponent.prototype.refreshSteps = function () {
        this.steps = new Array();
        this.steps.push(new uploader_model_1.Step("1", "INITIAL"));
        this.steps.push(new uploader_model_1.Step("2", "FIELDS"));
        this.steps.push(new uploader_model_1.Step("3", "SUMMARY"));
        var locationStep = new uploader_model_1.Step("4", "LOCATION");
        var coordinateStep = new uploader_model_1.Step("5", "COORDINATE");
        var geoProblemResStep = new uploader_model_1.Step("6", "GEO-VALIDATION");
        var categoryProblemResStep = new uploader_model_1.Step("7", "CATEGORY-VALIDATION");
        var hasLocationField = this.hasLocationField();
        var hasCoordinateField = this.hasCoordinateField();
        var hasCategoryField = this.hasCategoryField();
        if (hasLocationField && hasCoordinateField && hasCategoryField) {
            this.steps.splice(2, 0, locationStep, coordinateStep);
            this.steps.splice(5, 0, geoProblemResStep);
            this.steps.splice(6, 0, categoryProblemResStep);
        }
        else if (!hasLocationField && hasCoordinateField && hasCategoryField) {
            this.steps.splice(2, 0, coordinateStep);
            this.steps.splice(4, 0, geoProblemResStep);
            this.steps.splice(5, 0, categoryProblemResStep);
        }
        else if (hasLocationField && !hasCoordinateField && hasCategoryField) {
            this.steps.splice(2, 0, locationStep);
            this.steps.splice(4, 0, geoProblemResStep);
            this.steps.splice(5, 0, categoryProblemResStep);
        }
        else if (hasLocationField && hasCoordinateField && !hasCategoryField) {
            this.steps.splice(2, 0, locationStep, coordinateStep);
            this.steps.splice(5, 0, geoProblemResStep);
        }
        else if (hasLocationField) {
            this.steps.splice(2, 0, locationStep);
            this.steps.splice(4, 0, geoProblemResStep);
        }
        else if (hasCoordinateField) {
            this.steps.splice(2, 0, coordinateStep);
            this.steps.splice(4, 0, geoProblemResStep);
        }
        else if (hasCategoryField) {
            this.steps.splice(3, 0, categoryProblemResStep);
        }
    };
    UploadWizardComponent.prototype.hasLocationField = function () {
        for (var i = 0; i < this.sheet.fields.length; i++) {
            var field = this.sheet.fields[i];
            if (field.type == 'LOCATION') {
                return true;
            }
        }
        return false;
    };
    UploadWizardComponent.prototype.hasCoordinateField = function () {
        for (var i = 0; i < this.sheet.fields.length; i++) {
            var field = this.sheet.fields[i];
            if (field.type == 'LONGITUDE' || field.type == 'LATITUDE') {
                return true;
            }
        }
        return false;
    };
    UploadWizardComponent.prototype.hasCategoryField = function () {
        for (var i = 0; i < this.sheet.fields.length; i++) {
            var field = this.sheet.fields[i];
            if (field.type == 'CATEGORY') {
                return true;
            }
        }
        return false;
    };
    UploadWizardComponent.prototype.incrementStep = function (targetPage) {
        if (targetPage === 'MATCH-INITIAL') {
            this.currentStep = -1;
        }
        else if (targetPage == 'MATCH') {
            this.currentStep = -1;
        }
        else if (targetPage == 'BEGINNING-INFO') {
            this.currentStep = -1;
        }
        else if (targetPage == 'INITIAL') {
            this.currentStep = 1;
        }
        else if (targetPage === 'FIELDS') {
            this.currentStep = 2;
        }
        else if (targetPage === 'LOCATION') {
            this.currentStep = 3;
        }
        else if (targetPage === 'COORDINATE') {
            if (this.hasLocationField()) {
                this.currentStep = 4;
            }
            else {
                this.currentStep = 3;
            }
        }
        else if (targetPage === 'GEO-VALIDATION') {
            if (this.hasLocationField() && this.hasCoordinateField()) {
                this.currentStep = 5;
            }
            else if (this.hasLocationField()) {
                this.currentStep = 4;
            }
            else if (this.hasCoordinateField()) {
                this.currentStep = 4;
            }
            else {
                this.currentStep = 3;
            }
        }
    };
    /**
     * @param targetPage <optional>
     * @param sourcePage <optional>
     */
    UploadWizardComponent.prototype.next = function (targetPage, sourcePage) {
        this.pageDirection = "NEXT";
        if (targetPage && sourcePage) {
            this.page.current = targetPage;
            var snapshot = new uploader_model_1.Snapshot(sourcePage, Object.assign(new uploader_model_1.Sheet(), this.sheet));
            this.page.snapshots.push(snapshot);
        }
        else {
            // Linear logic
            if (this.page.current == 'MATCH-INITIAL') {
                this.page.current = 'MATCH';
                var snapshot = new uploader_model_1.Snapshot('MATCH-INITIAL', Object.assign(new uploader_model_1.Sheet(), this.sheet));
                this.page.snapshots.push(snapshot);
            }
            else if (this.page.current == 'MATCH') {
                this.page.current = 'BEGINNING-INFO';
                var snapshot = new uploader_model_1.Snapshot('MATCH', Object.assign(new uploader_model_1.Sheet(), this.sheet));
                this.page.snapshots.push(snapshot);
            }
            else if (this.page.current == 'BEGINNING-INFO') {
                this.page.current = 'INITIAL';
                this.incrementStep(this.page.current);
            }
            else if (this.page.current === 'INITIAL') {
                // Go to fields page  
                this.page.current = 'FIELDS';
                var snapshot = new uploader_model_1.Snapshot('INITIAL', Object.assign(new uploader_model_1.Sheet(), this.sheet));
                this.page.snapshots.push(snapshot);
            }
            else if (this.page.current === 'FIELDS') {
                if (this.hasLocationField()) {
                    // Go to location attribute page
                    this.page.current = 'LOCATION';
                }
                else if (this.hasCoordinateField()) {
                    // Go to coordinate page
                    this.page.current = 'COORDINATE';
                }
                else {
                    // Go to summary page
                    this.page.current = 'SUMMARY';
                }
                this.incrementStep(this.page.current);
                var snapshot = new uploader_model_1.Snapshot('FIELDS', Object.assign(new uploader_model_1.Sheet(), this.sheet));
                this.page.snapshots.push(snapshot);
            }
            else if (this.page.current === 'LOCATION') {
                if (this.hasCoordinateField()) {
                    // Go to coordinate page
                    this.page.current = 'COORDINATE';
                }
                else {
                    // Go to summary page
                    this.page.current = 'SUMMARY';
                }
                this.incrementStep(this.page.current);
                var snapshot = new uploader_model_1.Snapshot('LOCATION', Object.assign(new uploader_model_1.Sheet(), this.sheet));
                this.page.snapshots.push(snapshot);
            }
            else if (this.page.current === 'COORDINATE') {
                // Go to summary page
                this.page.current = 'SUMMARY';
                this.incrementStep(this.page.current);
                var snapshot = new uploader_model_1.Snapshot('COORDINATE', Object.assign(new uploader_model_1.Sheet(), this.sheet));
                this.page.snapshots.push(snapshot);
            }
            else if (this.page.current === 'GEO-VALIDATION') {
                this.page.current = 'CATEGORY-VALIDATION';
                this.incrementStep(this.page.current);
                var snapshot = new uploader_model_1.Snapshot('GEO-VALIDATION', Object.assign(new uploader_model_1.Sheet(), this.sheet));
                this.page.snapshots.push(snapshot);
            }
        }
    };
    UploadWizardComponent.prototype.prev = function () {
        this.pageDirection = "PREVIOUS";
        if (this.page.current === 'MATCH' || this.page.current === "SUMMARY" || this.page.current === "BEGINNING-INFO" || this.page.current === "CATEGORY-VALIDATION") {
            this.handlePrev();
        }
        else if (confirm(this.localizationService.localize("dataUploader", "prevDialogContent"))) {
            this.handlePrev();
        }
    };
    UploadWizardComponent.prototype.handlePrev = function () {
        if (this.page.snapshots.length > 0) {
            if (this.page.current === 'INITIAL') {
                this.page.current = 'BEGINNING-INFO';
            }
            else {
                var snapshot = this.page.snapshots.pop();
                this.page.current = snapshot.page;
                this.sheet = snapshot.sheet;
            }
        }
        if (this.page.current === "SUMMARY" || this.page.current === 'CATEGORY-VALIDATION') {
            var stepCt = 4;
            if (!this.hasCoordinateField()) {
                stepCt = stepCt - 1;
            }
            if (!this.hasLocationField()) {
                stepCt = stepCt - 1;
            }
            if (this.page.current === 'CATEGORY-VALIDATION') {
                stepCt = stepCt - 1;
            }
            this.currentStep = stepCt;
        }
        else if (this.page.current === "COORDINATE") {
            var stepCt = 3;
            if (!this.hasLocationField()) {
                stepCt = stepCt - 1;
            }
            this.currentStep = stepCt;
        }
        else if (this.page.current === "LOCATION") {
            this.currentStep = 2;
        }
        else if (this.page.current === "FIELDS") {
            this.currentStep = 1;
        }
        else if (this.page.current === "INITIAL") {
            this.currentStep = 0;
        }
    };
    UploadWizardComponent.prototype.onSubmit = function () {
        this.onSuccess.emit();
        this.clear();
    };
    UploadWizardComponent.prototype.cancel = function () {
        var _this = this;
        this.uploadService.cancelImport(this.info.information)
            .then(function (response) {
            _this.clear();
        });
    };
    UploadWizardComponent.prototype.clear = function () {
        this.steps = null;
        this.info = null;
        this.sheet = null;
        this.page = null;
        this.pageDirection = null;
        this.currentStep = null;
    };
    UploadWizardComponent.prototype.isReady = function () {
        var current = this.page.current;
        // TODO   return (current === 'SUMMARY' || current === 'CATEGORY-VALIDATION' || (current === 'GEO-VALIDATION' && this.problems.categories !== null && this.problems.categories.length === 0));
        return (current === 'SUMMARY' || current === 'CATEGORY-VALIDATION');
    };
    UploadWizardComponent.prototype.hasNextPage = function () {
        var current = this.page.current;
        //
        //    if(current == 'GEO-VALIDATION') {
        //      return (this.problems.categories !== null && this.problems.categories.length > 0);
        //    }
        return (current !== 'MATCH-INITIAL' && current !== 'SUMMARY' && current !== 'MATCH' && current !== 'CATEGORY-VALIDATION');
    };
    UploadWizardComponent.prototype.onNextPage = function (data) {
        this.next(data.targetPage, data.sourcePage);
    };
    return UploadWizardComponent;
}());
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], UploadWizardComponent.prototype, "onSuccess", void 0);
UploadWizardComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'upload-wizard',
        templateUrl: 'upload-wizard.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [localization_service_1.LocalizationService,
        upload_service_1.UploadService])
], UploadWizardComponent);
exports.UploadWizardComponent = UploadWizardComponent;
//# sourceMappingURL=upload-wizard.component.js.map