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
  
  function LayerFormService() {
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
    
    
    service.getThematicLayerJSON = function(layerId, onSuccess, onFailure) {
        var request = service.createRequest(onSuccess, onFailure);
            
        com.runwaysdk.geodashboard.gis.persist.DashboardThematicLayer.getJSON(request, layerId);
    }
    
    return service;
  }
  
  angular.module("layer-form-service", []);
  angular.module('layer-form-service')
    .factory('layerFormService', LayerFormService);
})();