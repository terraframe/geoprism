import { Component, EventEmitter, Output, OnDestroy } from '@angular/core';

import { Subscription }   from 'rxjs/Subscription';

import * as _ from 'lodash';

import { Dataset } from '../model/dataset';
import { UploadInformation, Step, Sheet, Snapshot, Page, Locations, Problems, DatasetResponse } from './uploader-model';

import { LocalizationService } from '../../core/service/localization.service';

import { UploadService } from '../service/upload.service';
import { NavigationService } from './navigation.service';

declare let acp: string;

@Component({
  
  selector: 'upload-wizard',
  templateUrl: './upload-wizard.component.html',
  styleUrls: []
})
export class UploadWizardComponent implements OnDestroy {

  @Output() onSuccess = new EventEmitter<any>();
  
  steps : Step[];
  
  info: UploadInformation;
  sheet: Sheet;
  page: Page;
  problems: Problems;
  hasError: boolean = false;
  
  subscription: Subscription;
  
  isPersisted: boolean = false;
  
  constructor(
    private localizationService: LocalizationService,
    private uploadService: UploadService){ }
  
  ngOnDestroy(): void {
//    this.subscription.unsubscribe();
  }
  
  initialize(info: string): void {
    let uInfo = JSON.parse(info) as UploadInformation;
    
    if(uInfo.information.type === 'ETL') {
      this.info = uInfo;
      this.sheet = this.info.information.sheets[0];
      this.hasError = false;
      
      this.persist()
       
    }
    else {
      console.log("expected uInfo.information.type == ETL but was [" + uInfo.information.type + "]. Full data = [" + uInfo + "].");
      console.log(uInfo);
      window.location.href = acp + '/prism/home#/data/uploadmanager';      
    }
  }
  
    
  onSubmit(): void {
    this.onSuccess.emit();
    this.clear();
  }
    
  cancel(): void {
    if (this.isPersisted)
    {
      // invoking cancel on the server is going to delete the vault file which we don't want to do if there's a job history record
      this.clear();
    }
    else
    {
      this.uploadService.cancelImport(this.info.information)
        .then(response => {
          this.clear();
        });
    }    
  }
  
  clear(): void {
    this.info = null;
    this.sheet = null;
    this.page = null;
  
  }
  
    
  persist(): void {
    this.info.information.sheets[0] = _.cloneDeep(this.sheet) as Sheet;
  
      this.uploadService.importData(this.info.information)
        .then(result => {
          window.location.href = acp + '/prism/home#/data/uploadmanager';
        	
//          console.log("persist importData return")
//          if(result.success || (this.reconstructionJSON != null && this.reconstructionJSON != "")) {
//            this.clear();
//            console.log("onSuccess emit (importData)")
//            this.onSuccess.emit({datasets:result.datasets, finished : true});          
//          }
//          else {
//            this.afterPersist(result);
//          }         
        })
        .catch(error => {
          this.hasError = true;
        });
  }
  
  afterPersist(result: DatasetResponse): void {
    this.isPersisted = true;
  
//    this.onSuccess.emit({datasets:result.datasets, finished : false});
  }
  
  
  onSelectSheet(sheet: Sheet): void {

    this.page.snapshot = _.cloneDeep(this.sheet) as Sheet;
  
    // Go to summary page
//    let page = new Page('SUMMARY', this.page);
    
//    this.page = page;          
    this.sheet = sheet;
  }
  
}
