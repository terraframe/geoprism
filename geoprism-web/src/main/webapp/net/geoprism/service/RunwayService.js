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

  function RunwayService($http) {
    var service = {};
    
    service.createRequest = function(onSuccess, onFailure){
      var request = new Mojo.ClientRequest({
        onSuccess : onSuccess,
        onFailure : function(e) {
                      
          if(onFailure != null) {
            onFailure(e);
          }
          else {
            GDB.ExceptionHandler.handleException(e);
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
              
            if(onFailure != null) {
              onFailure(e);
            }
            else {
              GDB.ExceptionHandler.handleException(e);
            }
          }
        }, elementId);
          
        return request;        
      }
        
      return service.createRequest(onSuccess, onFailure);
    }
    
    service.createConnectionRequest = function(connection) {
      if(connection.elementId != null) {
        return service.createStandbyRequest(connection.elementId, connection.onSuccess, connection.onFailure );
      }	
      else {
        return service.createRequest(connection.onSuccess, connection.onFailure );
      }
    }    
    
    service.execute = function(req, connection) {
      var request = service.createConnectionRequest(connection);      
      
      var success = function(response) {
        request.onSuccess(response.data);
      }
      
      var failure = function(response) {
        if(response.status === 401) {
          window.location = window.com.runwaysdk.__applicationContextPath + '/session/form';
        }
        else {
          request.onFailure(response.data);                
        }
      }
      
      if(connection.elementId != null) {
        
        $http(req).then(success, failure).finally(function(){
          request._hideStandby();
        });
        
        request._showStandby();
      }  
      else {
        $http(req).then(success, failure);
      }
    }
    
    service.generateId = function() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
      });
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
           
            if(value != null) {
              struct.localizedValue = value;              
            }
            else {
              struct.localizedValue = '';
            }
          }
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeEnumerationDTO) {
          attributeDTO.clear();
          
            attributeDTO.add(value);            
          }
          else {
            attributeDTO.setValue(value);
          }
        }
      }
    }
    
    service.populateObject = function(object, dto) {
      for(var key in object) {
        var attributeDTO = dto.getAttributeDTO(key);

        if(attributeDTO != null) {
           
          var attributeMd = attributeDTO.getAttributeMdDTO();
              
          if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeCharacterDTO) {
            var value = attributeDTO.getValue();
              
            if(value != null && value.length > 0) {
              object[key] = value;
            }
          }
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeReferenceDTO) {
            var value = attributeDTO.getValue();
              
            if(value != null && value.length > 0) {
              object[key] = value;
            }                        
          }          
          else if(attributeDTO instanceof com.runwaysdk.transport.attributes.AttributeLocalCharacterDTO) {
            var value = attributeDTO.getLocalizedValue();
            
            if(value != null && value.length > 0) {
              object[key] = value;
            }            
          }
          else {
            var value = attributeDTO.getValue();
              
            if(value != null) {
              object[key] = value;
            }
          }
        }        
      }      
    }
    
    service.loadClass = function(type, onSuccess, onFailure) {
      if (!Mojo.Meta.classExists(type)) {
        var request = service.createRequest(onSuccess, onFailure);
              
        com.runwaysdk.Facade.importTypes(request, [type], {autoEval : true});
      }
      else {
        onSuccess();
      }
    }
            
    service.loadDependencies = function(dependencies, onSuccess, onFailure) {
      var types = [];
      
      for(var i = 0; i < dependencies.length; i++) {
        if(!Mojo.Meta.classExists(dependencies[i])) {
          types.push(dependencies[i]);
        }
      }
      
      if (types.length > 0) {
        var request = service.createRequest(onSuccess, onFailure);
        
        com.runwaysdk.Facade.importTypes(request, types, {autoEval : true});
      }
      else {
        onSuccess();
      }
    }
    
    service.decorateFunction = function(f, dependencies) {
      return function() {
        var args = arguments;
      
        service.loadDependencies(dependencies, function() {
          f.apply(this, args);  
        });
      };
    }  
    
    service.decorateService = function(object, dependencies) {
      var decorator = {};
      
      for (var property in object) {
        if (object.hasOwnProperty(property)) {
          var f = object[property];
          
          if(typeof f === 'function') {
            decorator[property] = service.decorateFunction(f, dependencies);
          }
        }
      }
      
      return decorator;    	
    }  
    
    return service;
  }
  
  angular.module("runway-service", []);
  angular.module("runway-service")
    .factory('runwayService', RunwayService)
})();