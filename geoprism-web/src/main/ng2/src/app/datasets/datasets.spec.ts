import { async, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { AppModule } from '../app.module';

import { Dataset } from '../model/dataset';
import { DatasetsComponent } from './datasets.component';

import { LocalizationService } from '../service/localization.service';
import { MockLocalizationService } from '../service/localization.service.mock';

import { DatasetService } from '../service/dataset.service';
import { MockDatasetService } from '../service/dataset.service.mock';

describe('DatasetsComponent', () => {

  beforeEach(() => {
    
    TestBed.configureTestingModule({
      imports: [
        AppModule,                 
        RouterTestingModule.withRoutes([])
      ],
      providers: [
        {provide: DatasetService, useValue: new MockDatasetService()},
        {provide: LocalizationService, useValue: new MockLocalizationService()}
      ],      
    });
    
    this.fixture = TestBed.createComponent(DatasetsComponent);    
  });

  it('Test inital state', () => {
    let comp:DatasetsComponent = this.fixture.componentInstance;
    
    expect(comp.datasets).toBe(undefined);
    expect(comp.uploader).toBe(undefined);
    expect(comp.dropActive).toBe(false);
  });
  
  it('Test ng init', fakeAsync(() => {
    this.fixture.componentInstance.ngOnInit();    
    tick();    
    this.fixture.detectChanges();    

    let comp:DatasetsComponent = this.fixture.componentInstance;
    expect(comp.uploader).not.toBe(undefined);
    expect(comp.datasets.length).toBe(1);
  }));
  
  it('Test remove', fakeAsync(() => {
    this.fixture.componentInstance.ngOnInit();    
    tick();    
    this.fixture.detectChanges();    
    
    let dataset = this.fixture.componentInstance.datasets[0];

    this.fixture.componentInstance.remove(dataset);
    tick();    
    this.fixture.detectChanges();        
    
    expect(this.fixture.componentInstance.datasets.length).toBe(0);    
  }));
  
  it('Test edit', fakeAsync(() => {
    let navigateSpy = spyOn((<any>this.fixture.componentInstance).router, 'navigate');    
    
    this.fixture.componentInstance.ngOnInit();    
    tick();    
    this.fixture.detectChanges();    
    
    let dataset = this.fixture.componentInstance.datasets[0];
    
    this.fixture.componentInstance.edit(dataset);
    tick();    
    this.fixture.detectChanges();   
    
    expect(navigateSpy).toHaveBeenCalledWith(['/dataset', dataset.id]);    
  }));
  
  it('Test onSuccess', fakeAsync(() => {
    let navigateSpy = spyOn((<any>this.fixture.componentInstance).router, 'navigate');    
    
    this.fixture.componentInstance.ngOnInit();    
    tick();    
    this.fixture.detectChanges();    
    
    expect(this.fixture.componentInstance.datasets.length).toBe(1);        
    
    let dataset = this.fixture.componentInstance.datasets[0];
    
    let dataset2 = new Dataset();
    dataset2.id = 'test-id-2';
    
    this.fixture.componentInstance.onSuccess({datasets: [dataset, dataset2]});
    
    expect(this.fixture.componentInstance.datasets.length).toBe(2);      
  }));
});