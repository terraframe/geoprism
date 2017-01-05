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

import { Component, EventEmitter, Input, OnInit, OnChanges, Output, Inject, ViewChild } from '@angular/core';
import { ActivatedRoute, Params, Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Location } from '@angular/common';
import { NgForm} from '@angular/forms';

import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/switchMap';

import { Dataset } from '../model/dataset';
import { BasicCategory } from '../model/basic-category';
import { DatasetService } from '../service/dataset.service';

export class DatasetResolver implements Resolve<Dataset> {
  constructor(@Inject(DatasetService) private datasetService: DatasetService) {}
  
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot):Promise<Dataset> {
    return this.datasetService.edit(route.params['id']);
  }
}

@Component({
  moduleId: module.id,
  selector: 'dataset-detail',
  templateUrl: 'dataset-detail.component.jsp',
  styleUrls: []
})
export class DatasetDetailComponent implements OnInit {
  @Input() dataset: Dataset;
  @Output() close = new EventEmitter();
  @ViewChild('form') public form: NgForm;

  validName: boolean = true;
  error: any;

  constructor(
    private datasetService: DatasetService,
    private router: Router,      
    private route: ActivatedRoute,
    private location: Location) {
  }

  ngOnInit(): void {
    this.dataset = this.route.snapshot.data['dataset'];
  }
  
  validateName(name: string) {
    this.datasetService.validateDatasetName(name, this.dataset.id)
      .then((response:any) => {
        this.validName = true;
      })
     .catch((error:any) => {
        this.validName = false;       
     });        
  }  
  
  cancel(): void {
    this.datasetService.unlock(this.dataset)
     .then(response => {
       this.goBack(this.dataset);
     })
     .catch(error => this.error = error);    
  } 
  
  onSubmit(): void {
    this.datasetService.apply(this.dataset)
      .then(dataset => {
        this.goBack(dataset);
      })
      .catch(error => {
        this.error = error;
      });        
  }
  
  open(category: BasicCategory, event: any) : void {
    this.router.navigate(['/category', category.id]);
  }
  
  goBack(dataset : Dataset): void {
    this.close.emit(dataset);
    
    this.location.back();
  }
}
