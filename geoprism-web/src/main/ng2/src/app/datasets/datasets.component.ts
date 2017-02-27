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

import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { FileSelectDirective, FileDropDirective, FileUploader, FileUploaderOptions } from 'ng2-file-upload/ng2-file-upload';

import { Dataset } from '../model/dataset';

import { EventService } from '../service/core.service';
import { LocalizationService } from '../service/localization.service';
import { DatasetService } from '../service/dataset.service';

import { UploadWizardComponent } from '../uploader/upload-wizard.component';

declare let acp: string;

@Component({
  
  selector: 'datasets',
  templateUrl: './datasets.component.html',
  styleUrls: ['./datasets.component.css']
})
export class DatasetsComponent implements OnInit {
  public datasets: Dataset[];

  public uploader:FileUploader;
  public dropActive:boolean = false;

  @ViewChild(UploadWizardComponent)
  private wizard: UploadWizardComponent;
  
  @ViewChild('uploadEl') 
  private uploadElRef: ElementRef;

  constructor(
    private router: Router,
    private datasetService: DatasetService,
    private localizationService: LocalizationService,
    private eventService: EventService) { }

  ngOnInit(): void {
    this.getDatasets();
    
    let options:FileUploaderOptions =  {
      autoUpload: true,
      queueLimit: 1,
      removeAfterUpload: true,
      url: acp + '/uploader/getAttributeInformation'      
    };    
    
    this.uploader = new FileUploader(options);
    this.uploader.onBeforeUploadItem = (fileItem: any) => {
      this.eventService.start();
    };    
    this.uploader.onCompleteItem = (item:any, response:any, status:any, headers:any) => {
      this.eventService.complete();
    };    
    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any) => {
      this.wizard.initialize(response);
    };
    this.uploader.onErrorItem = (item: any, response: string, status: number, headers: any) => {
      this.eventService.onError(response);	
    }
  }
  
  ngAfterViewInit() {
    this.uploader.onAfterAddingFile = (item => {
      this.uploadElRef.nativeElement.value = ''
    });
  }  
  
  getDatasets() : void {
    this.datasetService
      .getDatasets()
      .then(datasets => {
        this.datasets = datasets        
      })
  }
  
  remove(dataset: Dataset, event: any) : void {
//    let message = this.localizationService.localize("dataset", "removeContent");
//    message = message.replace('{0}', dataset.label);
//
//    if(confirm(message)) {
    
      this.datasetService
        .remove(dataset)
        .then(response => {
          this.datasets = this.datasets.filter(h => h !== dataset);    
        });
//    }
  }
  
  edit(dataset: Dataset, event: any) : void {
    this.router.navigate(['/dataset', dataset.id]);
  }
  
  fileOver(e:any):void {
    this.dropActive = e;
  }
  
  onSuccess(data: any): void {
    if(data.datasets != null) {
      this.addDatasets(data.datasets);
    }
  }
  
  getIndex(dataset: Dataset) {
    for(var i = 0; i < this.datasets.length; i++) {
      if(this.datasets[i].id == dataset.id) {
        return i;
      }
    }
      
    return -1;
  }
  
  addDatasets(datasets:Dataset[]) {
    for(let i = 0; i < datasets.length; i++) {
      let dataset = datasets[i];
      
      let index = this.getIndex(dataset)
        
      if(index == -1) {
        this.datasets.push(dataset);        
      }
      else {
        this.datasets[index] = dataset;
      }
    }
  }
}
