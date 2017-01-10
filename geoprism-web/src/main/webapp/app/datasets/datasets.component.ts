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

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { FileSelectDirective, FileDropDirective, FileUploader } from 'ng2-file-upload/ng2-file-upload';

import { Dataset } from '../model/dataset';

import { EventService } from '../service/core.service';
import { LocalizationService } from '../service/localization.service';
import { DatasetService } from '../service/dataset.service';


declare var acp: string;

@Component({
  moduleId: module.id,
  selector: 'datasets',
  templateUrl: 'datasets.component.jsp',
  styleUrls: ['datasets.component.css']
})
export class DatasetsComponent implements OnInit {
  public datasets: Dataset[];

  public uploader:FileUploader;
  public dropActive:boolean = false;

  constructor(
    private router: Router,
    private datasetService: DatasetService,
    private localizationService: LocalizationService,
    private eventService: EventService) { }

  ngOnInit(): void {
    this.getDatasets();
    
    this.uploader = new FileUploader({url: acp + '/uploader/getAttributeInformation'});
    this.uploader.onAfterAddingFile = (fileItem: any) => {
      this.uploader.uploadItem(fileItem);
    };    
    this.uploader.onBeforeUploadItem = (fileItem: any) => {
      this.eventService.start();
    };    
    this.uploader.onCompleteItem = (item:any, response:any, status:any, headers:any) => {
      this.eventService.complete();
    };    
    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any) => {
      console.log('File Uploaded: ' + response)
    };
    this.uploader.onErrorItem = (item: any, response: string, status: number, headers: any) => {
      this.eventService.onError(response);	
    }
  }
  
  getDatasets() : void {
    this.datasetService
      .getDatasets()
      .then(datasets => {
        this.datasets = datasets        
      })
  }
  
  remove(dataset: Dataset, event: any) : void {
    let message = this.localizationService.localize("dataset", "removeContent");
    message = message.replace('{0}', dataset.label);

    if(confirm(message)) {
    
      this.datasetService
        .remove(dataset)
        .then(response => {
          this.datasets = this.datasets.filter(h => h !== dataset);    
        });
    }
  }
  
  edit(dataset: Dataset, event: any) : void {
    this.router.navigate(['/dataset', dataset.id]);
  }
  
  fileOver(e:any):void {
    this.dropActive = e;
  }
}
