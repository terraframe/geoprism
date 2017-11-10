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

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule} from '@angular/forms';
import { HttpModule, XHRBackend, RequestOptions, Http} from '@angular/http';

import { FileUploadModule } from 'ng2-file-upload/ng2-file-upload';
import { CustomFormsModule } from 'ng2-validation'
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ModalModule } from 'ngx-bootstrap/modal'
import { NgxPaginationModule } from 'ngx-pagination';

import { CoreModule } from '../core/core.module';

import { SystemLogoService } from './logo/system-logo.service';
import { EmailService } from './email/email.service';
import { AccountService } from './account/account.service';
import { AdminRoutingModule, routedComponents } from './admin-routing.module';

import { AdminComponent } from './admin.component'

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    FileUploadModule,
    BsDropdownModule.forRoot(),
    ModalModule.forRoot(),    
    CustomFormsModule,
    NgxPaginationModule,    
    AdminRoutingModule,
    CoreModule
  ],
  declarations: [
	// Global components
	AdminComponent,
	routedComponents
  ],
  providers: [
    SystemLogoService,
    EmailService,
    AccountService
  ],
  bootstrap: [AdminComponent],  
  exports: [	
	AdminComponent,
    routedComponents
  ]
})
export class AdminModule { }
