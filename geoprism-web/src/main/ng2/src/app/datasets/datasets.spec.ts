import { TestBed } from '@angular/core/testing';
import { AppModule } from '../app.module';
import { DatasetsComponent } from './datasets.component';

var acp: string = 'test/context';
var Globalize: any = {};
var com: any= {};

describe('DatasetsComponent', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [AppModule]
    });
    
    this.fixture = TestBed.createComponent(DatasetsComponent);    
  });

  it('test inital state', () => {
	let comp:DatasetsComponent = this.fixture.componentInstance;
    
    expect(comp.datasets).toBe(null);
    expect(comp.uploader).toBe(null);
    expect(comp.dropActive).toBe(false);
  });
});