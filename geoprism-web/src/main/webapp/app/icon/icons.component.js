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
var icon_service_1 = require("../service/icon.service");
var IconsComponent = (function () {
    function IconsComponent(router, iconService) {
        this.router = router;
        this.iconService = iconService;
    }
    IconsComponent.prototype.ngOnInit = function () {
        this.getIcons();
    };
    IconsComponent.prototype.getIcons = function () {
        var _this = this;
        this.iconService
            .getIcons()
            .then(function (icons) {
            _this.icons = icons;
        });
    };
    IconsComponent.prototype.remove = function (icon) {
        var _this = this;
        this.iconService
            .remove(icon.id)
            .then(function (response) {
            _this.icons = _this.icons.filter(function (h) { return h.id !== icon.id; });
        });
    };
    IconsComponent.prototype.edit = function (icon) {
        this.router.navigate(['/icon', icon.id]);
    };
    IconsComponent.prototype.add = function () {
        this.router.navigate(['/icon', -1]);
    };
    return IconsComponent;
}());
IconsComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'icons',
        templateUrl: 'icons.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [router_1.Router,
        icon_service_1.IconService])
], IconsComponent);
exports.IconsComponent = IconsComponent;
//# sourceMappingURL=icons.component.js.map