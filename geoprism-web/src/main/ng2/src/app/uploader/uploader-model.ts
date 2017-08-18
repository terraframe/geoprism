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

import { Dataset } from '../model/dataset';
import { Pair } from '../model/pair';

export class Universal {
  value: string;
  label: string; 
  useCoordinatesForLocationAssignment: boolean;
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

  // Properties for category fields 
  categoryLabel: string;
  root: string;

  // Properties for location fields
  universal: string;
  assigned: boolean;
}

export class LocationAttribute {
  label: string;
  name: string;
  universal: string;
  fields: { [key:string]:string};
  id: string;
  editing: boolean;
  useCoordinatesForLocationAssignment: boolean; 
  coordinatesForLocationAssignmentOptions: Field[];
  latForLocationAssignment = '';
  longForLocationAssignment = '';
}

export class Locations {
  ids: string[];
  values: { [key:string]:LocationAttribute}; 
}

export class CoordinateAttribute {
  label : string;
  latitude : string;
  longitude : string;
  featureLabel : string;
  location : string;
  featureId : string;
  id : string;  
  universal: string;
}

export class Coordinates {
  ids: string[];
  values: { [key:string]:CoordinateAttribute};
}

export class Sheet {
  value: string;
  label: string;
  name: string;
  description: string;
  source: string;
  country: string;
  replaceExisting: boolean;
  exists: boolean;

  fields : Field[];
  attributes: Locations;
  coordinates: CoordinateAttribute[];
  categories: any;
  matches: any[];
}

export class LocationExclusion {
  constructor(public id: string, public universal: string, public locationLabel: string, public parentId: string) {}
}

export class Workbook {
  filename: string;
  directory: string;
  sheets: Sheet[];
  locationExclusions: LocationExclusion[];
  categoryExclusion: { [key:string]:string[]};
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
  confirm: boolean;
  layout: string;

  constructor(public name: string, public prev: Page) {
    this.layout = 'holder';
    this.hasNext = true;
    this.isReady = false;
    this.confirm = false;
  }
}

export class LocationContext {
  label: string;
  universal: string;
}

export class LocationProblem {
  type: string;
  label: string;
  parentId: string;
  universalId: string;
  universalLabel: string;
  context: LocationContext[];
  resolved: boolean;
  synonym: string;
  action: any;
}

export class CategoryProblem {
  label: string;
  mdAttributeId: string;
  categoryId: string;
  optionId: string;
  resolved: boolean;
  synonym: string;
  action: any;
}

export class Problems {
  locations  : LocationProblem[];
  categories  : CategoryProblem[];
  options: { [key:string]:Pair[]};
}

export class DatasetResponse {
  success: boolean;
  datasets: Dataset[];
  sheets: Sheet[];
  problems: Problems;
}

export class Label {
  label: string;
}

export class GeoSynonym {
  synonymId: string;
  label: string;
  ancestors: Label[];
}

export class ClassifierSynonym {
  synonymId: string;
  label: string;
}

export class Entity {
  entityId: string;
}


