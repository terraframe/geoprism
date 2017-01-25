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
var AutoCompleteComponent = (function () {
    function AutoCompleteComponent() {
        // Emit a selected event when an item in the list is selected
        this.onDropdownSelect = new core_1.EventEmitter();
    }
    /**
     * Listen for a click event on the list
     */
    AutoCompleteComponent.prototype.onClick = function (item) {
        this.onDropdownSelect.emit(item);
    };
    AutoCompleteComponent.prototype.onMouseEnter = function () {
    };
    AutoCompleteComponent.prototype.onMouseLeave = function () {
    };
    return AutoCompleteComponent;
}());
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], AutoCompleteComponent.prototype, "onDropdownSelect", void 0);
AutoCompleteComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: "auto-complete",
        templateUrl: 'auto-complete.component.jsp',
        styleUrls: ['auto-complete.component.css'],
    }),
    __metadata("design:paramtypes", [])
], AutoCompleteComponent);
exports.AutoCompleteComponent = AutoCompleteComponent;
//# sourceMappingURL=auto-complete.component.js.map