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
var ConfirmModalDirective = (function () {
    function ConfirmModalDirective(service) {
        this.service = service;
        this.enabled = true;
        this.message = "Are you sure?";
        this.onConfirm = new core_1.EventEmitter();
    }
    /**
     * On key event is triggered when a key is released on the host component
     * the event starts a timer to prevent concurrent requests
     */
    ConfirmModalDirective.prototype.onClick = function () {
        if (this.enabled) {
            this.service.open(this);
        }
        else {
            this.onConfirm.emit();
        }
    };
    ConfirmModalDirective.prototype.getMessage = function () {
        return this.message;
    };
    ConfirmModalDirective.prototype.confirm = function () {
        this.onConfirm.emit();
    };
    ConfirmModalDirective.prototype.cancel = function () {
    };
    return ConfirmModalDirective;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", Boolean)
], ConfirmModalDirective.prototype, "enabled", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", String)
], ConfirmModalDirective.prototype, "message", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], ConfirmModalDirective.prototype, "onConfirm", void 0);
__decorate([
    core_1.HostListener('click'),
    __metadata("design:type", Function),
    __metadata("design:paramtypes", []),
    __metadata("design:returntype", void 0)
], ConfirmModalDirective.prototype, "onClick", null);
ConfirmModalDirective = __decorate([
    core_1.Directive({
        selector: "[confirm-modal]"
    }),
    __metadata("design:paramtypes", [confirm_modal_service_1.ConfirmService])
], ConfirmModalDirective);
exports.ConfirmModalDirective = ConfirmModalDirective;
//# sourceMappingURL=confirm-modal.directive.js.map