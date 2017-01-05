import { Component, Input } from '@angular/core';


@Component({
  moduleId: module.id,
  selector: 'error-message',
  templateUrl: 'error-message.component.jsp',
  styleUrls: []
})
export class ErrorMessageComponent {

  @Input('error') error: any;
	
  constructor() { }
}
