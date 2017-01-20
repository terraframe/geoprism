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
var category_service_1 = require("../service/category.service");
var AttributesPageComponent = (function () {
    function AttributesPageComponent(categoryService) {
        this.categoryService = categoryService;
        this.onFieldChange = new core_1.EventEmitter();
        this.longitudeFields = {};
        this.latitudeFields = {};
        this.textFields = {};
    }
    AttributesPageComponent.prototype.ngOnInit = function () {
        for (var i = 0; i < this.sheet.fields.length; i++) {
            var field = this.sheet.fields[i];
            if (field.categoryLabel == null) {
                field.categoryLabel = field.label;
            }
            if (field.root == null) {
                field.root = '';
            }
            this.accept(field);
        }
        // Initialize the universal options
        if (this.info.options != null) {
            var countries = this.info.options.countries;
            for (var i = 0; i < countries.length; i++) {
                var country = countries[i];
                if (country.value == this.sheet.country) {
                    this.universals = country.options;
                }
            }
        }
    };
    AttributesPageComponent.prototype.accept = function (field) {
        if (field.type === "LATITUDE") {
            this.latitudeFields[field.name] = field;
        }
        else {
            delete this.latitudeFields[field.name];
        }
        if (field.type === "LONGITUDE") {
            this.longitudeFields[field.name] = field;
        }
        else {
            delete this.longitudeFields[field.name];
        }
        if (field.type === "TEXT") {
            this.textFields[field.name] = field;
        }
        else {
            delete this.textFields[field.name];
        }
        //    let matched = (Object.keys(this.latitudeFields).length == Object.keys(this.longitudeFields).length);
        //    this.form.$setValidity("coordinate", matched);
        //      
        //    if(Object.keys(this.latitudeFields).length > 0 || Object.keys(this.longitudeFields).length > 0) {
        //      this.form.$setValidity("coordinateText", (Object.keys(this.textFields).length > 0));        
        //    }
        //    else {
        //      this.form.$setValidity("coordinateText", true);        
        //    } 
        //    this.onFieldChange.emit(field);
    };
    AttributesPageComponent.prototype.localValidate = function (value, config) {
        if (config == 'label') {
            return this.validateLabel(value);
        }
        return null;
    };
    AttributesPageComponent.prototype.validateLabel = function (label) {
        if (this.sheet != null) {
            var count = 0;
            for (var i = 0; i < this.sheet.fields.length; i++) {
                var field = this.sheet.fields[i];
                if (field.label == label) {
                    count++;
                }
            }
            if (count > 1) {
                return { unique: false };
            }
        }
        return null;
    };
    AttributesPageComponent.prototype.validate = function (value, config) {
        if (config == 'category') {
            return this.validateCategory(value);
        }
        return null;
    };
    AttributesPageComponent.prototype.validateCategory = function (label) {
        return this.categoryService.validate(label, '')
            .then(function (response) {
            return null;
        })
            .catch(function (error) {
            return { uniqueName: false };
        });
    };
    return AttributesPageComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.UploadInformation)
], AttributesPageComponent.prototype, "info", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Sheet)
], AttributesPageComponent.prototype, "sheet", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Page)
], AttributesPageComponent.prototype, "page", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], AttributesPageComponent.prototype, "onFieldChange", void 0);
AttributesPageComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'attributes-page',
        templateUrl: 'attributes-page.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [category_service_1.CategoryService])
], AttributesPageComponent);
exports.AttributesPageComponent = AttributesPageComponent;
//# sourceMappingURL=attributes-page.component.js.map