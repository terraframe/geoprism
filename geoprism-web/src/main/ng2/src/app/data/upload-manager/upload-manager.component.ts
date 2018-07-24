import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { ExcelImportHistory } from './upload-manager.model';

import { EventService } from '../../core/service/core.service';
import { LocalizationService } from '../../core/service/localization.service';

import { UploadManagerService } from './upload-manager.service';

import { Observable } from 'rxjs/Observable';

declare let acp: string;

@Component({
  selector: 'upload-manager',
  templateUrl: './upload-manager.component.html',
  styleUrls: ['./upload-manager.component.css']
})
export class UploadManagerComponent implements OnInit {
  public histories: ExcelImportHistory[] = [];

  constructor(
    private router: Router,
    private uploadManagerService: UploadManagerService,
    private localizationService: LocalizationService,
    private eventService: EventService) { }

  ngOnInit(): void {
    this.getAllHistory();
    
    this.uploadManagerService.pollAllHistory().subscribe(
      histories => {
        this.histories = histories;
      }
    );
  };
  
  getAllHistory() : void {
    this.uploadManagerService
      .getAllHistory()
      .then(histories => {
        this.histories = histories;
      })
  };
  
  clearHistory() : void {
    this.uploadManagerService
      .clearHistory()
      .then(response => {
        this.getAllHistory();
      })
  }
}
