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
var upload_service_1 = require("../service/upload.service");
var core_service_1 = require("../service/core.service");
var CategoryValidationProblemComponent = (function () {
    function CategoryValidationProblemComponent(uploadService, categoryService, idService) {
        var _this = this;
        this.uploadService = uploadService;
        this.categoryService = categoryService;
        this.idService = idService;
        this.onProblemChange = new core_1.EventEmitter();
        this.source = function (text) {
            var limit = '20';
            return _this.uploadService.getClassifierSuggestions(_this.problem.mdAttributeId, text, limit);
        };
    }
    CategoryValidationProblemComponent.prototype.ngOnInit = function () {
        this.problem.synonym = null;
        this.show = false;
        this.hasSynonym = false;
    };
    CategoryValidationProblemComponent.prototype.setSynonym = function () {
        this.hasSynonym = (this.problem.synonym != null && this.problem.synonym.length > 0);
    };
    CategoryValidationProblemComponent.prototype.createSynonym = function () {
        var _this = this;
        if (this.hasSynonym) {
            this.uploadService.createClassifierSynonym(this.problem.synonym, this.problem.label)
                .then(function (response) {
                _this.problem.resolved = true;
                _this.problem.action = {
                    name: 'SYNONYM',
                    synonymId: response.synonymId,
                    label: response.label
                };
                _this.onProblemChange.emit(_this.problem);
            });
        }
    };
    CategoryValidationProblemComponent.prototype.createOption = function () {
        var _this = this;
        this.categoryService.create(this.problem.label, this.problem.categoryId)
            .then(function (response) {
            _this.problem.resolved = true;
            _this.problem.action = {
                name: 'OPTION',
                optionId: response.id
            };
            _this.onProblemChange.emit(_this.problem);
        });
    };
    CategoryValidationProblemComponent.prototype.ignoreValue = function () {
        this.problem.resolved = true;
        this.problem.action = {
            name: 'IGNORE'
        };
        var mdAttributeId = this.problem.mdAttributeId;
        if (!this.workbook.categoryExclusion) {
            this.workbook.categoryExclusion = {};
        }
        if (!this.workbook.categoryExclusion[mdAttributeId]) {
            this.workbook.categoryExclusion[mdAttributeId] = [];
        }
        this.workbook.categoryExclusion[mdAttributeId].push(this.problem.label);
        this.onProblemChange.emit(this.problem);
    };
    CategoryValidationProblemComponent.prototype.removeExclusion = function () {
        var mdAttributeId = this.problem.mdAttributeId;
        var label = this.problem.label;
        if (this.workbook.categoryExclusion && this.workbook.categoryExclusion[mdAttributeId]) {
            this.workbook.categoryExclusion[mdAttributeId] = this.workbook.categoryExclusion[mdAttributeId].filter(function (h) { return h !== label; });
        }
        if (this.workbook.categoryExclusion[mdAttributeId].length === 0) {
            delete this.workbook.categoryExclusion[mdAttributeId];
        }
    };
    CategoryValidationProblemComponent.prototype.undoAction = function () {
        var _this = this;
        if (this.problem.resolved) {
            var action = this.problem.action;
            if (action.name == 'IGNORE') {
                this.problem.resolved = false;
                this.removeExclusion();
                this.onProblemChange.emit(this.problem);
            }
            else if (action.name == 'SYNONYM') {
                this.uploadService.deleteClassifierSynonym(action.synonymId)
                    .then(function (response) {
                    _this.problem.resolved = false;
                    _this.problem.synonym = null;
                    _this.problem.action = null;
                    _this.hasSynonym = (_this.problem.synonym != null);
                    _this.onProblemChange.emit(_this.problem);
                });
            }
            else if (action.name == 'OPTION') {
                this.categoryService.remove(action.optionId)
                    .then(function (response) {
                    _this.problem.resolved = false;
                    _this.problem.optionId = null;
                    _this.problem.action = null;
                    _this.hasSynonym = (_this.problem.synonym != null);
                    _this.onProblemChange.emit(_this.problem);
                });
            }
        }
    };
    return CategoryValidationProblemComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.CategoryProblem)
], CategoryValidationProblemComponent.prototype, "problem", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", Number)
], CategoryValidationProblemComponent.prototype, "index", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Workbook)
], CategoryValidationProblemComponent.prototype, "workbook", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", Array)
], CategoryValidationProblemComponent.prototype, "options", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], CategoryValidationProblemComponent.prototype, "onProblemChange", void 0);
CategoryValidationProblemComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'category-validation-problem',
        templateUrl: 'category-validation-problem.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [upload_service_1.UploadService, category_service_1.CategoryService, core_service_1.IdService])
], CategoryValidationProblemComponent);
exports.CategoryValidationProblemComponent = CategoryValidationProblemComponent;
//# sourceMappingURL=category-validation-problem.component.js.map