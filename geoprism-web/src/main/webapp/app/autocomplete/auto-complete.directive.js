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
var auto_complete_component_1 = require("./auto-complete.component");
/**
 * display auto-complete section with input and dropdown list when it is clicked
 */
var AutoCompleteDirective = (function () {
    function AutoCompleteDirective(resolver, viewContainerRef, el) {
        this.resolver = resolver;
        this.viewContainerRef = viewContainerRef;
        this.el = el;
        // The directive emits onDropdownSelect event when an item from the list is selected
        this.onDropdownSelect = new core_1.EventEmitter();
        this.term = "";
        this.listCmp = undefined;
        this.refreshTimer = undefined;
        this.searchInProgress = false;
        this.searchRequired = false;
    }
    AutoCompleteDirective.prototype.ngOnInit = function () {
        var _this = this;
        // When an item is selected remove the list
        this.onDropdownSelect.subscribe(function () {
            _this.removeList();
        });
    };
    /**
     * On key event is triggered when a key is released on the host component
     * the event starts a timer to prevent concurrent requests
     */
    AutoCompleteDirective.prototype.onKey = function (event) {
        var _this = this;
        if (!this.refreshTimer) {
            this.refreshTimer = setTimeout(function () {
                if (!_this.searchInProgress) {
                    _this.doSearch();
                }
                else {
                    // If a request is in progress mark that a new search is required
                    _this.searchRequired = true;
                }
            }, 200);
        }
        this.term = event.target.value;
        if (this.term === "" && this.listCmp) {
            // clean the list if the search term is empty
            this.removeList();
        }
    };
    /**
     * Call the search function and handle the results
     */
    AutoCompleteDirective.prototype.doSearch = function () {
        var _this = this;
        this.refreshTimer = undefined;
        // if we have a search function and a valid search term call the search
        if (this.source && this.term !== "" && this.term.length > 1) {
            this.searchInProgress = true;
            this.source(this.term).then(function (res) {
                _this.searchInProgress = false;
                // if the term has changed during our search do another search
                if (_this.searchRequired) {
                    _this.searchRequired = false;
                    _this.doSearch();
                }
                else {
                    // display the list of results
                    _this.displayList(res);
                }
            })
                .catch(function (err) {
                _this.removeList();
            });
        }
        else if (this.term === "") {
            this.onDropdownSelect.emit({ text: '', data: null });
        }
    };
    /**
     * Display the list of results
     * Dynamically load the list component if it doesn't exist yet and update the suggestions list
     */
    AutoCompleteDirective.prototype.displayList = function (list) {
        var _this = this;
        if (!this.listCmp) {
            var factory = this.resolver.resolveComponentFactory(auto_complete_component_1.AutoCompleteComponent);
            this.listCmp = this.viewContainerRef.createComponent(factory);
            this.updateList(list);
            // Emit the selectd event when the component fires its selected event
            (this.listCmp.instance).onDropdownSelect.subscribe(function (selectedItem) {
                _this.el.nativeElement.value = selectedItem.text;
                _this.onDropdownSelect.emit(selectedItem);
            });
        }
        else {
            this.updateList(list);
        }
    };
    /**
     * Update the suggestions list in the list component
     */
    AutoCompleteDirective.prototype.updateList = function (list) {
        if (this.listCmp) {
            (this.listCmp.instance).list = list;
        }
    };
    /**
     * remove the list component
     */
    AutoCompleteDirective.prototype.removeList = function () {
        this.searchInProgress = false;
        this.searchRequired = false;
        if (this.listCmp) {
            this.listCmp.destroy();
            this.listCmp = undefined;
        }
    };
    return AutoCompleteDirective;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", Function)
], AutoCompleteDirective.prototype, "source", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], AutoCompleteDirective.prototype, "onDropdownSelect", void 0);
AutoCompleteDirective = __decorate([
    core_1.Directive({
        selector: "[auto-complete], [auto-complete]",
        host: {
            "(keyup)": "onKey($event)" // Liten to keyup events on the host component
        }
    }),
    __metadata("design:paramtypes", [core_1.ComponentFactoryResolver,
        core_1.ViewContainerRef,
        core_1.ElementRef])
], AutoCompleteDirective);
exports.AutoCompleteDirective = AutoCompleteDirective;
//# sourceMappingURL=auto-complete.directive.js.map