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
var _ = require("lodash");
var uploader_model_1 = require("./uploader-model");
var localization_service_1 = require("../service/localization.service");
var upload_service_1 = require("../service/upload.service");
var navigation_service_1 = require("./navigation.service");
var UploadWizardComponent = (function () {
    function UploadWizardComponent(localizationService, uploadService, navigationService) {
        var _this = this;
        this.localizationService = localizationService;
        this.uploadService = uploadService;
        this.navigationService = navigationService;
        this.onSuccess = new core_1.EventEmitter();
        this.subscription = navigationService.navigationAnnounced$.subscribe(function (direction) {
            if (direction === 'next') {
                _this.next(null, null);
            }
            else if (direction === 'prev') {
                _this.prev();
            }
            else if (direction === 'cancel') {
                _this.cancel();
            }
            else if (direction === 'ready') {
                _this.persist();
            }
        });
    }
    UploadWizardComponent.prototype.ngOnDestroy = function () {
        this.subscription.unsubscribe();
    };
    UploadWizardComponent.prototype.initialize = function (info) {
        this.info = JSON.parse(info);
        this.sheet = this.info.information.sheets[0];
        if (this.sheet.attributes == null) {
            this.sheet.attributes = new uploader_model_1.Locations();
            this.sheet.attributes.ids = [];
            this.sheet.attributes.values = {};
        }
        if (this.sheet.coordinates == null) {
            this.sheet.coordinates = [];
        }
        if (this.info.information.locationExclusions == null) {
            this.info.information.locationExclusions = [];
        }
        if (this.sheet.matches.length > 0) {
            this.page = new uploader_model_1.Page('MATCH-INITIAL', null);
        }
        else {
            this.page = new uploader_model_1.Page('BEGINNING-INFO', null);
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
                for (var j = 0; j < longs.length; j++) {
                    if (label.includes(longs[j])) {
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
            this.page.name = targetPage;
            this.page.snapshot = _.cloneDeep(this.sheet);
            var page = new uploader_model_1.Page(targetPage, this.page);
            page.hasNext = this.hasNextPage(targetPage);
            page.isReady = this.isReady(targetPage);
            this.page = page;
        }
        else {
            this.page.snapshot = _.cloneDeep(this.sheet);
            // Linear logic
            if (this.page.name == 'MATCH-INITIAL') {
                var page = new uploader_model_1.Page('MATCH', this.page);
                page.hasNext = this.hasNextPage('MATCH');
                page.isReady = this.isReady('MATCH');
                this.page = page;
            }
            else if (this.page.name == 'MATCH') {
                var page = new uploader_model_1.Page('BEGINNING-INFO', this.page);
                page.hasNext = this.hasNextPage('BEGINNING-INFO');
                page.isReady = this.isReady('BEGINNING-INFO');
                this.page = page;
            }
            else if (this.page.name == 'BEGINNING-INFO') {
                var page = new uploader_model_1.Page('INITIAL', this.page);
                page.hasNext = this.hasNextPage('INITIAL');
                page.isReady = this.isReady('INITIAL');
                this.page = page;
                this.incrementStep(this.page.name);
            }
            else if (this.page.name === 'INITIAL') {
                var page = new uploader_model_1.Page('FIELDS', this.page);
                page.hasNext = this.hasNextPage('FIELDS');
                page.isReady = this.isReady('FIELDS');
                page.layout = 'wide-holder';
                this.page = page;
            }
            else if (this.page.name === 'FIELDS') {
                if (this.hasLocationField()) {
                    // Go to location attribute page
                    var page = new uploader_model_1.Page('LOCATION', this.page);
                    page.hasNext = this.hasNextPage('LOCATION');
                    page.isReady = this.isReady('LOCATION');
                    this.page = page;
                }
                else if (this.hasCoordinateField()) {
                    // Go to location attribute page
                    var page = new uploader_model_1.Page('COORDINATE', this.page);
                    page.hasNext = this.hasNextPage('COORDINATE');
                    page.isReady = this.isReady('COORDINATE');
                    this.page = page;
                }
                else {
                    // Go to summary page
                    var page = new uploader_model_1.Page('SUMMARY', this.page);
                    page.hasNext = this.hasNextPage('SUMMARY');
                    page.isReady = this.isReady('SUMMARY');
                    this.page = page;
                }
                this.incrementStep(this.page.name);
            }
            else if (this.page.name === 'LOCATION') {
                if (this.hasCoordinateField()) {
                    // Go to coordinate page
                    var page = new uploader_model_1.Page('COORDINATE', this.page);
                    page.hasNext = this.hasNextPage('COORDINATE');
                    page.isReady = this.isReady('COORDINATE');
                    this.page = page;
                }
                else {
                    // Go to summary page
                    var page = new uploader_model_1.Page('SUMMARY', this.page);
                    page.hasNext = this.hasNextPage('SUMMARY');
                    page.isReady = this.isReady('SUMMARY');
                    this.page = page;
                }
            }
            else if (this.page.name === 'COORDINATE') {
                // Go to summary page
                var page = new uploader_model_1.Page('SUMMARY', this.page);
                page.hasNext = this.hasNextPage('SUMMARY');
                page.isReady = this.isReady('SUMMARY');
                this.page = page;
                this.incrementStep(this.page.name);
            }
            else if (this.page.name === 'GEO-VALIDATION') {
                // Go to summary page
                var page = new uploader_model_1.Page('CATEGORY-VALIDATION', this.page);
                page.hasNext = this.hasNextPage('CATEGORY-VALIDATION');
                page.isReady = this.isReady('CATEGORY-VALIDATION');
                page.layout = 'wide-holder';
                this.page = page;
                this.incrementStep(this.page.name);
            }
        }
    };
    UploadWizardComponent.prototype.prev = function () {
        this.pageDirection = "PREVIOUS";
        if (this.page.name === 'MATCH' || this.page.name === "SUMMARY" || this.page.name === "BEGINNING-INFO" || this.page.name === "CATEGORY-VALIDATION") {
            this.handlePrev();
        }
        else if (confirm(this.localizationService.localize("dataUploader", "prevDialogContent"))) {
            this.handlePrev();
        }
    };
    UploadWizardComponent.prototype.handlePrev = function () {
        if (this.page.prev != null) {
            this.page = this.page.prev;
            this.sheet = this.page.snapshot;
            this.page.snapshot = null;
        }
        if (this.page.name === "SUMMARY" || this.page.name === 'CATEGORY-VALIDATION') {
            var stepCt = 4;
            if (!this.hasCoordinateField()) {
                stepCt = stepCt - 1;
            }
            if (!this.hasLocationField()) {
                stepCt = stepCt - 1;
            }
            if (this.page.name === 'CATEGORY-VALIDATION') {
                stepCt = stepCt - 1;
            }
            this.currentStep = stepCt;
        }
        else if (this.page.name === "COORDINATE") {
            var stepCt = 3;
            if (!this.hasLocationField()) {
                stepCt = stepCt - 1;
            }
            this.currentStep = stepCt;
        }
        else if (this.page.name === "LOCATION") {
            this.currentStep = 2;
        }
        else if (this.page.name === "FIELDS") {
            this.currentStep = 1;
        }
        else if (this.page.name === "INITIAL") {
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
    UploadWizardComponent.prototype.persist = function () {
        var _this = this;
        this.info.information.sheets[0] = _.cloneDeep(this.sheet);
        this.uploadService.importData(this.info.information)
            .then(function (result) {
            if (result.success) {
                _this.clear();
                _this.onSuccess.emit({ datasets: result.datasets, finished: true });
            }
            else {
                if (_this.hasLocationField() && _this.hasCoordinateField()) {
                    _this.currentStep = 5;
                }
                else if (_this.hasLocationField() || _this.hasCoordinateField()) {
                    _this.currentStep = 4;
                }
                else {
                    _this.currentStep = 3;
                }
                if (!result.problems.locations || result.problems.locations.length > 0) {
                    var page = new uploader_model_1.Page('GEO-VALIDATION', null);
                    page.hasNext = _this.hasNextPage('CATEGORY-VALIDATION');
                    page.isReady = _this.isReady('CATEGORY-VALIDATION');
                    page.layout = 'wide-holder';
                    _this.page = page;
                }
                else {
                    var page = new uploader_model_1.Page('CATEGORY-VALIDATION', null);
                    page.hasNext = false;
                    page.isReady = true;
                    page.layout = 'wide-holder';
                    _this.page = page;
                }
                _this.problems = result.problems;
                _this.onSuccess.emit({ datasets: result.datasets, finished: false });
            }
        });
    };
    UploadWizardComponent.prototype.isReady = function (name) {
        return (name === 'SUMMARY' || name === 'CATEGORY-VALIDATION' || (name === 'GEO-VALIDATION' && this.problems.categories !== null && this.problems.categories.length === 0));
    };
    UploadWizardComponent.prototype.hasNextPage = function (name) {
        if (name == 'GEO-VALIDATION') {
            return (this.problems.categories !== null && this.problems.categories.length > 0);
        }
        return (name !== 'MATCH-INITIAL' && name !== 'SUMMARY' && name !== 'MATCH' && name !== 'CATEGORY-VALIDATION');
    };
    UploadWizardComponent.prototype.onNextPage = function (data) {
        this.next(data.targetPage, data.sourcePage);
    };
    UploadWizardComponent.prototype.onSelectSheet = function (sheet) {
        this.page.snapshot = _.cloneDeep(this.sheet);
        // Go to summary page
        var page = new uploader_model_1.Page('SUMMARY', this.page);
        page.hasNext = this.hasNextPage('SUMMARY');
        page.isReady = this.isReady('SUMMARY');
        this.page = page;
        this.sheet = sheet;
    };
    UploadWizardComponent.prototype.showStep = function () {
        var names = ['MATCH-INITIAL', 'MATCH', 'GEO-VALIDATION', 'CATEGORY-VALIDATION'];
        return this.page && (names.indexOf(this.page.name) === -1);
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
        upload_service_1.UploadService,
        navigation_service_1.NavigationService])
], UploadWizardComponent);
exports.UploadWizardComponent = UploadWizardComponent;
//# sourceMappingURL=upload-wizard.component.js.map