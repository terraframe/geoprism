///
/// Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
///
/// This file is part of Runway SDK(tm).
///
/// Runway SDK(tm) is free software: you can redistribute it and/or modify
/// it under the terms of the GNU Lesser General Public License as
/// published by the Free Software Foundation, either version 3 of the
/// License, or (at your option) any later version.
///
/// Runway SDK(tm) is distributed in the hope that it will be useful, but
/// WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU Lesser General Public License for more details.
///
/// You should have received a copy of the GNU Lesser General Public
/// License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
///
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
var platform_browser_1 = require("@angular/platform-browser");
var forms_1 = require("@angular/forms");
var http_1 = require("@angular/http");
require("./rxjs-extensions");
var app_component_1 = require("./app.component");
var loading_bar_component_1 = require("./core/loading-bar.component");
var error_message_component_1 = require("./core/error-message.component");
var app_routing_module_1 = require("./app-routing.module");
var dataset_service_1 = require("./service/dataset.service");
var category_service_1 = require("./service/category.service");
var event_service_1 = require("./service/event.service");
var event_http_service_1 = require("./service/event-http.service");
var AppModule = (function () {
    function AppModule() {
    }
    return AppModule;
}());
AppModule = __decorate([
    core_1.NgModule({
        imports: [
            platform_browser_1.BrowserModule,
            forms_1.FormsModule,
            app_routing_module_1.AppRoutingModule,
            http_1.HttpModule
        ],
        declarations: [
            app_component_1.AppComponent,
            loading_bar_component_1.LoadingBarComponent,
            error_message_component_1.ErrorMessageComponent,
            app_routing_module_1.routedComponents
        ],
        providers: [
            dataset_service_1.DatasetService,
            category_service_1.CategoryService,
            event_service_1.EventService,
            {
                provide: event_http_service_1.EventHttpService,
                useFactory: function (xhrBackend, requestOptions, service) {
                    return new event_http_service_1.EventHttpService(xhrBackend, requestOptions, service);
                },
                deps: [http_1.XHRBackend, http_1.RequestOptions, event_service_1.EventService]
            }
        ],
        bootstrap: [app_component_1.AppComponent]
    }),
    __metadata("design:paramtypes", [])
], AppModule);
exports.AppModule = AppModule;
//# sourceMappingURL=app.module.js.map