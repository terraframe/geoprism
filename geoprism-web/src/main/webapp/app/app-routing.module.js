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
var core_1 = require("@angular/core");
var common_1 = require("@angular/common");
var router_1 = require("@angular/router");
var icons_component_1 = require("./icon/icons.component");
var icon_detail_component_1 = require("./icon/icon-detail.component");
var datasets_component_1 = require("./datasets/datasets.component");
var dataset_detail_component_1 = require("./datasets/dataset-detail.component");
var categories_component_1 = require("./category/categories.component");
var category_detail_component_1 = require("./category/category-detail.component");
var option_detail_component_1 = require("./category/option-detail.component");
var routes = [
    {
        path: '',
        redirectTo: '/datasets',
        pathMatch: 'full'
    },
    {
        path: 'datasets',
        component: datasets_component_1.DatasetsComponent
    },
    {
        path: 'dataset/:id',
        component: dataset_detail_component_1.DatasetDetailComponent,
        resolve: {
            dataset: dataset_detail_component_1.DatasetResolver
        }
    },
    {
        path: 'categories',
        component: categories_component_1.CategoriesComponent
    },
    {
        path: 'category/:id',
        component: category_detail_component_1.CategoryDetailComponent,
        resolve: {
            category: category_detail_component_1.CategoryResolver
        }
    },
    {
        path: 'category-option/:parentId/:id',
        component: option_detail_component_1.OptionDetailComponent,
        resolve: {
            category: option_detail_component_1.OptionResolver
        }
    },
    {
        path: 'icons',
        component: icons_component_1.IconsComponent
    },
    {
        path: 'icon/:id',
        component: icon_detail_component_1.IconDetailComponent,
        resolve: {
            icon: icon_detail_component_1.IconResolver
        }
    },
];
var AppRoutingModule = (function () {
    function AppRoutingModule() {
    }
    return AppRoutingModule;
}());
AppRoutingModule = __decorate([
    core_1.NgModule({
        imports: [router_1.RouterModule.forRoot(routes)],
        exports: [router_1.RouterModule],
        providers: [{ provide: common_1.LocationStrategy, useClass: common_1.HashLocationStrategy }, dataset_detail_component_1.DatasetResolver, category_detail_component_1.CategoryResolver, option_detail_component_1.OptionResolver, icon_detail_component_1.IconResolver]
    })
], AppRoutingModule);
exports.AppRoutingModule = AppRoutingModule;
exports.routedComponents = [datasets_component_1.DatasetsComponent, dataset_detail_component_1.DatasetDetailComponent, categories_component_1.CategoriesComponent, category_detail_component_1.CategoryDetailComponent, option_detail_component_1.OptionDetailComponent, icons_component_1.IconsComponent, icon_detail_component_1.IconDetailComponent];
//# sourceMappingURL=app-routing.module.js.map