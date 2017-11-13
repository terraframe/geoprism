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

import { EmailComponent } from './email/email.component';
import { SystemLogoComponent } from './logo/system-logo.component';
import { SystemLogosComponent } from './logo/system-logos.component';
import { AccountsComponent } from './account/accounts.component';
import { AccountComponent, AccountResolver } from './account/account.component';
import { GeoTreeComponent } from './geotree/geotree.component';
import { UniversalTreeComponent } from './universaltree/universaltree.component';
import { ClassifierTreeComponent } from './classifiertree/classifiertree.component';
import { BrowserComponent } from './browser/browser.component';

import { AuthGuardService } from '../core/authentication/auth-guard.service';

declare var acp: any;

const routes: Routes = [
  {
    path: '',
    redirectTo: '/accounts',
    pathMatch: 'full'
  },
  {
    path: 'logos',
    component: SystemLogosComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'logo/:id',
    component: SystemLogoComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'email',
    component: EmailComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'accounts',
    component: AccountsComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'geotree',
    component: GeoTreeComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'universaltree',
    component: UniversalTreeComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'classifiertree',
    component: ClassifierTreeComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'browser',
    component: BrowserComponent,
    canActivate: [ AuthGuardService ]
  },
  {
    path: 'account/:id',
    component: AccountComponent,
    canActivate: [ AuthGuardService ],
    resolve: {
      account: AccountResolver
    }        
  },  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}, AccountResolver]
})
export class AdminRoutingModule { }

export const routedComponents = [SystemLogosComponent, SystemLogoComponent, EmailComponent, AccountsComponent, AccountComponent, GeoTreeComponent, UniversalTreeComponent, ClassifierTreeComponent, BrowserComponent];
