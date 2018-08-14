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

  function BuilderService($http, runwayService, localizationService) {
    var service = {};
//    service.dto = new net.geoprism.dashboard.Dashboard();
    
    service.applyWithOptions = function(object, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
      
      for(var key in object) {      
        service.dto[key] = object[key];
      }
        
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/apply-with-options', 
        method: "POST",
        data: {dto: service.dto, options:object.options}
      }, request);      
    }
    
    service.getLayersToDelete = function(object, element, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(element, onSuccess, onFailure);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/layers-to-delete', 
        method: "POST",
        data: {dashboardId: service.dto.oid, options:object.options}
      }, request);      
    }
    
    service.unlock = function(object, onSuccess, onFailure) {
      if(service.dto == null || service.dto.newInstance) {
        onSuccess(true);  
      }
      else {
        var request = runwayService.createRequest(function(){onSuccess(false)}, onFailure);
        
        runwayService.http({
          url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/unlock', 
          method: "POST",
          data: {dashboardId: service.dto.oid}
        }, request);      
      }
    }    
    
    service.loadDashboard = function(dashboardId, element, onSuccess, onFailure) {
      
      /*
       * Second: Get all options
       */
      var request = runwayService.createStandbyRequest(element, function(response) {        
        var object = response.data.definition;
        service.dto = response.data.dto;
        
        var result = {
          fields : []            
        };        
        
        // Populate the list of country options  
        // ORDER MATTERS for this array of field names. Fields will be added to the form in order.
        var attributeNames = ['displayLabel', 'description', 'name'];
            
        if(service.dto.newInstance) {
          attributeNames = ['displayLabel', 'description'];
        }
        
        attributeNames.forEach(function(attributeName) {
          var field = {};
          field.name = attributeName;
          field.type = 'text';
          field.message = '';
          field.label = localizationService.localize("net.geoprism.dashboard.Dashboard", attributeName);          
          field.required = attributeName === "description" ? false : true;
          field.readable = true;
          field.writable = true;
        
          result.fields.push(field);          
        });
            
//        result.fields = runwayService.getFields(service.dto, attributeNames);      
            
        // Overwrite name field options       
        if(!service.dto.newInstance) {
          result.fields[2].writable = true;              
        }
            
        result.object = object;
          
        onSuccess(result);
           
      }, onFailure);
      
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/dashboard-definition', 
        method: "POST",
        data: dashboardId != null ? {dashboardId: dashboardId} : {}
      }, request);      
    }
    
    
    return service;  
  }
  
  angular.module("builder-service", ["runway-service", "localization-service"]);
  angular.module("builder-service")
    .factory('builderService', BuilderService)
})();