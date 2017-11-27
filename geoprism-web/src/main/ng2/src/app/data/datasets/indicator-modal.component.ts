import { Component, EventEmitter, Output, Input } from '@angular/core';

import { Dataset, DatasetAttribute, IndicatorField} from '../model/dataset';

import { DatasetService } from '../service/dataset.service';

@Component({  
  selector: 'indicator-modal',
  templateUrl: './indicator-modal.component.html',
  styleUrls: []
})
export class IndicatorModalComponent {

  @Output() onSuccess = new EventEmitter<DatasetAttribute>();
  
  datasetId:string;
  indicator: IndicatorField;
  
  show: boolean;
  aggregations: {id:string; value:string}[];
  attributes: DatasetAttribute[];
  
  constructor(private datasetService: DatasetService) {}
  
  initialize(datasetId:string, aggregations:{id:string; value:string}[], attributes:DatasetAttribute[], indicator:IndicatorField): void {
	
    if(indicator === undefined) {
      this.indicator = {
        label: '',
        left: {
          aggregation: '',
          attribute: ''       
        },
        right: {
          aggregation: '',
          attribute: ''         
        },
        id: undefined,
        percentage : false
      };     
    }
    else {
      this.indicator = indicator;
      
      if(this.indicator.percentage === undefined) {
        this.indicator.percentage = false;
      }
    }
    
    this.datasetId = datasetId;
    this.aggregations = aggregations;;
    this.attributes = attributes;
    this.show = true;	  
  }
  
  clear(): void{
    this.aggregations = null;
    this.attributes = null;
    this.show = false;
  }
  
  cancel(): void{
    if(this.indicator.id === undefined) {
      this.clear();	
    }
    else {
      this.datasetService.unlockAttribute(this.indicator.id)
        .then(response => {
          this.clear();
        });
    }
  }
  onSubmit(): void{
//	  event.stopPropagation(); 
	  
    this.datasetService.addIndicator(this.datasetId, this.indicator)
      .then(attribute => {
        this.onSuccess.emit(attribute);
        this.clear();
      });
//	  
//	
//    this.onSuccess.emit(this.indicator);
//    this.cancel();
  }
}
