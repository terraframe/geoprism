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
var SummaryPageComponent = (function () {
    function SummaryPageComponent() {
        this.texts = [];
        this.categories = [];
        this.numbers = [];
        this.booleans = [];
        this.dates = [];
        this.universalMap = {};
    }
    SummaryPageComponent.prototype.ngOnInit = function () {
        // Initialize the universal options
        if (this.info.options != null) {
            var countries = this.info.options.countries;
            for (var i = 0; i < countries.length; i++) {
                var country = countries[i];
                if (country.value == this.sheet.country) {
                    this.universals = country.options;
                    this.labels = {};
                    for (var j = 0; j < country.options.length; j++) {
                        var universal = country.options[j];
                        this.labels[universal.value] = universal.label;
                    }
                }
            }
        }
        for (var i = 0; i < this.sheet.fields.length; i++) {
            var field = this.sheet.fields[i];
            var valid = this.isValid(field);
            if (valid && field.type === 'TEXT') {
                this.texts.push(field);
            }
            if (valid && (field.type === 'CATEGORY' || field.type === 'DOMAIN')) {
                this.categories.push(field);
            }
            if (valid && field.type === 'DOUBLE' || field.type === 'LONG') {
                this.numbers.push(field);
            }
            if (valid && field.type === 'BOOLEAN') {
                this.booleans.push(field);
            }
            if (valid && field.type === 'DATE') {
                this.dates.push(field);
            }
        }
        for (var i = 0; i < this.sheet.attributes.ids.length; i++) {
            var id = this.sheet.attributes.ids[i];
            var attribute = this.sheet.attributes.values[id];
            this.universalMap[id] = [];
            for (var j = 0; j < this.universals.length; j++) {
                var universal = this.universals[j];
                if (attribute.fields[universal.value] != null && attribute.fields[universal.value] != 'EXCLUDE') {
                    this.universalMap[id].push(universal);
                }
            }
        }
        var c = this.sheet.coordinates;
        if (c != null && typeof c === 'object' && c.ids != null) {
            var coordinates_1 = new Array();
            c.ids.forEach(function (id) {
                coordinates_1.push(c.values[id]);
            });
            this.sheet.coordinates = coordinates_1;
        }
    };
    SummaryPageComponent.prototype.hasFieldType = function (type) {
        var fields = this.sheet.fields;
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            if (field.type.toLowerCase() === type.toLowerCase()) {
                return true;
            }
        }
        return false;
    };
    SummaryPageComponent.prototype.isValid = function (field) {
        return !(field.type == 'LOCATION' || field.type == 'LONGITUDE' || field.type == 'LATITUDE' || field.type == 'IGNORE' || field.type == '');
    };
    return SummaryPageComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Sheet)
], SummaryPageComponent.prototype, "sheet", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Page)
], SummaryPageComponent.prototype, "page", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.UploadInformation)
], SummaryPageComponent.prototype, "info", void 0);
SummaryPageComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'summary-page',
        templateUrl: 'summary-page.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [])
], SummaryPageComponent);
exports.SummaryPageComponent = SummaryPageComponent;
//# sourceMappingURL=summary-page.component.js.map