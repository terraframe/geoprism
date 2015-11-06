/*
 * Copyright (c) 2015 TerraFrame, Inc. All rights reserved.
 *
 * This file is part of Runway SDK(tm).
 *
 * Runway SDK(tm) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Runway SDK(tm) is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Runway SDK(tm).  If not, see <http://www.gnu.org/licenses/>.
 */
(function(){
  var ClassFramework = Mojo.Meta;
  var Util = Mojo.Util; 
  var FormEntry = com.runwaysdk.geodashboard.FormEntry;
  var Form = com.runwaysdk.geodashboard.Form;

  /**
   * LANGUAGE
   */
  com.runwaysdk.Localize.defineLanguage('com.runwaysdk.geodashboard.gis.FeatureForm', {
    "submit" : "Submit",
    "cancel" : "Cancel",
    "integerError" : "Value is not a valid whole number",
    "numberError" : "Value is not a valid number",
    "dateError" : "Value is not a date",
    "formErrors" : "Form errors must be corrected before the form can be submitted"
  });
      
  var FeatureForm = Mojo.Meta.newClass('com.runwaysdk.geodashboard.gis.FeatureForm', {
    Extends : com.runwaysdk.ui.Component,
    Instance : {
        
      /**
       * 
       */
      initialize : function(controller, config){
        this._controller = controller;
        this._id = config.id;
        this._type = config.type;
        this._object = null;
        
        // Number localization setup
        this._parser = Globalize.numberParser();
        this._formatter = Globalize.numberFormatter();    
      },
      _validateInteger : function (value) {
        var number = this._parser( value );
            
        return (value != '' && (!$.isNumeric(number) || Math.floor(number) != number));
      },        
      _validateNumeric : function (value) {
        var number = this._parser( value );
          
        return (value != '' && !$.isNumeric(number));        
      },      
      _validateDate : function (value) {
        var date = this._parseDate( value );
        
        return (value != '' && (date == null));        
      },      
      /*
       * Format dates
       * 
       */
      _formatDate : function(value) {
          
        if(this._dateFormatter == null) {
          this._dateFormatter = Globalize.dateFormatter({ date: "short" });                         
        }
          
        if(value != null) {
          return this._dateFormatter(value);          
        }
          
        return null;
      },      
      _parseDate : function(value) {
        
        if(this._dateParser == null) {
          this._dateParser = Globalize.dateParser({ date: "short" });                         
        }
        
        if(value != null) {
          return this._dateParser(value);          
        }
        
        return null;
      },      
      _loadClass : function(callback) {
          
        if (!ClassFramework.classExists(this._type)) {
          var thisDS = this;
          var clientRequest = new Mojo.ClientRequest({
            onSuccess : function(){
              callback.onSuccess();
            },
            onFailure : function(e){
              callback.onFailure(e);
            }
          });
              
          com.runwaysdk.Facade.importTypes(clientRequest, [this._type], {autoEval : true});
        }
        else {
          callback.onSuccess();
        }
      },
      isValid : function(attributeMd) {
        if(!attributeMd.isSystem()) {
          var attributeName = attributeMd.getName();
          
          if(attributeName == 'keyName'){
            return false;
          }
          
          return true;
        }
        
        return false;
      },
      _build : function(object) {
        this._object = object;
        
        this._form = new Form();
        
        var attributeNames = this._object.getAttributeNames();
        
        for(var i = 0; i < attributeNames.length; i++) {
          var attributeName = attributeNames[i];
          var attributeDTO = this._object.getAttributeDTO(attributeName);
          var attributeMd = attributeDTO.getAttributeMdDTO();
          
          if(attributeDTO.isReadable() && attributeDTO.isWritable() && this.isValid(attributeMd)) {
            
            if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeCharacterDTO) {                
              var input = FormEntry.newInput('text', attributeName, {attributes:{type:'text'}});
              input.setValue(attributeDTO.getValue());
                
              this._form.addFormEntry(attributeMd, input);
            }
            else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeNumberDTO) {                
              var value = attributeDTO.getValue();
              
              if(value != null) {
                value = this._formatter(value);                
              }
                
              var input = FormEntry.newInput('text', attributeName, {attributes:{type:'text'}});
              input.setValue(value);
              
              var entry = this._form.addFormEntry(attributeMd, input);
              
              if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeIntegerDTO) {
                entry.addValidator(Mojo.Util.bind(this, this._validateInteger), this.localize("integerError"));
              }
              else {                
                entry.addValidator(Mojo.Util.bind(this, this._validateNumeric), this.localize("numberError"));
              }                            
            }
            else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeReferenceDTO) {                
              if(attributeMd.getReferencedMdBusiness() == 'com.runwaysdk.geodashboard.ontology.Classifier' ) {                
                var entry = new com.runwaysdk.geodashboard.ClassifierEntry(attributeMd, attributeDTO.getValue());
              
                this._form.addEntry(entry);                
              }
              else if(attributeMd.getReferencedMdBusiness() == 'com.runwaysdk.system.gis.geo.GeoEntity' ) {                
                var entry = new com.runwaysdk.geodashboard.GeoEntityEntry(attributeMd, attributeDTO.getValue(), this._controller.getDashboardId());
                
                this._form.addEntry(entry);                
              }
            }
            else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeDateDTO) {                
              // Normalize the incoming date to a standard format regardless of the locale                              
              var date = attributeDTO.getValue();
              var value = this._formatDate(date);
              
              var entry = new com.runwaysdk.geodashboard.DateFormEntry(attributeMd, attributeName, value);
              entry.addValidator(Mojo.Util.bind(this, this._validateDate), this.localize("dateError"));
              
              this._form.addEntry(entry);
            }
          }
        }
        
        // Create the dialog
        var label = this._object.getTypeMd().getDisplayLabel();
        
        this._dialog = this.getFactory().newDialog(label, {minHeight:250, maxHeight:$(window).height() - 75, minWidth:730, resizable: false});   
        this._dialog.setId(this._id);
        this._dialog.appendContent(this._form);        
        this._dialog.addButton(this.localize("submit"), Mojo.Util.bind(this, this._onSubmit), null, {id:'user-submit', class:'btn btn-primary'});
        this._dialog.addButton(this.localize("cancel"), Mojo.Util.bind(this, this._onCancel), null, {id:'user-cancel', class:'btn'});            
        this._dialog.render();   
        
        this._form.render();        
      },
      handleException : function(ex, throwIt) {
        this._form.handleException(ex, throwIt); 
      },
      handleErrorMessage : function(message) {
        var dialog = this.getFactory().newDialog(com.runwaysdk.Localize.get("rError", "Error"), {modal: true});
        dialog.appendContent(message);
        dialog.addButton(com.runwaysdk.Localize.get("rOk", "Ok"), function(){dialog.close();});
        dialog.render();        
      },
      _onSubmit : function() {
        
        if(this._form.hasError()) {
          this.handleErrorMessage(this.localize("formErrors"));  
        }
        else {
          var that = this;
          var values = this._form.getValues();        
          var attributeNames = values.keySet();
        
          $.each(attributeNames, function( index, attributeName ) {          
            var attributeDTO = that._object.getAttributeDTO(attributeName);
          
            if(attributeDTO != null) {
              var attributeMd = attributeDTO.getAttributeMdDTO();
              var value = values.get(attributeName);
            
              if(attributeDTO.isWritable() && that.isValid(attributeMd)) {
              
                if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeDateDTO) {
                  var date = that._parseDate(value);                                
                 
                  attributeDTO.setValue(date);
                }
                else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeNumberDTO) {
                  if(value != null) {
                    value = that._parser(value);
                  }
                
                  attributeDTO.setValue(value);
                }
                else {                
                  attributeDTO.setValue(value);            
                }
              }
            } 
          });
        
          var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
            onSuccess : function(object){
              that._dialog.close();
              that._controller.refresh();
            },
            onFailure : function(e){
              that.handleException(e);
            }
          }, '#' + this._dialog.getId());
        
          this._object.apply(request);
        }
      },
      _onCancel : function() {
        var that = this;
          
        var request = new com.runwaysdk.geodashboard.StandbyClientRequest({
          onSuccess : function(object){
            that._dialog.close();
          },
          onFailure : function(e){
            that.handleException(e);
          }
        }, '#' + this._dialog.getId());
         
        // Get the object and lock it
        com.runwaysdk.Facade.unlock(request, this._id);
      },
      
      onSuccess : function() {
        var that = this;
     
        var request = new Mojo.ClientRequest({
          onSuccess : function(object){
            that._build(object);
          },
          onFailure : function(e){
            that.handleException(e);
          }
        });
       
        // Get the object and lock it
        com.runwaysdk.Facade.lock(request, this._id);       
      },
      onFailure : function(e){
        this.handleException(e);
      },      
      render : function() {
        this._loadClass(this);  
      }
    }
  });      
})();