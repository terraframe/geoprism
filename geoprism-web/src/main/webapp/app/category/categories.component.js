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
var core_service_1 = require("../service/core.service");
var category_service_1 = require("../service/category.service");
var Instance = (function () {
    function Instance() {
    }
    return Instance;
}());
var CategoriesComponent = (function () {
    function CategoriesComponent(router, eventService, categoryService) {
        this.router = router;
        this.eventService = eventService;
        this.categoryService = categoryService;
        this.instance = new Instance();
    }
    CategoriesComponent.prototype.ngOnInit = function () {
        this.getCategories();
    };
    CategoriesComponent.prototype.getCategories = function () {
        var _this = this;
        this.categoryService
            .getAll()
            .then(function (categories) {
            _this.categories = categories;
        });
    };
    CategoriesComponent.prototype.remove = function (category) {
        var _this = this;
        this.categoryService
            .remove(category.id)
            .then(function (response) {
            _this.categories = _this.categories.filter(function (h) { return h !== category; });
        });
    };
    CategoriesComponent.prototype.edit = function (category) {
        this.router.navigate(['/category', category.id]);
    };
    CategoriesComponent.prototype.newInstance = function () {
        this.instance.active = true;
    };
    CategoriesComponent.prototype.create = function () {
        var _this = this;
        this.categoryService.create(this.instance.label, '', true)
            .then(function (category) {
            _this.categories.push(category);
            _this.instance.active = false;
            _this.instance.label = '';
        });
    };
    CategoriesComponent.prototype.cancel = function () {
        this.instance.active = false;
        this.instance.label = '';
    };
    return CategoriesComponent;
}());
CategoriesComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'categories',
        templateUrl: 'categories.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [router_1.Router,
        core_service_1.EventService,
        category_service_1.CategoryService])
], CategoriesComponent);
exports.CategoriesComponent = CategoriesComponent;
//# sourceMappingURL=categories.component.js.map