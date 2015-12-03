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

  function RunwayService() {
    var service = {};
    
    service.createRequest = function(onSuccess, onFailure){
      var request = new Mojo.ClientRequest({
        onSuccess : onSuccess,
        onFailure : function(e) {
          GDB.ExceptionHandler.handleException(e);
                      
          if(onFailure != null) {
            onFailure(e);
          }
        }
      });
        
      return request;
    }
      
    service.createStandbyRequest = function(elementId, onSuccess, onFailure){
      var el = $(elementId);
        
      if(el.length > 0) {      
        var request = new GDB.StandbyClientRequest({
          onSuccess : onSuccess,
          onFailure : function(e){
            GDB.ExceptionHandler.handleException(e);
              
            if(onFailure != null) {
              onFailure(e);
            }
          }
        }, elementId);
          
        return request;        
      }
        
      return service.createRequest(onSuccess, onFailure);
    }
    
    service.isValid = function(attributeMd) {
      if(!attributeMd.isSystem()) {
        var attributeName = attributeMd.getName();
          
        if(attributeName == 'keyName'){
          return false;
        }
          
        return true;
      }
        
      return false;
    }    
    
    service.getFields = function(dto, attributeNames) {
      var fields = [];
      
      if(attributeNames == null) {
        attributeNames = dto.getAttributeNames();        
      }
        
      for(var i = 0; i < attributeNames.length; i++) {
        var attributeName = attributeNames[i];
          
        var attributeDTO = dto.getAttributeDTO(attributeName);
        var attributeMd = attributeDTO.getAttributeMdDTO();
          
        if(attributeDTO.isReadable() && attributeDTO.isWritable() && service.isValid(attributeMd)) {
          var field = {};
              
          if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeCharacterDTO || attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO) {
            field.name = attributeName;
            field.type = 'text';
            field.message = '';
            field.label = attributeMd.getDisplayLabel();
          }
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeReferenceDTO) {
            field.name = attributeName;
            field.type = 'select';
            field.message = '';
            field.label = attributeMd.getDisplayLabel();
            field.options = [];
          }
            
          if(!$.isEmptyObject(field)) {          
          field.required = attributeMd.isRequired();
          field.readable = attributeDTO.isReadable();
          field.writable = attributeDTO.isWritable();
          
            fields.push(field);
          }
        }        
      }      
      
      return fields;
    }
    
    service.convertToObject = function(dto) {
      var object = {};
        
      var attributeNames = dto.getAttributeNames();
          
      for(var i = 0; i < attributeNames.length; i++) {
        var attributeName = attributeNames[i];
            
        var attributeDTO = dto.getAttributeDTO(attributeName);
        var attributeMd = attributeDTO.getAttributeMdDTO();
            
        if(attributeDTO.isReadable() && attributeDTO.isWritable() && service.isValid(attributeMd)) {          
          if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeCharacterDTO) {
            object[attributeName] = attributeDTO.getValue();
          }
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO) {
            object[attributeName] = attributeDTO.getLocalizedValue();
          }
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeReferenceDTO) {
            object[attributeName] = attributeDTO.getValue();
          }
        }        
      }      
       
      return object;    
    }
    
    service.populate = function(dto, object) {
      for(var key in object) {
        var attributeDTO = dto.getAttributeDTO(key);
        
        if(attributeDTO != null) {
          var value = object[key];
          
          if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO) {
            var struct = attributeDTO.getStructDTO();
           
            struct.localizedValue = value;
          }
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeEnumerationDTO) {
              attributeDTO.add(value);            
          }
          else {
            attributeDTO.setValue(value);
          }
        }
        else{
        	// Model properties that do not map to server object properties
        	console.log(key);
        }
      }
    }
        
    return service;  
  }
  
  angular.module("runway-service", []);
  angular.module("runway-service")
    .factory('runwayService', RunwayService)
})();