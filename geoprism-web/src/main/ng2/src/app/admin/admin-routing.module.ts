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
import { SystemLogosComponent} from './logo/system-logos.component';

declare var acp: any;

const routes: Routes = [
  {
    path: '',
    redirectTo: '/logos',
    pathMatch: 'full'
  },
  {
    path: 'logos',
    component: SystemLogosComponent
  },
  {
    path: 'logo/:id',
    component: SystemLogoComponent,
  },
  {
    path: 'email',
    component: EmailComponent
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}]
})
export class AdminRoutingModule { }

export const routedComponents = [SystemLogosComponent, SystemLogoComponent, EmailComponent];
