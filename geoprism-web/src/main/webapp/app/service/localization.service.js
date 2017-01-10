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
/// License along with Runway SDK(tm).  If not, see <ehttp://www.gnu.org/licenses/>.
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
var LocalizationService = (function () {
    function LocalizationService() {
        this.parser = Globalize.numberParser();
        this.formatter = Globalize.numberFormatter();
    }
    LocalizationService.prototype.parseNumber = function (value) {
        if (value != null && value.length > 0) {
            //convert data from view format to model format
            var number = this.parser(value);
            return number;
        }
        return null;
    };
    LocalizationService.prototype.formatNumber = function (value) {
        if (value != null) {
            var number = value;
            if (typeof number === 'string') {
                if (number.length > 0 && Number(number)) {
                    number = Number(value);
                }
                else {
                    return "";
                }
            }
            //convert data from model format to view format
            return this.formatter(number);
        }
        return null;
    };
    LocalizationService.prototype.localize = function (bundle, key) {
        return com.runwaysdk.Localize.localize(bundle, key);
    };
    LocalizationService.prototype.get = function (key) {
        return com.runwaysdk.Localize.get(key);
    };
    return LocalizationService;
}());
LocalizationService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [])
], LocalizationService);
exports.LocalizationService = LocalizationService;
//# sourceMappingURL=localization.service.js.map