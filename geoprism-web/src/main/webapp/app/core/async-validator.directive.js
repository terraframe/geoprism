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
var forms_1 = require("@angular/forms");
var AsyncValidator = AsyncValidator_1 = (function () {
    function AsyncValidator() {
    }
    AsyncValidator.prototype.validate = function (c) {
        if (c.value != null && c.value.length > 0) {
            return this.remoteValidator.validate(c.value, this.config);
        }
        return null;
    };
    return AsyncValidator;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", Object)
], AsyncValidator.prototype, "remoteValidator", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", String)
], AsyncValidator.prototype, "config", void 0);
AsyncValidator = AsyncValidator_1 = __decorate([
    core_1.Directive({
        selector: '[asyncValidator][ngModel]',
        providers: [
            { provide: forms_1.NG_ASYNC_VALIDATORS, useExisting: core_1.forwardRef(function () { return AsyncValidator_1; }), multi: true }
        ]
    }),
    __metadata("design:paramtypes", [])
], AsyncValidator);
exports.AsyncValidator = AsyncValidator;
var AsyncValidator_1;
//# sourceMappingURL=async-validator.directive.js.map