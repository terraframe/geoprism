<%@ taglib uri="/WEB-INF/tlds/geoprism.tld" prefix="gdb"%>

<form #form="ngForm" name="form" class="modal-form">
  <div>
    <fieldset>
      <section class="form-container">




<div>
  <div class="label-holder"></div>
  <div class="holder">
    <div class="row-holder">
      <p><localize key="dataUploader.textLocationConfiguration.heading.paragraph"></localize></p>
    </div>
  </div>

  <div *ngIf="unassignedFields.length > 0">
    <div class="label-holder">
      <strong><localize key="dataUploader.unassignedLocationFields"></localize></strong>
    </div>
    <div class="holder">
      <div class="row-holder">
        <div *ngFor="let field of unassignedFields" class="location-selector-container">
          <h3 class="location-field-info-card-title">{{field.label}}</h3>
        </div>
      </div> <!-- end row-holder -->
    </div> <!-- end holder -->
  </div> 
    
  <form *ngIf="attribute != null" #attributeForm="ngForm">
    <div class="label-holder">
      <strong><localize key="dataUploader.locationCreatorWidgetLabel"></localize></strong>
    </div>    
    <div class="holder">
      <div class="location-selector-container">
        <div class="row-holder">
          <h4 class="location-select-container-heading-text"><localize key="dataUploader.locationContainerHeadingToolTip"></localize> {{attribute.label}}</h4>
        </div>
          <span class="text">
          <input [(ngModel)]="attribute.label" #label="ngModel" name="label" required type="text" funcValidator [validator]="this" config="label" />
        </span>
        <div class="error-message">
          <p *ngIf="label.invalid">
            <localize key="dataUploader.unique"></localize>
          </p>    
        </div>
        <div class="row-holder">
          <hr>
        </div>        
        <div class="row-holder" *ngFor="let universal of universalOptions; let i = index;">
          <div class="label-help-ico-container">
            <i class="fa fa-question-circle help-info-ico"></i>      
            <p class="select-label">{{universal.label}} <localize key="dataUploader.selectLabelToolTip"></localize></p>
          </div>
          <div class="location-selector-box-right">
            <div class="box">
              <select class="select-area" [(ngModel)]="attribute.fields[universal.value]" (change)="change(attribute.fields)" [name]="i + '-universal'" required>
                <option value=""></option>          
                <option *ngFor="let field of locationFields[universal.value]" [value]="field.label">{{field.label}}</option>   
                <option value="EXCLUDE"><localize key="dataUploader.exclude"></localize></option>
              </select>
            </div>
          </div>
        </div>
          <div class="row-holder">
          <div class="button-holder">
              <input type="button" value="+" class="btn btn-primary set-location-btn pull-right" (click)="newAttribute()" [disabled]="attributeForm.invalid" [title]="'dataUploader.createBtnToolTip' | localize" />
          </div>
          </div>  
      </div>  
    </div>
  </form>  
  
  <div *ngIf="sheet.attributes.ids.length > 0">
    <div class="label-holder">
      <strong><localize key="dataUploader.attributes"></localize></strong>
    </div>
    <div class="holder">
      <div class="row-holder">
        
        <div *ngFor="let key of sheet.attributes.values | keys">
          <div *ngIf="!key.value.editing" class="location-selector-container scale-fade">
            <h3 class="location-field-info-card-title">{{key.value.label}}</h3>
            <div class="cell" style="float: right;">            
              <i class="fa fa-pencil ico-edit" (click)="edit(key.value)" [title]="'dataUploader.editToolTip' | localize"></i>
              <i class="fa fa-trash-o ico-remove" (click)="remove(key.value)" [title]="'dataUploader.deleteToolTip' | localize"></i>
            </div>
            <div class="row-holder"></div>
            <ul class="location-field-list-display">
              <div *ngFor="let universal of universals">
                <li *ngIf="key.value.fields[universal.value] != null && key.value.fields[universal.value] != 'EXCLUDE'">
                  <i class="fa fa-check-square"></i>{{key.value.fields[universal.value]}}            
                </li>
              </div>
            </ul>   
          </div>
        </div>
        
      </div> <!-- end row-holder -->
    </div> <!-- end holder -->
  </div> <!-- end *ngIf -->
  
  <div class="label-holder"></div>
  <div class="holder">
    <div class="error-message">
      <p *ngIf="(unassignedFields.length > 0 || attribute != null)"><localize key="dataUploader.unassignedLocationFields"></localize></p>
    </div>          
  </div>  
</div>

      </section>        
    </fieldset>   
    
    <paging [form]="form" [page]="page" [global]="(unassignedFields.length == 0 && attribute == null)"></paging>
  </div>
</form>
