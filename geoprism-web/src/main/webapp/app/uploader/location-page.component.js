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
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require("@angular/core");
var uploader_model_1 = require("./uploader-model");
var LocationField = (function () {
    function LocationField(field, universal) {
        this.field = field;
        this.universal = universal;
    }
    return LocationField;
}());
var LocationPageComponent = (function () {
    function LocationPageComponent() {
        this.onFieldChange = new core_1.EventEmitter();
        this.locationFields = {};
        this.attribute = this.createNewAttribute();
    }
    LocationPageComponent.prototype.ngOnInit = function () {
        // Initialize the universal options
        var countries = this.info.options.countries;
        for (var i = 0; i < countries.length; i++) {
            var country = countries[i];
            if (country.value == this.sheet.country) {
                this.universals = country.options;
            }
        }
        // Create a map of possible location fields
        for (var j = 0; j < this.sheet.fields.length; j++) {
            var field = this.sheet.fields[j];
            if (field.type == 'LOCATION') {
                if (this.locationFields[field.universal] == null) {
                    this.locationFields[field.universal] = [];
                }
                this.locationFields[field.universal].push(field);
            }
        }
        this.setLocationFieldAutoAssignment();
    };
    /**
     * Gets the lowest unassigned location fields in across the entire universal hierarchy.
     */
    LocationPageComponent.prototype.getLowestUnassignedLocationFields = function () {
        var unassignedLowestFields = new Array();
        for (var i = (this.universals.length - 1); i >= 0; i--) {
            var universal = this.universals[i];
            if (this.locationFields[universal.value] != null) {
                var fields = this.locationFields[universal.value];
                for (var j = 0; j < fields.length; j++) {
                    var field = fields[j];
                    if (!field.assigned) {
                        unassignedLowestFields.push(new LocationField(field, universal));
                    }
                }
                if (unassignedLowestFields.length > 0) {
                    return unassignedLowestFields;
                }
            }
        }
        return unassignedLowestFields;
    };
    /**
     * Gets all unassigned location fields at a given universal level
     *
     * @universalId - The universal level at which to search for unassigned fields
     */
    LocationPageComponent.prototype.getUnassignedLocationFields = function (universalId) {
        if (this.locationFields.hasOwnProperty(universalId)) {
            var fields = this.locationFields[universalId];
            var unassignedFields = [];
            for (var j = 0; j < fields.length; j++) {
                var field = fields[j];
                if (!field.assigned) {
                    unassignedFields.push(field);
                }
            }
            return unassignedFields;
        }
        return [];
    };
    /**
     * Gets the next location moving from low to high in the universal hierarchy.
     * Valid returns include another field at the same universal level or a higher universal level.
     *
     */
    LocationPageComponent.prototype.getNextLocationField = function () {
        for (var i = (this.universals.length - 1); i >= 0; i--) {
            var universal = this.universals[i];
            if (this.locationFields[universal.value] != null) {
                var fields = this.locationFields[universal.value];
                for (var j = 0; j < fields.length; j++) {
                    var field = fields[j];
                    if (!field.assigned) {
                        return new LocationField(field, universal);
                    }
                }
            }
        }
        return null;
    };
    LocationPageComponent.prototype.edit = function (attribute) {
        // This should only be hit if trying to edit when an existing edit session is in place. 
        if (this.attribute && this.sheet.attributes.values[this.attribute.id]) {
            if (this.sheet.attributes.values[this.attribute.id].editing) {
                this.sheet.attributes.values[this.attribute.id].editing = false;
            }
            // all fields that are in the current attribute (ui widget) should be set back to assigned
            // which creates a save type behavior for the current state of the attribute.
            this.setFieldAssigned();
        }
        if (this.attribute == null) {
            this.attribute = this.createNewAttribute();
        }
        Object.assign(this.attribute, attribute);
        this.sheet.attributes.values[this.attribute.id].editing = true;
        this.unassignLocationFields();
        var locFieldsSelectedButNotYetAssigned = this.getLocationFieldsSelectedInWidget();
        this.refreshUnassignedFields(locFieldsSelectedButNotYetAssigned);
        var fieldLabel = this.attribute.fields[attribute.universal];
        var field = this.getField(fieldLabel);
        // EXCLUDEed fields should be skipped
        if (field) {
            this.setUniversalOptions(field);
        }
    };
    /**
     * Sets all this.locationFields to false if the location field is part of this.attribute.fields.
     */
    LocationPageComponent.prototype.unassignLocationFields = function () {
        for (var key in this.attribute.fields) {
            if (this.attribute.fields.hasOwnProperty(key)) {
                var attributeFieldLabel = this.attribute.fields[key];
                var locFields = this.locationFields[key];
                for (var i = 0; i < locFields.length; i++) {
                    var locField = locFields[i];
                    if (locField.label === attributeFieldLabel) {
                        locField.assigned = false;
                    }
                }
            }
        }
    };
    LocationPageComponent.prototype.getLocationFieldsSelectedInWidget = function () {
        var unassignedFields = [];
        for (var key in this.attribute.fields) {
            if (this.attribute.fields.hasOwnProperty(key)) {
                var attributeFieldLabel = this.attribute.fields[key];
                var locFields = this.locationFields[key];
                for (var i = 0; i < locFields.length; i++) {
                    var locField = locFields[i];
                    // making sure to only add fields that are part of this.attribute
                    if (locField.label === attributeFieldLabel && !locField.assigned) {
                        unassignedFields.push(locField.label);
                    }
                }
            }
        }
        return unassignedFields;
    };
    /**
     * Gets a field from sheet.fields by label
     *
     * @label = label of a field
     */
    LocationPageComponent.prototype.getField = function (label) {
        for (var j = 0; j < this.sheet.fields.length; j++) {
            var field = this.sheet.fields[j];
            if (field.label === label) {
                return field;
            }
        }
        return null;
    };
    /**
     * Populate the unassigned field array from sheet.fields
     *
     * @selectedFields - fields that are to be excluded from the unassignedFields array. Typically this is because they are set in the
     *            location field attribute by the user in the UI and that location card has not yet been set. Keeping these fields out
     *            of the unassignedFields array ensures the fields aren't displayed as unassigned in the UI.
     */
    LocationPageComponent.prototype.refreshUnassignedFields = function (selectedFields) {
        this.unassignedFields = [];
        if (this.attribute != null) {
            for (var i = 0; i < this.sheet.fields.length; i++) {
                var field = this.sheet.fields[i];
                if (field.type == 'LOCATION' && !field.assigned && field.name != this.attribute.label && selectedFields.indexOf(field.name) === -1) {
                    this.unassignedFields.push(field);
                }
            }
        }
    };
    /**
     * Remove attribute from the sheet
     *
     * @attribute - attribute to remove
     */
    LocationPageComponent.prototype.remove = function (attribute) {
        if (this.sheet.attributes.values[attribute.id]) {
            delete this.sheet.attributes.values[attribute.id];
            this.sheet.attributes.ids.splice(this.sheet.attributes.ids.indexOf(attribute.id), 1);
            this.setFieldAssigned();
            if (!this.attribute) {
                this.newAttribute();
            }
            else {
                this.refreshUnassignedFields([]);
            }
        }
    };
    LocationPageComponent.prototype.createNewAttribute = function () {
        var attribute = new uploader_model_1.LocationAttribute();
        attribute.label = '';
        attribute.name = '';
        attribute.universal = '';
        attribute.id = '';
        attribute.fields = {};
        return attribute;
    };
    /**
     * Create a new attribute from a location field in the source data
     *
     * @attribute - location field that is being constructed.
     *         It corresponds to a configurable location card on the UI where the user can set the location.
     */
    LocationPageComponent.prototype.newAttribute = function () {
        if (this.attribute) {
            if (this.attribute.id === '') {
                this.attribute.id = this.generateId();
                this.sheet.attributes.ids.push(this.attribute.id);
                this.sheet.attributes.values[this.attribute.id] = new uploader_model_1.LocationAttribute();
            }
            var attributeInSheet = this.sheet.attributes.values[this.attribute.id];
            attributeInSheet.editing = false;
            // copy the properties from the attribute (cofigurable widget in the ui) to sheet.attributes
            // this.attribute is coppied to --> attributeInSheet
            Object.assign(attributeInSheet, this.attribute);
            // Set the assigned prop in sheet.fields if an attribute stored in sheet.attributes.values
            // also references the field.  This means that a saved attribute has claimed that field.
            this.setFieldAssigned();
        }
        var nextLocation = this.getNextLocationField();
        // This typically passes when a user manually sets an attribute and 
        // there are more location fields yet to be set
        if (nextLocation) {
            var field = nextLocation.field;
            var universal = nextLocation.universal;
            this.attribute = this.createNewAttribute();
            this.attribute.label = field.label;
            this.attribute.universal = universal.value;
            this.addField(field);
            this.setUniversalOptions(field);
            this.refreshUnassignedFields([]);
        }
        else {
            this.refreshUnassignedFields([]);
            this.attribute = null;
        }
    };
    //    
    //    //TODO: remove this if not needed
    ////    this.handleExcludedFields = function() {
    ////      let attributeFieldsToDelete = [];
    ////      for (let key in this.attribute.fields) {
    ////        if (this.attribute.fields.hasOwnProperty(key)) {
    ////          let attributeFieldLabel = this.attribute.fields[key];
    ////          if(attributeFieldLabel === "EXCLUDE"){
    ////            attributeFieldsToDelete.push(key);
    ////          }
    ////        }
    ////      }
    ////      
    ////      for(let i=0; i<attributeFieldsToDelete.length; i++){
    ////        let field = attributeFieldsToDelete[i];
    ////        
    ////        if(field){
    ////          delete this.attribute.fields[field];
    ////        }
    ////      }
    ////    }
    //    
    //    
    /**
     * Try to auto build the location field if there is only one field option per universal
     *
     * Rules:
     *  1. Attempt to auto-assign context fields if there is ONLY one lowest level universal
     *  2. Auto-assign a single context field only if there is a single option
     *
     */
    LocationPageComponent.prototype.setLocationFieldAutoAssignment = function () {
        var lowestLevelUnassignedLocationFields = this.getLowestUnassignedLocationFields();
        var lowestLevelUnassignedUniversal;
        if (lowestLevelUnassignedLocationFields.length > 0) {
            lowestLevelUnassignedUniversal = lowestLevelUnassignedLocationFields[0].universal.value;
        }
        if (lowestLevelUnassignedLocationFields.length === 1) {
            for (var j = 0; j < lowestLevelUnassignedLocationFields.length; j++) {
                var field = lowestLevelUnassignedLocationFields[j].field;
                // construct the initial model for a location field
                this.attribute = this.createNewAttribute();
                this.attribute.label = field.label;
                this.attribute.name = field.name;
                this.attribute.universal = field.universal;
                // add the targetLocationField.field (remember, it's from the source data) 
                // to the new location field (i.e. attribute)
                this.addField(field);
                // sets all valid universal options (excluding the current universal) for this location field
                this.setUniversalOptions(field);
                // There is only one or no universal options (i.e. context locations) so just set the field
                // to save a click for the user
                if (this.universalOptions.length < 1) {
                    // calling newAttribute() is safe because there are no other location fields so the 
                    // location attribute will just be set to null.
                    this.newAttribute();
                }
                else {
                    // Attempting auto-assignment of context fields
                    this.constructContextFieldsForAttribute(field);
                }
            }
        }
        else if (lowestLevelUnassignedLocationFields.length > 1) {
            for (var j = 0; j < lowestLevelUnassignedLocationFields.length; j++) {
                var field = lowestLevelUnassignedLocationFields[j].field;
                // construct the initial model for a location field
                this.attribute = this.createNewAttribute();
                this.attribute.label = field.label;
                this.attribute.name = field.name;
                this.attribute.universal = field.universal;
                // add the targetLocationField.field (remember, it's from the source data) 
                // to the new location field (i.e. attribute)
                this.addField(field);
                // sets all valid universal options (excluding the current universal) for this location field
                this.setUniversalOptions(field);
                this.refreshUnassignedFields([field.name]);
                break;
            }
        }
    };
    /**
     * Construct all possible context fields for a given target field.
     * NOTE: there is an assumption that this will only be called for a lowest level univeral field
     *
     * @field - the target field to which context fields would be built from. This is typically a field
     *       in the lowest level universal of a sheet.
     */
    LocationPageComponent.prototype.constructContextFieldsForAttribute = function (field) {
        var unassignedLocationFieldsForTargetFieldUniversal = this.getUnassignedLocationFields(field.universal);
        if (this.universalOptions.length > 0 && unassignedLocationFieldsForTargetFieldUniversal.length === 1) {
            var fieldsSetToUniversalOptions = new Array();
            for (var i = this.universalOptions.length; i--;) {
                var universalOption = this.universalOptions[i];
                var unassignedLocationFieldsForThisUniversal = this.getUnassignedLocationFields(universalOption.value);
                // Set the field ONLY if there is a single option per universal
                if (unassignedLocationFieldsForThisUniversal.length === 1) {
                    this.addField(unassignedLocationFieldsForThisUniversal[0]);
                    fieldsSetToUniversalOptions.push(unassignedLocationFieldsForThisUniversal[0].name);
                }
            }
            // set the location attribute only if all the universal options have been set automatically
            // i.e. The # of universal options must match the # of fields set
            if (fieldsSetToUniversalOptions.length === this.universalOptions.length) {
                this.newAttribute();
            }
            this.refreshUnassignedFields(fieldsSetToUniversalOptions);
        }
    };
    LocationPageComponent.prototype.addField = function (field) {
        this.attribute.fields[field.universal] = field.label;
    };
    /**
     * Sets the valid universal options for a given field.
     *
     * Valid options are:
     *   1.  universals that are assigned by the user in the source data
     *   2.  universals with fields that are not yet assigned
     */
    LocationPageComponent.prototype.setUniversalOptions = function (field) {
        this.universalOptions = [];
        var valid = true;
        for (var i = 0; i < this.universals.length; i++) {
            var universal = this.universals[i];
            var unassignedFieldsForThisUniversal = this.getUnassignedLocationFields(universal.value);
            if (universal.value == field.universal) {
                valid = false;
            }
            else if (valid && unassignedFieldsForThisUniversal.length > 0) {
                this.universalOptions.push(universal);
            }
        }
    };
    /**
     * Sets all sheet.fields in a sheet to isAssigned = true if the field is assigned to a sheet attribute
     * and is a location field.
     *
     */
    LocationPageComponent.prototype.setFieldAssigned = function () {
        for (var i = 0; i < this.sheet.fields.length; i++) {
            var field = this.sheet.fields[i];
            if (field.type == 'LOCATION') {
                field.assigned = this.isAssigned(field);
            }
            else {
                field.assigned = false;
            }
        }
    };
    /**
     * Check if a field is assigned to a sheet attribute
     *
     * @field - the field to check
     *
     * isAssigned = true if the sheet attribute fields has a field with a corresponding field label
     */
    LocationPageComponent.prototype.isAssigned = function (field) {
        for (var i = 0; i < this.sheet.attributes.ids.length; i++) {
            var id = this.sheet.attributes.ids[i];
            var attribute = this.sheet.attributes.values[id];
            for (var key in attribute.fields) {
                if (attribute.fields.hasOwnProperty(key)) {
                    if (attribute.fields[key] == field.label) {
                        return true;
                    }
                }
            }
        }
        return false;
    };
    LocationPageComponent.prototype.change = function (selectedFields) {
        var selectedFieldsArr = [];
        for (var key in selectedFields) {
            if (selectedFields.hasOwnProperty(key) && selectedFields[key] !== "EXCLUDE") {
                selectedFieldsArr.push(selectedFields[key]);
            }
        }
        this.refreshUnassignedFields(selectedFieldsArr);
    };
    LocationPageComponent.prototype.generateId = function () {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
            var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    };
    LocationPageComponent.prototype.localValidate = function (value, config) {
        if (config == 'label') {
            return this.validateLabel(value);
        }
        return null;
    };
    LocationPageComponent.prototype.validateLabel = function (label) {
        if (this.sheet != null) {
            var count = 0;
            for (var i = 0; i < this.sheet.fields.length; i++) {
                var field = this.sheet.fields[i];
                if (field.type != 'LOCATION' && field.label == label) {
                    count++;
                }
            }
            for (var i = 0; i < this.sheet.attributes.ids.length; i++) {
                var id = this.sheet.attributes.ids[i];
                var attribute = this.sheet.attributes.values[id];
                if (attribute.label === label && !attribute.editing) {
                    count++;
                }
                if (this.attribute && this.attribute.label === attribute.label && !attribute.editing) {
                    count++;
                }
            }
            if (count > 0) {
                return { unique: false };
            }
        }
        return null;
    };
    return LocationPageComponent;
}());
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.UploadInformation)
], LocationPageComponent.prototype, "info", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Sheet)
], LocationPageComponent.prototype, "sheet", void 0);
__decorate([
    core_1.Input(),
    __metadata("design:type", uploader_model_1.Page)
], LocationPageComponent.prototype, "page", void 0);
__decorate([
    core_1.Output(),
    __metadata("design:type", Object)
], LocationPageComponent.prototype, "onFieldChange", void 0);
LocationPageComponent = __decorate([
    core_1.Component({
        moduleId: module.id,
        selector: 'location-page',
        templateUrl: 'location-page.component.jsp',
        styleUrls: []
    }),
    __metadata("design:paramtypes", [])
], LocationPageComponent);
exports.LocationPageComponent = LocationPageComponent;
//# sourceMappingURL=location-page.component.js.map