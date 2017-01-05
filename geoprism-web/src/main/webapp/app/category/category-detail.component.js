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
var category_1 = require("../model/category");
var category_service_1 = require("../service/category.service");
var CategoryResolver = (function () {
    function CategoryResolver(categoryService) {
        this.categoryService = categoryService;
    }
    CategoryResolver.prototype.resolve = function (route, state) {
        return this.categoryService.get(route.params['id']);
    };
    return CategoryResolver;
}());
CategoryResolver = __decorate([
    __param(0, core_1.Inject(category_service_1.CategoryService)),
    __metadata("design:paramtypes", [category_service_1.CategoryService])
], CategoryResolver);
exports.CategoryResolver = CategoryResolver;
var Instance = (function () {
    function Instance() {
    }
    return Instance;
}());
var CategoryDetailComponent = (function () {
    function CategoryDetailComponent(categoryService, route, location) {
        this.categoryService = categoryService;
        this.route = route;
        this.location = location;
        this.close = new core_1.EventEmitter();
        this.instance = new Instance();
    }
    CategoryDetailComponent.prototype.ngOnInit = function () {
        this.category = this.route.snapshot.data['category'];
        this.instance.active = false;
        this.instance.label = '';
    };
    CategoryDetailComponent.prototype.onSubmit = function () {
        var _this = this;
        this.categoryService.apply(this.category)
            .then(function (category) {
            _this.goBack(category);
        })
            .catch(function (error) {
            _this.error = error;
        });
    };
    CategoryDetailComponent.prototype.goBack = function (category) {
        this.close.emit(category);
        this.location.back();
    };
    return CategoryDetailComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", category_1.Category)
], CategoryDetailComponent.prototype, "category", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], CategoryDetailComponent.prototype, "close", void 0);
CategoryDetailComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'category-detail',
        templateUrl: 'category-detail.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [category_service_1.CategoryService,
        router_1.ActivatedRoute,
        common_1.Location])
], CategoryDetailComponent);
exports.CategoryDetailComponent = CategoryDetailComponent;
//# sourceMappingURL=category-detail.component.js.map