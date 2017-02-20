"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require("@angular/core");
var Subject_1 = require("rxjs/Subject");
var NavigationService = (function () {
    function NavigationService() {
        // Observable string sources
        this.navigationSource = new Subject_1.Subject();
        // Observable string streams
        this.navigationAnnounced$ = this.navigationSource.asObservable();
    }
    // Service message commands
    NavigationService.prototype.navigate = function (direction) {
        this.navigationSource.next(direction);
    };
    return NavigationService;
}());
NavigationService = __decorate([
    core_1.Injectable()
], NavigationService);
exports.NavigationService = NavigationService;
//# sourceMappingURL=navigation.service.js.map