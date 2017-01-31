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
var ConfirmModalComponent = (function () {
    function ConfirmModalComponent() {
        this.onConfirm = new core_1.EventEmitter();
        this.onCancel = new core_1.EventEmitter();
    }
    ConfirmModalComponent.prototype.setMessage = function (message) {
        this.message = message;
    };
    ConfirmModalComponent.prototype.confirm = function () {
        this.onConfirm.emit();
    };
    ConfirmModalComponent.prototype.cancel = function () {
        this.onCancel.emit();
    };
    return ConfirmModalComponent;
}());
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], ConfirmModalComponent.prototype, "onConfirm", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], ConfirmModalComponent.prototype, "onCancel", void 0);
ConfirmModalComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: "confirm-modal",
        templateUrl: 'confirm-modal.component.jsp',
        styleUrls: ['confirm-modal.component.css'],
    }),
    __metadata("design:paramtypes", [])
], ConfirmModalComponent);
exports.ConfirmModalComponent = ConfirmModalComponent;
//# sourceMappingURL=confirm-modal.component.js.map