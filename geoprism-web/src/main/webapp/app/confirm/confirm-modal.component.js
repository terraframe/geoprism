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
var confirm_modal_service_1 = require("./confirm-modal.service");
var ConfirmModalComponent = (function () {
    function ConfirmModalComponent(service) {
        this.service = service;
        this.active = false;
    }
    ConfirmModalComponent.prototype.ngOnInit = function () {
        this.service.registerListener(this);
    };
    ConfirmModalComponent.prototype.ngOnDestroy = function () {
        this.service.deregisterListener(this);
    };
    ConfirmModalComponent.prototype.open = function (action) {
        if (this.action == null) {
            this.action = action;
            this.message = action.getMessage();
            this.active = true;
        }
    };
    ConfirmModalComponent.prototype.confirm = function () {
        if (this.action != null) {
            this.action.confirm();
            this.action = undefined;
        }
        this.active = false;
        this.message = undefined;
    };
    ConfirmModalComponent.prototype.cancel = function () {
        if (this.action != null) {
            this.action.cancel();
            this.action = undefined;
        }
        this.active = false;
        this.message = undefined;
    };
    return ConfirmModalComponent;
}());
ConfirmModalComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: "confirm-modal",
        templateUrl: 'confirm-modal.component.jsp',
        styleUrls: ['confirm-modal.component.css'],
    }),
    __metadata("design:paramtypes", [confirm_modal_service_1.ConfirmService])
], ConfirmModalComponent);
exports.ConfirmModalComponent = ConfirmModalComponent;
//# sourceMappingURL=confirm-modal.component.js.map