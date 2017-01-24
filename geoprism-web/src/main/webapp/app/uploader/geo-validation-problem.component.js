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
var upload_service_1 = require("../service/upload.service");
var core_service_1 = require("../service/core.service");
var GeoValidationProblemComponent = (function () {
    function GeoValidationProblemComponent(uploadService, idService) {
        var _this = this;
        this.uploadService = uploadService;
        this.idService = idService;
        this.onResolve = new core_1.EventEmitter();
        this.source = function (keyword) {
            var limit = '20';
            return _this.uploadService.getGeoEntitySuggestions(_this.problem.parentId, _this.problem.universalId, keyword, limit);
        };
    }
    GeoValidationProblemComponent.prototype.ngOnInit = function () {
        this.problem.synonym = null;
        this.show = false;
        this.hasSynonym = false;
    };
    GeoValidationProblemComponent.prototype.setSynonym = function (item) {
        this.problem.synonym = item.data;
        this.hasSynonym = (this.problem.synonym != null);
    };
    GeoValidationProblemComponent.prototype.createSynonym = function () {
        var _this = this;
        if (this.hasSynonym) {
            this.uploadService.createGeoEntitySynonym(this.problem.synonym, this.problem.label)
                .then(function (response) {
                _this.problem.resolved = true;
                _this.problem.action = {
                    name: 'SYNONYM',
                    synonymId: response.synonymId,
                    label: response.label,
                    ancestors: response.ancestors
                };
            });
        }
    };
    GeoValidationProblemComponent.prototype.createEntity = function () {
        var _this = this;
        this.uploadService.createGeoEntity(this.problem.parentId, this.problem.universalId, this.problem.label)
            .then(function (response) {
            _this.problem.resolved = true;
            _this.problem.action = {
                name: 'ENTITY',
                entityId: response.entityId
            };
        });
    };
    GeoValidationProblemComponent.prototype.removeLocationExclusion = function (exclusionId) {
        if (this.workbook.locationExclusions) {
            this.workbook.locationExclusions = this.workbook.locationExclusions.filter(function (h) { return h.id !== exclusionId; });
        }
    };
    GeoValidationProblemComponent.prototype.ignoreDataAtLocation = function () {
        var locationLabel = this.problem.label;
        var universal = this.problem.universalId;
        var id = this.idService.generateId();
        this.problem.resolved = true;
        this.problem.action = {
            name: 'IGNOREATLOCATION',
            label: locationLabel,
            id: id
        };
        var exclusion = new uploader_model_1.LocationExclusion(id, universal, locationLabel, this.problem.parentId);
        if (this.workbook.locationExclusions) {
            this.workbook.locationExclusions.push(exclusion);
        }
        else {
            this.workbook.locationExclusions = [exclusion];
        }
    };
    GeoValidationProblemComponent.prototype.undoAction = function () {
        var _this = this;
        var locationLabel = this.problem.label;
        var universal = this.problem.universalId;
        if (this.problem.resolved) {
            var action = this.problem.action;
            if (action.name == 'ENTITY') {
                this.uploadService.deleteGeoEntity(action.entityId)
                    .then(function (response) {
                    _this.problem.resolved = false;
                    _this.problem.synonym = null;
                    _this.problem.action = null;
                    _this.hasSynonym = (_this.problem.synonym != null);
                });
            }
            else if (action.name == 'IGNOREATLOCATION') {
                this.problem.resolved = false;
                this.problem.action = null;
                this.removeLocationExclusion(action.id);
            }
            else if (action.name == 'SYNONYM') {
                this.uploadService.deleteGeoEntitySynonym(action.synonymId)
                    .then(function (response) {
                    _this.problem.resolved = false;
                    _this.problem.synonym = null;
                    _this.problem.action = null;
                    _this.hasSynonym = (_this.problem.synonym != null);
                });
            }
        }
    };
    GeoValidationProblemComponent.prototype.toggle = function () {
        this.show = !this.show;
    };
    return GeoValidationProblemComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.LocationProblem)
], GeoValidationProblemComponent.prototype, "problem", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", Number)
], GeoValidationProblemComponent.prototype, "index", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Workbook)
], GeoValidationProblemComponent.prototype, "workbook", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], GeoValidationProblemComponent.prototype, "onResolve", void 0);
GeoValidationProblemComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'geo-validation-problem',
        templateUrl: 'geo-validation-problem.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [upload_service_1.UploadService, core_service_1.IdService])
], GeoValidationProblemComponent);
exports.GeoValidationProblemComponent = GeoValidationProblemComponent;
//# sourceMappingURL=geo-validation-problem.component.js.map