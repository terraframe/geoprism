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

  function LocationService(runwayService) {
    var service = {};
    
    service.select = function(connection, id, universalId, existingLayers) {
      var req = {
        method: 'POST',
        url: com.runwaysdk.__applicationContextPath + '/location/select',
        data : {
          id : id,
          universalId : universalId,
          existingLayers : existingLayers
        }
      }      
      
      runwayService.execute(req, connection);      
    }
    
    service.open = function(connection, id, existingLayers) {
      var req = {
        method: 'POST',
        url: com.runwaysdk.__applicationContextPath + '/location/open',
        data : {
          id : id,
          existingLayers : existingLayers            
        }
      }      
      
      runwayService.execute(req, connection);      
    }
    
    service.getGeoEntitySuggestions = function(connection, text, limit) {
      var req = {
        method: 'POST',
        url: com.runwaysdk.__applicationContextPath + '/location/suggestions',
        data : {
          text : text,
          limit : limit
        }
      }      
              
      runwayService.execute(req, connection);      
    }
      
    return service;  
  }
  
  angular.module("location-service", ["runway-service"]);
  angular.module("location-service")
    .factory('locationService', LocationService)
})();
