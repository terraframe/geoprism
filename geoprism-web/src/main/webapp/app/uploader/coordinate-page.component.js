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
var CoordinatePageComponent = (function () {
    function CoordinatePageComponent(localizationService) {
        this.localizationService = localizationService;
        this.longitudes = [];
        this.featureLabels = [];
        this.locations = [];
        this.featureIds = [];
        this.labels = {};
    }
    CoordinatePageComponent.prototype.ngOnInit = function () {
        var countries = this.info.options.countries;
        for (var i = 0; i < countries.length; i++) {
            var country = countries[i];
            this.universals = country.options;
            if (country.value == this.sheet.country) {
                for (var j = 0; j < country.options.length; j++) {
                    var universal = country.options[j];
                    this.labels[universal.value] = universal.label;
                }
            }
        }
        for (var i = 0; i < this.sheet.fields.length; i++) {
            var field = this.sheet.fields[i];
            if (field.type == 'LATITUDE') {
                if (!this.hasCoordinateField(field)) {
                    var coordinate = new uploader_model_1.CoordinateAttribute();
                    coordinate.label = "";
                    coordinate.latitude = field.label;
                    coordinate.longitude = this.getSuggestedLongitude(field);
                    coordinate.featureLabel = "";
                    coordinate.location = "";
                    coordinate.featureId = "";
                    coordinate.id = field.label;
                    this.sheet.coordinates.push(coordinate);
                }
            }
            else if (field.type == 'LONGITUDE') {
                this.longitudes.push(field);
            }
            else if (field.type == 'TEXT') {
                this.featureLabels.push(field);
            }
            else if (this.isBasic(field)) {
                this.featureIds.push(field);
            }
        }
        /*
         * If there is only 1 longitude field then set that value
         * automatically and don't give the user a drop-down that
         * they need to select from
         */
        if (this.longitudes.length == 1) {
            for (var i = 0; i < this.sheet.coordinates.length; i++) {
                var coordinate = this.sheet.coordinates[i];
                coordinate.longitude = this.longitudes[0].label;
            }
        }
        if (this.sheet.attributes != null) {
            for (var i = 0; i < this.sheet.attributes.ids.length; i++) {
                var id = this.sheet.attributes.ids[i];
                var attribute = this.sheet.attributes.values[id];
                this.locations.push({
                    label: attribute.label,
                    universal: attribute.universal
                });
            }
        }
    };
    CoordinatePageComponent.prototype.getSuggestedLongitude = function (targetField) {
        var fields = this.sheet.fields;
        var trackingPosition = null;
        var mostLikelyLongitudeField = null;
        var label = targetField.label.toLowerCase();
        var labels = [
            this.localizationService.localize("dataUploader", "attributeLatAbbreviation").toLowerCase(),
            this.localizationService.localize("dataUploader", "attributeLatitudeName").toLowerCase(),
            this.localizationService.localize("dataUploader", "attributeLngAbbreviation").toLowerCase(),
            this.localizationService.localize("dataUploader", "attributeLongAbbreviation").toLowerCase(),
            this.localizationService.localize("dataUploader", "attributeLongitudeName").toLowerCase()
        ];
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (field.type === "LATITUDE" && field.name === targetField.label) {
                trackingPosition = field.fieldPosition;
            }
            else if (field.type === "LONGITUDE") {
                // if fields are located next to each other in the source data (spreadsheet)
                if (field.fieldPosition === trackingPosition + 1 || field.fieldPosition === trackingPosition - 1) {
                    return field.name;
                }
                else {
                    for (var j = 0; j < labels.length; j++) {
                        if (label.includes(labels[j])) {
                            return field.name;
                        }
                    }
                }
            }
        }
        return null;
    };
    CoordinatePageComponent.prototype.hasCoordinateField = function (field) {
        for (var i = 0; i < this.sheet.coordinates.length; i++) {
            var coordinate = this.sheet.coordinates[i];
            if (coordinate.id === field.label) {
                return true;
            }
        }
        return false;
    };
    CoordinatePageComponent.prototype.isBasic = function (field) {
        return (field.type == 'TEXT' || field.type == 'LONG' || field.type == 'DOUBLE');
    };
    CoordinatePageComponent.prototype.localValidate = function (value, config) {
        if (config == 'label') {
            return this.validateLabel(value);
        }
        return null;
    };
    CoordinatePageComponent.prototype.validateLabel = function (label) {
        if (this.sheet != null) {
            var count = 0;
            for (var i = 0; i < this.sheet.fields.length; i++) {
                var field = this.sheet.fields[i];
                if (field.label == label) {
                    count++;
                }
            }
            if (this.sheet.attributes != null) {
                for (var i = 0; i < this.sheet.attributes.ids.length; i++) {
                    var id = this.sheet.attributes.ids[i];
                    var attribute = this.sheet.attributes.values[id];
                    if (attribute.label == label) {
                        count++;
                    }
                }
            }
            for (var i = 0; i < this.sheet.coordinates.length; i++) {
                var coordinate = this.sheet.coordinates[i];
                if (coordinate.label == label) {
                    count++;
                }
            }
            if (count > 1) {
                return { unique: false };
            }
        }
        return null;
    };
    return CoordinatePageComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.UploadInformation)
], CoordinatePageComponent.prototype, "info", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Sheet)
], CoordinatePageComponent.prototype, "sheet", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Page)
], CoordinatePageComponent.prototype, "page", void 0);
CoordinatePageComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'coordinate-page',
        templateUrl: 'coordinate-page.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [localization_service_1.LocalizationService])
], CoordinatePageComponent);
exports.CoordinatePageComponent = CoordinatePageComponent;
//# sourceMappingURL=coordinate-page.component.js.map