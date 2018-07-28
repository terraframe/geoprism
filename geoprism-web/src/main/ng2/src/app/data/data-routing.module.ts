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
import { IndicatorModalComponent } from './datasets/indicator-modal.component';

import { CategoriesComponent } from './category/categories.component';
import { CategoryDetailComponent, CategoryResolver} from './category/category-detail.component';
import { OptionDetailComponent, OptionResolver} from './category/option-detail.component';

import { DHIS2IdFinderComponent } from './dhis2/dhis2-id-finder.component';

import { UploadManagerComponent } from './upload-manager/upload-manager.component';

declare var acp: any;

const routes: Routes = [
  {
    path: '',
    component: DatasetsComponent,
    data: { title: 'dataset.title' }        
  },
  {
    path: 'dhis2',
    component: DHIS2IdFinderComponent,    
    data: { title: 'dhis2.id.finder.title' }            
  },
  {
    path: 'datasets',
    component: DatasetsComponent,    
    data: { title: 'dataset.title' }            
  },
  {
    path: 'datasets/:historyId/:pageNum',
    component: DatasetsComponent,
    data: { title: 'dataset.title' }                        
  },
  {
    path: 'dataset/:id',
    component: DatasetDetailComponent,
    resolve: {
      dataset: DatasetResolver
    },
    data: { title: 'dataset.title' }                
  },
  {
    path: 'categories',
    component: CategoriesComponent, 
    data: { title: 'category.management.title' }                
  },
  {
    path: 'category/:id',
    component: CategoryDetailComponent,
    resolve: {
      category: CategoryResolver
    },
    data: { title: 'category.management.title' }                    
  },  
  {
    path: 'category-option/:parentId/:id',
    component: OptionDetailComponent,
    resolve: {
      category: OptionResolver
    },
    data: { title: 'category.management.title' }                    
  },
  {
    path: 'icons',
    component: IconsComponent,    
    data: { title: 'category.icon.title' }                    
  },
  {
    path: 'icon/:id',
    component: IconDetailComponent,
    resolve: {
      icon: IconResolver
    },
    data: { title: 'category.icon.title' }                        
  },
  {
    path: 'uploadmanager',
    component: UploadManagerComponent,
    data: { title: 'upload.manager.title' }                        
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}, DatasetResolver, CategoryResolver, OptionResolver, IconResolver]
})
export class DataRoutingModule { }

export const routedComponents = [UploadManagerComponent, DatasetsComponent, DatasetDetailComponent, CategoriesComponent, CategoryDetailComponent, OptionDetailComponent, IconsComponent, IconDetailComponent, IndicatorModalComponent];
