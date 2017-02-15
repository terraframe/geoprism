"use strict";
var __extends = (this && this.__extends) || function (d, b) {
    for (var p in b) if (b.hasOwnProperty(p)) d[p] = b[p];
    function __() { this.constructor = d; }
    d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
};
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
var http_1 = require("@angular/http");
require("rxjs/add/operator/finally");
var core_service_1 = require("./core.service");
var EventHttpService = (function (_super) {
    __extends(EventHttpService, _super);
    function EventHttpService(_backend, _defaultOptions, service) {
        var _this = _super.call(this, _backend, _defaultOptions) || this;
        _this.service = service;
        _this.currentRequests = 0;
        return _this;
    }
    EventHttpService.prototype.get = function (url, options) {
        var _this = this;
        this.incrementRequestCount();
        var response = _super.prototype.get.call(this, url, options).finally(function () {
            _this.decrementRequestCount();
        });
        return response;
    };
    EventHttpService.prototype.post = function (url, body, options) {
        var _this = this;
        this.incrementRequestCount();
        var response = _super.prototype.post.call(this, url, body, options).finally(function () {
            _this.decrementRequestCount();
        });
        return response;
    };
    EventHttpService.prototype.decrementRequestCount = function () {
        if (--this.currentRequests == 0) {
            this.service.complete();
        }
    };
    EventHttpService.prototype.incrementRequestCount = function () {
        if (this.currentRequests++ == 0) {
            this.service.start();
        }
    };
    return EventHttpService;
}(http_1.Http));
EventHttpService = __decorate([
    core_1.Injectable(),
    __metadata("design:paramtypes", [http_1.ConnectionBackend, http_1.RequestOptions, core_service_1.EventService])
], EventHttpService);
exports.EventHttpService = EventHttpService;
//# sourceMappingURL=event-http.service.js.map