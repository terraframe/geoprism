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
require("rxjs/add/operator/switchMap");
var core_service_1 = require("../service/core.service");
var localization_service_1 = require("../service/localization.service");
var category_service_1 = require("../service/category.service");
var OptionResolver = (function () {
    function OptionResolver(categoryService, eventService) {
        this.categoryService = categoryService;
        this.eventService = eventService;
    }
    OptionResolver.prototype.resolve = function (route, state) {
        var _this = this;
        return this.categoryService.edit(route.params['parentId'], route.params['id'])
            .catch(function (error) {
            _this.eventService.onError(error);
            return Promise.reject(error);
        });
    };
    return OptionResolver;
}());
OptionResolver = __decorate([
    __param(0, core_1.Inject(category_service_1.CategoryService)), __param(1, core_1.Inject(core_service_1.EventService)),
    __metadata("design:paramtypes", [category_service_1.CategoryService, core_service_1.EventService])
], OptionResolver);
exports.OptionResolver = OptionResolver;
var Action = (function () {
    function Action() {
        this.synonym = '';
        this.restore = [];
    }
    return Action;
}());
var OptionDetailComponent = (function () {
    function OptionDetailComponent(categoryService, route, location, localizationService) {
        this.categoryService = categoryService;
        this.route = route;
        this.location = location;
        this.localizationService = localizationService;
        this.close = new core_1.EventEmitter();
    }
    OptionDetailComponent.prototype.ngOnInit = function () {
        this.category = this.route.snapshot.data['category'];
        this.action = new Action();
    };
    OptionDetailComponent.prototype.onSubmit = function () {
        var _this = this;
        var config = {
            option: this.category,
            synonym: this.action.synonym,
            restore: this.action.restore
        };
        this.categoryService.apply(config)
            .then(function (response) {
            _this.goBack(_this.category);
        });
    };
    OptionDetailComponent.prototype.cancel = function () {
        var _this = this;
        this.categoryService.unlock(this.category)
            .then(function (response) {
            _this.goBack(_this.category);
        });
    };
    OptionDetailComponent.prototype.goBack = function (category) {
        this.close.emit(category);
        this.location.back();
    };
    OptionDetailComponent.prototype.restore = function (synonym) {
        var message = this.localizationService.localize("category.management", "restoreConfirm");
        message = message.replace('{0}', this.category.label);
        if (confirm(message)) {
            this.action.restore.push(synonym.id);
            this.category.synonyms = this.category.synonyms.filter(function (h) { return h !== synonym; });
        }
    };
    return OptionDetailComponent;
}());
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], OptionDetailComponent.prototype, "close", void 0);
OptionDetailComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'option-detail',
        templateUrl: 'option-detail.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [category_service_1.CategoryService,
        router_1.ActivatedRoute,
        common_1.Location,
        localization_service_1.LocalizationService])
], OptionDetailComponent);
exports.OptionDetailComponent = OptionDetailComponent;
//# sourceMappingURL=option-detail.component.js.map