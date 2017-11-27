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
import { CommonModule } from '@angular/common';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import './rxjs-extensions';

import { FileUploadModule } from 'ng2-file-upload/ng2-file-upload';
import { CustomFormsModule } from 'ng2-validation'
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { ModalModule } from 'ngx-bootstrap/modal'
import { PasswordStrengthBarModule } from 'ng2-password-strength-bar';

import { AppComponent } from './app.component';
import { AppRoutingModule, routedComponents } from './app-routing.module';

import { CoreModule } from './core/core.module';

import { SessionService } from './authentication/session.service';
import { ProfileService } from './profile/profile.service';
import { ProfileComponent } from './profile/profile.component';

import { HubService } from './hub/hub.service';
import { ForgotPasswordService } from './forgotpassword/forgotpassword.service';
import { ForgotPasswordCompleteService } from './forgotpassword-complete/forgotpassword-complete.service';
import { AuthGuard } from './authentication/auth.guard';
import { AdminGuard } from './authentication/admin.guard';

@NgModule({
  imports: [
	BrowserModule,
	FormsModule,
	HttpModule,	
	CommonModule,
    CoreModule,	  
    AppRoutingModule,
    FileUploadModule,
    BsDropdownModule.forRoot(),
    ModalModule.forRoot(),    
    CustomFormsModule,    
    PasswordStrengthBarModule,
  ],
  declarations: [
	// Global components
    AppComponent,
    ProfileComponent,
    
    // Routing components
    routedComponents
  ],
  providers: [
    ProfileService,
    SessionService,
    ForgotPasswordService,
    ForgotPasswordCompleteService,
    HubService,
    AuthGuard,
    AdminGuard
  ],
  bootstrap: [AppComponent],
  entryComponents: [
    ProfileComponent
  ],      
})
export class AppModule { }
