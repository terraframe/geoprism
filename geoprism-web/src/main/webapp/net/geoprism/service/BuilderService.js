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

  function BuilderService(runwayService) {
    var service = {};
    service.dto = new net.geoprism.dashboard.Dashboard();
    
    service.applyWithOptions = function(object, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);

      runwayService.populate(service.dto, object);
     
      service.dto.applyWithOptions(request, object.options);
    }
    
    service.getLayersToDelete = function(object, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
       
      service.dto.getLayersToDelete(request, object.options);    
    }
    
    service.unlock = function(object, onSuccess, onFailure) {
      if(service.dto == null || service.dto.isNewInstance()) {
        onSuccess(true);  
      }
      else {
        var request = runwayService.createRequest(function(){onSuccess(false)}, onFailure);
        
        service.dto.unlock(request);  
      }
    }    
    
    service.loadDashboard = function(dashboardId, element, onSuccess, onFailure) {
      
      /*
       * Second: Get all options
       */
      var request = runwayService.createStandbyRequest(element, function(json, dto) {    	  
        service.dto = dto;
    	  
        var object = JSON.parse(json);
        
        var result = {};
        // Populate the list of country options  
        // ORDER MATTERS for this array of field names. Fields will be added to the form in order.
        var attributeNames = ['displayLabel', 'description', 'name'];
            
        if(dto.isNewInstance()) {
          attributeNames = ['displayLabel', 'description'];
        }        
            
        result.fields = runwayService.getFields(service.dto, attributeNames);      
            
        // Overwrite name field options       
        if(!dto.isNewInstance()) {
          result.fields[2].writable = dto.isNewInstance();              
        }
            
        result.object = object;
          
        onSuccess(result);
           
      }, onFailure);
      
      
      if(dashboardId != null) {
          
        /*
         * First: Lock the dashboard object
         */
        var lockRequest = runwayService.createStandbyRequest(element, function(dto){          
          dto.getDashboardDefinition(request);
        });
                
        net.geoprism.dashboard.Dashboard.lock(lockRequest, dashboardId);
      }
      else {
        var dto = new net.geoprism.dashboard.Dashboard();
        dto.getDashboardDefinition(request);
      }
    }
    
    
    return service;  
  }
  
  angular.module("builder-service", ["runway-service"]);
  angular.module("builder-service")
    .factory('builderService', BuilderService)
})();