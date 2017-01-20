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
var ng2_file_upload_1 = require("ng2-file-upload/ng2-file-upload");
var core_service_1 = require("../service/core.service");
var localization_service_1 = require("../service/localization.service");
var dataset_service_1 = require("../service/dataset.service");
var upload_wizard_component_1 = require("../uploader/upload-wizard.component");
var DatasetsComponent = (function () {
    function DatasetsComponent(router, datasetService, localizationService, eventService) {
        this.router = router;
        this.datasetService = datasetService;
        this.localizationService = localizationService;
        this.eventService = eventService;
        this.dropActive = false;
    }
    DatasetsComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.getDatasets();
        this.uploader = new ng2_file_upload_1.FileUploader({ url: acp + '/uploader/getAttributeInformation' });
        this.uploader.onAfterAddingFile = function (fileItem) {
            _this.uploader.uploadItem(fileItem);
        };
        this.uploader.onBeforeUploadItem = function (fileItem) {
            _this.eventService.start();
        };
        this.uploader.onCompleteItem = function (item, response, status, headers) {
            _this.eventService.complete();
        };
        this.uploader.onSuccessItem = function (item, response, status, headers) {
            _this.wizard.initialize(response);
        };
        this.uploader.onErrorItem = function (item, response, status, headers) {
            _this.eventService.onError(response);
        };
    };
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
        var message = this.localizationService.localize("dataset", "removeContent");
        message = message.replace('{0}', dataset.label);
        if (confirm(message)) {
            this.datasetService
                .remove(dataset)
                .then(function (response) {
                _this.datasets = _this.datasets.filter(function (h) { return h !== dataset; });
            });
        }
    };
    DatasetsComponent.prototype.edit = function (dataset, event) {
        this.router.navigate(['/dataset', dataset.id]);
    };
    DatasetsComponent.prototype.fileOver = function (e) {
        this.dropActive = e;
    };
    DatasetsComponent.prototype.onSuccess = function (data) {
        if (data.datasets != null) {
            this.addDatasets(data.datasets);
        }
    };
    DatasetsComponent.prototype.getIndex = function (dataset) {
        for (var i = 0; i < this.datasets.length; i++) {
            if (this.datasets[i].id == dataset.id) {
                return i;
            }
        }
        return -1;
    };
    DatasetsComponent.prototype.addDatasets = function (datasets) {
        for (var i = 0; i < datasets.length; i++) {
            var dataset = datasets[i];
            var index = this.getIndex(dataset);
            if (index == -1) {
                this.datasets.push(dataset);
            }
            else {
                this.datasets[index] = dataset;
            }
        }
    };
    return DatasetsComponent;
}());
__decorate([
    core_1.ViewChild(upload_wizard_component_1.UploadWizardComponent),
    __metadata("design:type", upload_wizard_component_1.UploadWizardComponent)
], DatasetsComponent.prototype, "wizard", void 0);
DatasetsComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'datasets',
        templateUrl: 'datasets.component.jsp',
        styleUrls: ['datasets.component.css']
    }),
    __metadata("design:paramtypes", [router_1.Router,
        dataset_service_1.DatasetService,
        localization_service_1.LocalizationService,
        core_service_1.EventService])
], DatasetsComponent);
exports.DatasetsComponent = DatasetsComponent;
//# sourceMappingURL=datasets.component.js.map