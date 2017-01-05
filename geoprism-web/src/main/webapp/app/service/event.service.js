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
var EventService = (function () {
    function EventService() {
        this.listeners = [];
    }
    EventService.prototype.registerListener = function (listener) {
        this.listeners.push(listener);
    };
    EventService.prototype.deregisterListener = function (listener) {
        var indexOfItem = this.listeners.indexOf(listener);
        if (indexOfItem === -1) {
            return false;
        }
        this.listeners.splice(indexOfItem, 1);
        return true;
    };
    EventService.prototype.start = function () {
        for (var _i = 0, _a = this.listeners; _i < _a.length; _i++) {
            var listener = _a[_i];
            listener.start();
        }
    };
    EventService.prototype.complete = function () {
        for (var _i = 0, _a = this.listeners; _i < _a.length; _i++) {
            var listener = _a[_i];
            listener.complete();
        }
    };
    return EventService;
}());
EventService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [])
], EventService);
exports.EventService = EventService;
//# sourceMappingURL=event.service.js.map