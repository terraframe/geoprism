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
"use strict";
var Universal = (function () {
    function Universal() {
    }
    return Universal;
}());
exports.Universal = Universal;
var Country = (function () {
    function Country() {
    }
    return Country;
}());
exports.Country = Country;
var Options = (function () {
    function Options() {
    }
    return Options;
}());
exports.Options = Options;
var Classifier = (function () {
    function Classifier() {
    }
    return Classifier;
}());
exports.Classifier = Classifier;
var Field = (function () {
    function Field() {
    }
    return Field;
}());
exports.Field = Field;
var LocationAttribute = (function () {
    function LocationAttribute() {
    }
    return LocationAttribute;
}());
exports.LocationAttribute = LocationAttribute;
var Locations = (function () {
    function Locations() {
    }
    return Locations;
}());
exports.Locations = Locations;
var CoordinateAttribute = (function () {
    function CoordinateAttribute() {
    }
    return CoordinateAttribute;
}());
exports.CoordinateAttribute = CoordinateAttribute;
var Coordinates = (function () {
    function Coordinates() {
    }
    return Coordinates;
}());
exports.Coordinates = Coordinates;
var Sheet = (function () {
    function Sheet() {
    }
    return Sheet;
}());
exports.Sheet = Sheet;
var LocationExclusion = (function () {
    function LocationExclusion(id, universal, locationLabel, parentId) {
        this.id = id;
        this.universal = universal;
        this.locationLabel = locationLabel;
        this.parentId = parentId;
    }
    return LocationExclusion;
}());
exports.LocationExclusion = LocationExclusion;
var Workbook = (function () {
    function Workbook() {
    }
    return Workbook;
}());
exports.Workbook = Workbook;
var UploadInformation = (function () {
    function UploadInformation() {
    }
    return UploadInformation;
}());
exports.UploadInformation = UploadInformation;
var Step = (function () {
    function Step(label, page) {
        this.label = label;
        this.page = page;
    }
    return Step;
}());
exports.Step = Step;
var Snapshot = (function () {
    function Snapshot(page, sheet) {
        this.page = page;
        this.sheet = sheet;
    }
    return Snapshot;
}());
exports.Snapshot = Snapshot;
var Page = (function () {
    function Page(name, prev) {
        this.name = name;
        this.prev = prev;
        this.layout = 'holder';
        this.hasNext = true;
        this.isReady = false;
        this.confirm = false;
    }
    return Page;
}());
exports.Page = Page;
var LocationContext = (function () {
    function LocationContext() {
    }
    return LocationContext;
}());
exports.LocationContext = LocationContext;
var LocationProblem = (function () {
    function LocationProblem() {
    }
    return LocationProblem;
}());
exports.LocationProblem = LocationProblem;
var CategoryProblem = (function () {
    function CategoryProblem() {
    }
    return CategoryProblem;
}());
exports.CategoryProblem = CategoryProblem;
var Problems = (function () {
    function Problems() {
    }
    return Problems;
}());
exports.Problems = Problems;
var DatasetResponse = (function () {
    function DatasetResponse() {
    }
    return DatasetResponse;
}());
exports.DatasetResponse = DatasetResponse;
var Label = (function () {
    function Label() {
    }
    return Label;
}());
exports.Label = Label;
var GeoSynonym = (function () {
    function GeoSynonym() {
    }
    return GeoSynonym;
}());
exports.GeoSynonym = GeoSynonym;
var ClassifierSynonym = (function () {
    function ClassifierSynonym() {
    }
    return ClassifierSynonym;
}());
exports.ClassifierSynonym = ClassifierSynonym;
var Entity = (function () {
    function Entity() {
    }
    return Entity;
}());
exports.Entity = Entity;
//# sourceMappingURL=uploader-model.js.map