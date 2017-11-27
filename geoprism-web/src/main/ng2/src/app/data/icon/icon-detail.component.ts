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

import { Component, OnInit, ViewChild, ElementRef, Inject, Input } from '@angular/core';
import { ActivatedRoute, Params, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Location } from '@angular/common';

import { FileSelectDirective, FileDropDirective, FileUploader, FileUploaderOptions } from 'ng2-file-upload/ng2-file-upload';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { EventService } from '../../core/service/core.service';

import { Icon } from '../model/icon';
import { IconService } from '../service/icon.service';

export class IconResolver implements Resolve<Icon> {
  constructor(@Inject(IconService) private iconService: IconService, @Inject(EventService) private eventService: EventService) {}
  
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):Promise<Icon> {
    let id = route.params['id'];
  
    if(id !== "-1") {
      return this.iconService.edit(id)
        .catch((error:any) => {
          this.eventService.onError(error); 
        
          return Promise.reject(error);
        });
    }
    else {
      return new Promise<Icon>((resolve, reject) => {
        resolve(new Icon());
      });    
    }
  }
}

declare var acp: any;

@Component({
  
  selector: 'icon-detail',
  templateUrl: './icon-detail.component.html',
  styleUrls: []
})
export class IconDetailComponent implements OnInit {
  @Input() icon: Icon;

  public uploader:FileUploader;
  public dropActive:boolean = false;

  @ViewChild('uploadEl') 
  private uploadElRef: ElementRef;  
  
  private file: any;
  private context: string;

  constructor(
    private router: Router,      
    private route: ActivatedRoute,
    private location: Location,
    private iconService: IconService,
    private eventService: EventService) {
    this.context = acp as string;	  
  }

  ngOnInit(): void {
    this.icon = this.route.snapshot.data['icon'];
    
    let url = acp + '/iconimage/create';
    
    if(this.icon.id != null) {
      url = acp + '/iconimage/apply';      
    }
        
    let options:FileUploaderOptions = {
      autoUpload: false,
      queueLimit: 1,
      removeAfterUpload: true,
      url: url
    };    
    
    this.uploader = new FileUploader(options);
    this.uploader.onBeforeUploadItem = (fileItem: any) => {
      this.eventService.start();
    };    
    this.uploader.onCompleteItem = (item:any, response:any, status:any, headers:any) => {
      this.eventService.complete();
    };    
    this.uploader.onSuccessItem = (item: any, response: string, status: number, headers: any) => {
      this.location.back();
    };
    this.uploader.onErrorItem = (item: any, response: string, status: number, headers: any) => {
      this.eventService.onError(response);  
    };
    this.uploader.onBuildItemForm = (fileItem: any, form: any) => {
      form.append('label', this.icon.label);
      
      if(this.icon.id != null) {
        form.append('id', this.icon.id);        
      }
    };        
  }
  
  ngAfterViewInit() {
    let that = this;
  
    this.uploader.onAfterAddingFile = (item => {
      this.uploadElRef.nativeElement.value = ''
        
      let reader = new FileReader();
        reader.onload = function(e: any) {
        that.file = reader.result;
      };
      reader.readAsDataURL(item._file);
      
      if(that.icon.label == null || that.icon.label == '') {
        that.icon.label = item.file.name.replace(".png", "");    	  
      }
    });
  }
  
  fileOver(e:any):void {
    this.dropActive = e;
  }  
  
  cancel(): void {
    if(this.icon.id != null) {
      this.iconService.unlock(this.icon.id)
        .then((response:any) => {
          this.location.back();
        })      
    }
    else {
      this.location.back();      
    }
  } 
  
  onSubmit(): void {
    if(this.file == null) {
      this.iconService.apply(this.icon.id, this.icon.label)
        .then(icon => {
          this.location.back();
        });
    }
    else {    
      this.uploader.uploadAll();      
    }
  }  
  
  clear(): void {
    this.file = null;
    this.icon.filePath = null;
    
    this.uploader.clearQueue()    
  }
}
