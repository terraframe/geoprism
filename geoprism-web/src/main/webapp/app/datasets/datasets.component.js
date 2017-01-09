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
var router_1 = require("@angular/router");
var dataset_service_1 = require("../service/dataset.service");
var DatasetsComponent = (function () {
    function DatasetsComponent(router, datasetService) {
        this.router = router;
        this.datasetService = datasetService;
    }
    DatasetsComponent.prototype.getDatasets = function () {
        var _this = this;
        this.datasetService
            .getDatasets()
            .then(function (datasets) {
            _this.datasets = datasets;
        });
    };
    DatasetsComponent.prototype.remove = function (dataset, event) {
        var _this = this;
        this.datasetService
            .remove(dataset)
            .then(function (response) {
            _this.datasets = _this.datasets.filter(function (h) { return h !== dataset; });
        });
    };
    DatasetsComponent.prototype.edit = function (dataset, event) {
        this.router.navigate(['/dataset', dataset.id]);
    };
    DatasetsComponent.prototype.ngOnInit = function () {
        this.getDatasets();
    };
    return DatasetsComponent;
}());
DatasetsComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'datasets',
        templateUrl: 'datasets.component.jsp',
        styleUrls: ['datasets.component.css']
    }),
    __metadata("design:paramtypes", [router_1.Router,
        dataset_service_1.DatasetService])
], DatasetsComponent);
exports.DatasetsComponent = DatasetsComponent;
//# sourceMappingURL=datasets.component.js.map