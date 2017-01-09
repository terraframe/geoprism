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
var core_service_1 = require("../service/core.service");
var ErrorMessageComponent = (function () {
    function ErrorMessageComponent(service) {
        this.service = service;
        this.error = null;
    }
    ErrorMessageComponent.prototype.ngOnInit = function () {
        this.service.registerListener(this);
    };
    ErrorMessageComponent.prototype.ngOnDestroy = function () {
        this.service.deregisterListener(this);
    };
    ErrorMessageComponent.prototype.start = function () {
        this.error = null;
    };
    ErrorMessageComponent.prototype.complete = function () {
        console.log('complete');
    };
    ErrorMessageComponent.prototype.onError = function (error) {
        this.error = error;
    };
    return ErrorMessageComponent;
}());
ErrorMessageComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'error-message',
        templateUrl: 'error-message.component.jsp',
        styleUrls: ['error-message.component.css']
    }),
    __metadata("design:paramtypes", [core_service_1.EventService])
], ErrorMessageComponent);
exports.ErrorMessageComponent = ErrorMessageComponent;
//# sourceMappingURL=error-message.component.js.map