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
  
  function DashboardService() {
    var service = {};
    service.edit = false;
    service.workspace = '';
    
    service.setEdit = function(edit) {
      service.edit = edit;
    };
    
    service.canEdit = function(edit) {
      return service.edit;
    };
    
    service.setWorkspace = function(workspace) {
      service.workspace = workspace;
    };
    
    service.getWorkspace = function(workspace) {
      return service.workspace;
    };
    
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
    
    service.updateLegend = function(layer, onSuccess, onFailure) {
      if(service.canEdit()) {
        var request = service.createRequest(onSuccess, onFailure);
                   
        com.runwaysdk.geodashboard.gis.persist.DashboardLayer.updateLegend(request, layer.layerId, layer.legendXPosition, layer.legendYPosition, layer.groupedInLegend);        
      }
    };
    
    service.removeLayer = function(layerId, elementId, onSuccess, onFailure) {
      if(service.canEdit()) {      
        var request = service.createStandbyRequest(elementId, onSuccess, onFailure);
          
        com.runwaysdk.Facade.deleteEntity(request, layerId);
      }
    };
    
    service.orderLayers = function(mapId, layerIds, elementId, onSuccess, onFailure) {
      if(service.canEdit()) {      
        var request = service.createStandbyRequest(elementId, onSuccess, onFailure);
        
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.orderLayers(request, mapId, layerIds);
      }
    };
    
    service.setDashboardBaseLayer = function(dashboardId, baseMap, onSuccess, onFailure) {
      if(service.canEdit()){
        var request = service.createRequest(onSuccess, onFailure);
      
        com.runwaysdk.geodashboard.Dashboard.setBaseLayerState(request, dashboardId, baseMap);
      }
    }
    
    service.refreshMap = function(state, onSuccess, onFailure) {
      var request = service.createRequest(onSuccess, onFailure);
              
      com.runwaysdk.geodashboard.gis.persist.DashboardMap.refresh(request, state.mapId, state);
    }
    
    service.getDashboardJSON = function(dashboardId, onSuccess, onFailure) {
      var request = service.createRequest(onSuccess, onFailure);
          
      com.runwaysdk.geodashboard.Dashboard.getJSON(request, dashboardId);
    }
    
    service.saveDashboardState = function(dashboardId, state, elementId, onSuccess, onFailure) {
      var request = service.createStandbyRequest(elementId, onSuccess, onFailure);
      
      com.runwaysdk.geodashboard.Dashboard.saveState(request, dashboardId, state);      
    }
    
    service.getGeoEntitySuggestions = function(dashboardId, text, size, onSuccess, onFailure) {
      var request = service.createRequest(onSuccess, onFailure);
    
      com.runwaysdk.geodashboard.Dashboard.getGeoEntitySuggestions(request, dashboardId, text, size);
    }
    
    return service;
  }
  
  angular.module("dashboard-services", []);
  angular.module('dashboard-services')
    .factory('dashboardService', DashboardService)
})();