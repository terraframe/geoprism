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
var Item = (function () {
    function Item(item) {
        this.text = item.text;
        this.data = item.data;
        this.selected = false;
    }
    return Item;
}());
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
    AutoCompleteComponent.prototype.onMouseEnter = function (item, index) {
        item.selected = true;
        this.index = index;
    };
    AutoCompleteComponent.prototype.onMouseLeave = function (item, index) {
        item.selected = false;
        this.index = -1;
    };
    AutoCompleteComponent.prototype.up = function () {
        if (this.index === -1) {
            this.index = (this.list.length - 1);
        }
        else {
            this.list[this.index].selected = false;
            this.index--;
        }
        if (this.index < 0) {
            this.index = (this.list.length - 1);
        }
        this.list[this.index].selected = true;
    };
    AutoCompleteComponent.prototype.down = function () {
        if (this.index === -1) {
            this.index = 0;
        }
        else {
            this.list[this.index].selected = false;
            this.index++;
        }
        if (this.index === this.list.length) {
            this.index = 0;
        }
        this.list[this.index].selected = true;
    };
    AutoCompleteComponent.prototype.enter = function () {
        this.onDropdownSelect.emit(this.list[this.index]);
    };
    AutoCompleteComponent.prototype.setOptions = function (items) {
        var _this = this;
        this.list = [];
        this.index = -1;
        items.forEach(function (item) {
            _this.list.push(new Item(item));
        });
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
        selector: "auto-complete-component",
        templateUrl: 'auto-complete.component.jsp',
        styleUrls: ['auto-complete.component.css'],
    }),
    __metadata("design:paramtypes", [])
], AutoCompleteComponent);
exports.AutoCompleteComponent = AutoCompleteComponent;
//# sourceMappingURL=auto-complete.component.js.map