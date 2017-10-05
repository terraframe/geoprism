import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule} from '@angular/forms';

import { ProgressbarModule } from 'ngx-bootstrap/progressbar';

import { ProgressBarComponent } from './progress-bar.component';
import { ProgressService } from './progress.service';

@NgModule({
  imports: [
    BrowserModule,
    FormsModule,  	  
    ProgressbarModule.forRoot()   
  ],	
  declarations: [
    ProgressBarComponent,    
  ],
  providers: [
    ProgressService
  ],
  exports: [	  
    ProgressBarComponent,
  ]
})
export class ProgressModule { }
