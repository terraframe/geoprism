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


import { Directive, forwardRef, Attribute, Input } from '@angular/core';
import { Validator, AbstractControl, NG_VALIDATORS } from '@angular/forms';

export interface LocalValidator {
  localValidate(value:string, config: string): {[key : string] : any};
}

@Directive({
  selector: '[funcValidator][ngModel]',
  providers: [
    { provide: NG_VALIDATORS, useExisting: forwardRef(() => FunctionValidator), multi: true }
  ]
})
export class FunctionValidator implements Validator {
  
  @Input() validator: LocalValidator;
  @Input() config: string;
  
  constructor(){}

  validate(c: AbstractControl): {[key : string] : any} {
    if(c.value != null && c.value.length > 0) {
      return this.validator.localValidate(c.value, this.config);    
    }
    
    return null;
  }
}