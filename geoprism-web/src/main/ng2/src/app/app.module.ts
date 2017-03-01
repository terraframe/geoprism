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

import './rxjs-extensions';

import { FileUploadModule } from 'ng2-file-upload/ng2-file-upload';
import { DropdownModule } from 'ng2-bootstrap'

import { AppComponent } from './app.component';
import { AppRoutingModule, routedComponents } from './app-routing.module';

import { LocalizeComponent } from './core/localize.component';
import { LoadingBarComponent } from './core/loading-bar.component';
import { MessageComponent } from './core/message.component';
import { AsyncValidator } from './core/async-validator.directive';
import { FunctionValidator } from './core/function-validator.directive';
import { KeysPipe } from './core/keys.pipe';
import { LocalizePipe } from './core/localize.pipe';

import { AutoCompleteDirective } from './autocomplete/auto-complete.directive';
import { AutoCompleteComponent } from './autocomplete/auto-complete.component';

import { ConfirmModalDirective } from './confirm/confirm-modal.directive';
import { ConfirmModalComponent } from './confirm/confirm-modal.component';
import { ConfirmService } from './confirm/confirm-modal.service';

import { EventService, IdService} from './service/core.service';
import { LocalizationService } from './service/localization.service';

import { DatasetService } from './service/dataset.service';
import { CategoryService } from './service/category.service';
import { IconService } from './service/icon.service';

// Upload wizard imports
import { NavigationService } from './uploader/navigation.service';
import { UploadWizardComponent } from './uploader/upload-wizard.component';
import { PagingComponent } from './uploader/paging.component';
import { MatchInitialPageComponent } from './uploader/match-initial-page.component';
import { MatchPageComponent } from './uploader/match-page.component';
import { BeginningInfoPageComponent } from './uploader/beginning-info-page.component';
import { NamePageComponent } from './uploader/name-page.component';
import { AttributesPageComponent } from './uploader/attributes-page.component';
import { LocationPageComponent } from './uploader/location-page.component';
import { CoordinatePageComponent } from './uploader/coordinate-page.component';
import { SummaryPageComponent } from './uploader/summary-page.component';
import { GeoValidationPageComponent } from './uploader/geo-validation-page.component';
import { GeoValidationProblemComponent } from './uploader/geo-validation-problem.component';
import { CategoryValidationPageComponent } from './uploader/category-validation-page.component';
import { CategoryValidationProblemComponent } from './uploader/category-validation-problem.component';
import { UploadService } from './service/upload.service';

import { EventHttpService } from './service/event-http.service';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule,
    FileUploadModule,
    DropdownModule.forRoot()
  ],
  declarations: [
	// Global components
    AppComponent,
    LocalizeComponent,
    LoadingBarComponent,
    MessageComponent,
    AsyncValidator,
    FunctionValidator,
    KeysPipe,
    LocalizePipe,
    
    AutoCompleteDirective,
    AutoCompleteComponent,
    
    ConfirmModalDirective,
    ConfirmModalComponent,
    
    // Upload Wizard components
    UploadWizardComponent,
    PagingComponent,
    MatchInitialPageComponent,
    MatchPageComponent,
    BeginningInfoPageComponent,
    NamePageComponent,
    AttributesPageComponent,
    LocationPageComponent,
    CoordinatePageComponent,
    SummaryPageComponent,
    GeoValidationPageComponent,
    GeoValidationProblemComponent,
    CategoryValidationPageComponent,
    CategoryValidationProblemComponent,
    
    // Routing components
    routedComponents
  ],
  providers: [
	LocalizationService,
	IdService,
    DatasetService,
    CategoryService,
    IconService,
    UploadService,
    NavigationService,
    ConfirmService,
    EventService,
    { 
      provide : EventHttpService,
      useFactory: (xhrBackend: XHRBackend, requestOptions: RequestOptions, service: EventService) => {
        return new EventHttpService(xhrBackend, requestOptions, service)
      },
      deps: [XHRBackend, RequestOptions, EventService]
    }   
  ],
  bootstrap: [AppComponent],
  entryComponents: [AutoCompleteComponent]
})
export class AppModule { }
