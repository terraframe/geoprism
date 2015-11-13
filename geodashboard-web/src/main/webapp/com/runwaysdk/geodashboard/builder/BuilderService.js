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
    service.dto = new com.runwaysdk.geodashboard.Dashboard();
    
    service.applyWithOptions = function(object, onSuccess, onFailure) {    
      var request = runwayService.createRequest(onSuccess, onFailure);

      runwayService.populate(service.dto, object);
     
      service.dto.applyWithOptions(request, object.options);
    }
    
    service.loadDashboard = function(dashboardId, onSuccess, onFailure) {
      
      var request = runwayService.createRequest(function(json, dto) {
        service.dto = dto;
      
        var object = JSON.parse(json);
    
        var result = {};
        /* Populate the list of country options */ 
        result.fields = runwayService.getFields(service.dto, ['name', 'displayLabel', 'country']);      
        result.fields[2].options = object.countries;      
        result.object = object;
      
        onSuccess(result);
        
      }, onFailure);
      
      if(dashboardId != null) {
      	var dto = new com.runwaysdk.geodashboard.Dashboard();
        dto.id = dashboardId;
        dto.newInstance = false;
        dto.attributeMap.id.value = dashboardId;
    	  
        dto.getDashboardDefinition(request);
      }
      else {
    	var dto = new com.runwaysdk.geodashboard.Dashboard();
        dto.getDashboardDefinition(request);
      }
    }
    
    return service;  
  }
  
  angular.module("builder-service", ["runway-service"]);
  angular.module("builder-service")
    .factory('builderService', BuilderService)
})();