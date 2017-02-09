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

import { NgModule, Injectable, Inject } from '@angular/core';
import { LocationStrategy, HashLocationStrategy } from '@angular/common';
import { Routes, RouterModule, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable } from 'rxjs/Observable';

import { IconsComponent } from './icon/icons.component';
import { IconDetailComponent, IconResolver} from './icon/icon-detail.component';

import { DatasetsComponent } from './datasets/datasets.component';
import { DatasetDetailComponent, DatasetResolver} from './datasets/dataset-detail.component';

import { CategoriesComponent } from './category/categories.component';
import { CategoryDetailComponent, CategoryResolver} from './category/category-detail.component';
import { OptionDetailComponent, OptionResolver} from './category/option-detail.component';

declare var acp: any;

const routes: Routes = [
  {
    path: '',
    redirectTo: '/datasets',
    pathMatch: 'full'
  },
  {
    path: 'datasets',
    component: DatasetsComponent
  },
  {
    path: 'dataset/:id',
    component: DatasetDetailComponent,
    resolve: {
      dataset: DatasetResolver
    }    
  },
  {
	  path: 'categories',
	  component: CategoriesComponent
  },
  {
    path: 'category/:id',
    component: CategoryDetailComponent,
    resolve: {
      category: CategoryResolver
    }    
  },  
  {
    path: 'category-option/:parentId/:id',
    component: OptionDetailComponent,
    resolve: {
      category: OptionResolver
    }    
  },
  {
    path: 'icons',
    component: IconsComponent
  },
  {
    path: 'icon/:id',
    component: IconDetailComponent,
    resolve: {
      icon: IconResolver
    }    
  },
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}, DatasetResolver, CategoryResolver, OptionResolver, IconResolver]
})
export class AppRoutingModule { }

export const routedComponents = [DatasetsComponent, DatasetDetailComponent, CategoriesComponent, CategoryDetailComponent, OptionDetailComponent, IconsComponent, IconDetailComponent];
