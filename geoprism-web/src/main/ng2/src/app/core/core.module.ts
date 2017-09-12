import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule} from '@angular/forms';
import { HttpModule, XHRBackend, RequestOptions, Http} from '@angular/http';

import { AutoCompleteDirective } from './autocomplete/auto-complete.directive';
import { AutoCompleteComponent } from './autocomplete/auto-complete.component';

import { ConfirmModalDirective } from './confirm/confirm-modal.directive';
import { ConfirmModalComponent } from './confirm/confirm-modal.component';
import { ConfirmService } from './confirm/confirm-modal.service';

import { LocalizeComponent } from './localize/localize.component';
import { LocalizePipe } from './localize/localize.pipe';

import { LoadingBarComponent } from './loading-bar/loading-bar.component';

import { MessageComponent } from './message/message.component';

import { AsyncValidator } from './async-validator.directive';
import { FunctionValidator } from './function-validator.directive';
import { KeysPipe } from './keys.pipe';

import { BooleanFieldComponent } from './boolean-field/boolean-field.component';

import { EventService, IdService, BasicService} from './service/core.service';
import { LocalizationService } from './service/localization.service';
import { EventHttpService } from './service/event-http.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,  
    HttpModule
  ],
  declarations: [
    AutoCompleteDirective,
    AutoCompleteComponent,
    
    ConfirmModalDirective,
    ConfirmModalComponent,
    	  
    LocalizeComponent,
    LoadingBarComponent,
    MessageComponent,
    AsyncValidator,
    FunctionValidator,
    KeysPipe,
    LocalizePipe,
    
    BooleanFieldComponent
  ],
  providers: [
    ConfirmService,
    LocalizationService,
    IdService,
    EventService,
    { 
      provide : EventHttpService,
      useFactory: (xhrBackend: XHRBackend, requestOptions: RequestOptions, service: EventService) => {
        return new EventHttpService(xhrBackend, requestOptions, service)
      },
      deps: [XHRBackend, RequestOptions, EventService]
    }   
  ],
  exports: [
	  
    AutoCompleteDirective,
    AutoCompleteComponent,
    
    ConfirmModalDirective,
    ConfirmModalComponent,
    	  
    LocalizeComponent,
    LoadingBarComponent,
    MessageComponent,
    AsyncValidator,
    FunctionValidator,
    KeysPipe,
    LocalizePipe,
    
    BooleanFieldComponent
  ],
  entryComponents: [AutoCompleteComponent]  
})
export class CoreModule { }
