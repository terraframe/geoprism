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

export class Universal {
  value: string;
  label: string; 
}

export class Country {
  options : Universal[];
  value: string;
  label: string;
}

export class Options {
  countries : Country[];  
}

export class Classifier {
  value: string;
  label: string;  
}

export class Field {
  columnType: string;
  fieldPosition: number;
  name: string;
  aggregatable: boolean;
  accepted: boolean;
  label: string;
  type: string;
  categoryLabel: string;
  root: string;
}

export class Locations {
  ids: string[];
  values: any;
}

export class Coordinates {
  ids: string[];
  values: any;
}

export class Sheet {
  value: string;
  label: string;
  name: string;
  country: string;
  replaceExisting: boolean;

  fields : Field[];
  attributes: Locations;
  coordinates: Coordinates;
  categories: any;
  matches: any[];
}

export class Workbook {
  filename: string;
  directory: string;
  sheets: Sheet[];
}

export class UploadInformation {
  options: Options;
  classifiers: Classifier[];
  information: Workbook;
}

export class Step {
  constructor(public label: string, public page: string) {}
}

export class Snapshot {

  constructor(public page: string, public sheet: Sheet) {}
}

export class Page {
  snapshot: Sheet;

  hasNext: boolean;
  isReady: boolean;
  layout: string;

  constructor(public name: string, public prev: Page) {
    this.layout = 'holder';
    this.hasNext = true;
    this.isReady = false;
  }
}