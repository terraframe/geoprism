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
var __param = (this && this.__param) || function (paramIndex, decorator) {
    return function (target, key) { decorator(target, key, paramIndex); }
};
var core_1 = require("@angular/core");
var router_1 = require("@angular/router");
var common_1 = require("@angular/common");
var ng2_file_upload_1 = require("ng2-file-upload/ng2-file-upload");
require("rxjs/add/operator/switchMap");
var core_service_1 = require("../service/core.service");
var icon_1 = require("../model/icon");
var icon_service_1 = require("../service/icon.service");
var IconResolver = (function () {
    function IconResolver(iconService, eventService) {
        this.iconService = iconService;
        this.eventService = eventService;
    }
    IconResolver.prototype.resolve = function (route, state) {
        var _this = this;
        var id = route.params['id'];
        if (id !== "-1") {
            return this.iconService.edit(id)
                .catch(function (error) {
                _this.eventService.onError(error);
                return Promise.reject(error);
            });
        }
        else {
            return new Promise(function (resolve, reject) {
                resolve(new icon_1.Icon());
            });
        }
    };
    return IconResolver;
}());
IconResolver = __decorate([
    __param(0, core_1.Inject(icon_service_1.IconService)), __param(1, core_1.Inject(core_service_1.EventService)),
    __metadata("design:paramtypes", [icon_service_1.IconService, core_service_1.EventService])
], IconResolver);
exports.IconResolver = IconResolver;
var IconDetailComponent = (function () {
    function IconDetailComponent(router, route, location, iconService, eventService) {
        this.router = router;
        this.route = route;
        this.location = location;
        this.iconService = iconService;
        this.eventService = eventService;
        this.dropActive = false;
    }
    IconDetailComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.icon = this.route.snapshot.data['icon'];
        var url = acp + '/iconimage/create';
        if (this.icon.id != null) {
            url = acp + '/iconimage/apply';
        }
        var options = {
            autoUpload: false,
            queueLimit: 1,
            removeAfterUpload: true,
            url: url
        };
        this.uploader = new ng2_file_upload_1.FileUploader(options);
        this.uploader.onBeforeUploadItem = function (fileItem) {
            _this.eventService.start();
        };
        this.uploader.onCompleteItem = function (item, response, status, headers) {
            _this.eventService.complete();
        };
        this.uploader.onSuccessItem = function (item, response, status, headers) {
            _this.location.back();
        };
        this.uploader.onErrorItem = function (item, response, status, headers) {
            _this.eventService.onError(response);
        };
        this.uploader.onBuildItemForm = function (fileItem, form) {
            form.append('label', _this.icon.label);
            if (_this.icon.id != null) {
                form.append('id', _this.icon.id);
            }
        };
    };
    IconDetailComponent.prototype.ngAfterViewInit = function () {
        var _this = this;
        var that = this;
        this.uploader.onAfterAddingFile = (function (item) {
            _this.uploadElRef.nativeElement.value = '';
            var reader = new FileReader();
            reader.onload = function (e) {
                that.file = reader.result;
            };
            reader.readAsDataURL(item._file);
            if (that.icon.label == null || that.icon.label == '') {
                that.icon.label = item.file.name.replace(".png", "");
            }
        });
    };
    IconDetailComponent.prototype.fileOver = function (e) {
        this.dropActive = e;
    };
    IconDetailComponent.prototype.cancel = function () {
        var _this = this;
        if (this.icon.id != null) {
            this.iconService.unlock(this.icon.id)
                .then(function (response) {
                _this.location.back();
            });
        }
        else {
            this.location.back();
        }
    };
    IconDetailComponent.prototype.onSubmit = function () {
        var _this = this;
        if (this.file == null) {
            this.iconService.apply(this.icon.id, this.icon.label)
                .then(function (icon) {
                _this.location.back();
            });
        }
        else {
            this.uploader.uploadAll();
        }
    };
    IconDetailComponent.prototype.clear = function () {
        this.file = null;
        this.icon.filePath = null;
        this.uploader.clearQueue();
    };
    return IconDetailComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", icon_1.Icon)
], IconDetailComponent.prototype, "icon", void 0);
__decorate([
    core_1.ViewChild('uploadEl'),
    __metadata("design:type", core_1.ElementRef)
], IconDetailComponent.prototype, "uploadElRef", void 0);
IconDetailComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'icon-detail',
        templateUrl: 'icon-detail.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [router_1.Router,
        router_1.ActivatedRoute,
        common_1.Location,
        icon_service_1.IconService,
        core_service_1.EventService])
], IconDetailComponent);
exports.IconDetailComponent = IconDetailComponent;
//# sourceMappingURL=icon-detail.component.js.map