import { Component} from '@angular/core';
import { Response } from '@angular/http';

import { BsModalRef } from 'ngx-bootstrap/modal/modal-options.class';
import * as FileSaver from 'file-saver';

import { UploadService } from '../service/upload.service';
import { DatasetResult } from './uploader-model';

@Component({
  selector: 'upload-result',
  templateUrl: './upload-result.component.html',
  styleUrls: []
})
export class UploadResultComponent{
  
  public summary:DatasetResult = {
    total:0,
    failures:0,
    file:undefined
  };
  
  constructor(private service:UploadService, public bsModalRef: BsModalRef) {}
  
  download():void{
    this.service.getErrorFile(this.summary.file).then((response: Response) => {
      let blob = response.blob();
        
      FileSaver.saveAs(blob, 'errors.xlsx');                
    });
  }
 
  submit():void {
    this.bsModalRef.hide();      
  }
}
