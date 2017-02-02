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
var ConfirmService = (function () {
    function ConfirmService() {
        this.listeners = [];
    }
    ConfirmService.prototype.registerListener = function (listener) {
        this.listeners.push(listener);
    };
    ConfirmService.prototype.deregisterListener = function (listener) {
        this.listeners = this.listeners.filter(function (h) { return h !== listener; });
    };
    ConfirmService.prototype.open = function (action) {
        for (var _i = 0, _a = this.listeners; _i < _a.length; _i++) {
            var listener = _a[_i];
            listener.open(action);
        }
    };
    return ConfirmService;
}());
ConfirmService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [])
], ConfirmService);
exports.ConfirmService = ConfirmService;
//# sourceMappingURL=confirm-modal.service.js.map