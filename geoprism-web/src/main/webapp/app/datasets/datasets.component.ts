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

import { Dataset } from '../model/dataset';
import { DatasetService } from '../service/dataset.service';

@Component({
  moduleId: module.id,
  selector: 'datasets',
  templateUrl: 'datasets.component.jsp',
  styleUrls: ['datasets.component.css']
})
export class DatasetsComponent implements OnInit {
  datasets: Dataset[];

  constructor(
    private router: Router,
    private datasetService: DatasetService) { }

  getDatasets() : void {
    this.datasetService
      .getDatasets()
      .then(datasets => {
        this.datasets = datasets    	  
      })
  }
  
  remove(dataset: Dataset, event: any) : void {
    this.datasetService
      .remove(dataset)
      .then(response => {
        this.datasets = this.datasets.filter(h => h !== dataset);    	  
      })
  }
  
  edit(dataset: Dataset, event: any) : void {
    this.router.navigate(['/dataset', dataset.id]);
  }
  
  ngOnInit(): void {
    this.getDatasets();
  }
}
