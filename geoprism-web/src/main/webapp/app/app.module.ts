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

import { AppComponent } from './app.component';
import { AppRoutingModule, routedComponents } from './app-routing.module';

import { LoadingBarComponent } from './core/loading-bar.component';
import { ErrorMessageComponent } from './core/error-message.component';
import { FilterPipe } from './core/filter.pipe';

import { DatasetService } from './service/dataset.service';
import { CategoryService } from './service/category.service';
import { EventService } from './service/core.service';

import { EventHttpService } from './service/event-http.service';


@NgModule({
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpModule
  ],
  declarations: [
    AppComponent,
    LoadingBarComponent,
    ErrorMessageComponent,
    FilterPipe,
    routedComponents
  ],
  providers: [
    DatasetService,
    CategoryService,
    EventService,
    { 
      provide : EventHttpService,
      useFactory: (xhrBackend: XHRBackend, requestOptions: RequestOptions, service: EventService) => {
        return new EventHttpService(xhrBackend, requestOptions, service)
      },
      deps: [XHRBackend, RequestOptions, EventService]
    }   
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
