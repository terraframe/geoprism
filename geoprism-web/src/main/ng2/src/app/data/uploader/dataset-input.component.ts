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
import { ActivatedRoute, Params, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';

import { BsModalService } from 'ngx-bootstrap/modal';
import { BsModalRef } from 'ngx-bootstrap/modal/bs-modal-ref.service';

import { FileSelectDirective, FileDropDirective, FileUploader, FileUploaderOptions } from 'ng2-file-upload/ng2-file-upload';

import { EventService } from '../../core/service/core.service';

import { UploadWizardComponent } from './upload-wizard.component';
import { UploadResultComponent } from './upload-result.component';

declare let acp: string;

@Component({
  selector: 'dataset-input',
  templateUrl: 'dataset-input.component.html',
  styleUrls: []
})
export class DatasetInputComponent implements OnInit {
  public uploader:FileUploader;
  public dropActive:boolean = false;
  
  @ViewChild(UploadWizardComponent)
  private wizard: UploadWizardComponent;
  
  @ViewChild('uploadEl') 
  private uploadElRef: ElementRef;
  
  bsModalRef: BsModalRef;  

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private eventService: EventService,
    private modalService: BsModalService) { }

  ngOnInit(): void {
    
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
//      this.wizard.setReconstructionJSON(this.reconstructionJSON);
    };
    this.uploader.onErrorItem = (item: any, response: string, status: number, headers: any) => {
      this.eventService.onError(response);	
    }
  };
  
  ngAfterViewInit() {
    this.uploader.onAfterAddingFile = (item => {
      this.uploadElRef.nativeElement.value = '';
    });
  }
  
  fileOver(e:any):void {
    this.dropActive = e;
  }
  
}
