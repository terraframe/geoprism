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

import { BasicCategory } from '../model/category';

export class DatasetAttribute { 
  oid: string;
  label: string;
  type: string
  numeric: boolean;  
  selected: boolean;
  root: BasicCategory;
}

export class Dataset {
  oid: string;
  label: string;
  description: string;
  type: string;
  value: string;
  source: string;
  attributes : DatasetAttribute[];
  aggregations: {oid: string;value: string;}[];
  indicators: IndicatorField[];
}

export class DatasetCollection {
  canExport: boolean;
  datasets: Dataset[];
}

export class Indicator {
  aggregation: string;
  attribute: string; 
}

export class IndicatorField {
  label: string; 
  left: Indicator;
  right: Indicator;
  oid: string;
  percentage: boolean;
}