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
  
  function DashboardService(runwayService) {
    var service = {};
    service.edit = false;
    service.editData = false;
    service.dashboard = null;
    
    service.setEdit = function(edit) {
      service.edit = edit;
    };
    
    service.canEdit = function() {
      return service.edit;
    };
    
    service.setEditData = function(editData) {
      service.editData = editData;
    };
    
    service.canEditData = function() {
      return service.editData;
    };
    
    service.setDashboard = function(dashboard) {
      service.dashboard = dashboard;
    };
    
    service.getDashboard = function() {
      return service.dashboard;
    };
    
    service.getSelectedFeatureInfo = function() {
    	return service.getDashboard().feature;
    }
        
    service.updateLegend = function(layer, onSuccess, onFailure) {
      if(service.canEdit()) {
        var request = runwayService.createRequest(onSuccess, onFailure);
                   
        net.geoprism.dashboard.layer.DashboardLayer.updateLegend(request, layer.layerId, layer.legendXPosition, layer.legendYPosition, layer.groupedInLegend);        
      }
    };
    
    service.removeLayer = function(layerId, elementId, onSuccess, onFailure) {
      if(service.canEdit()) {      
        var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
          
        com.runwaysdk.Facade.deleteEntity(request, layerId);
      }
    };
    
    service.orderLayers = function(mapId, layerIds, elementId, onSuccess, onFailure) {
      if(service.canEdit()) {      
        var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
        
        net.geoprism.dashboard.DashboardMap.orderLayers(request, mapId, layerIds);
      }
    };
    
    service.setDashboardBaseLayer = function(dashboardId, baseMap, onSuccess, onFailure) {
      if(service.canEdit()){
        var request = runwayService.createRequest(onSuccess, onFailure);
      
        net.geoprism.dashboard.Dashboard.setBaseLayerState(request, dashboardId, baseMap);
      }
    }
    
    service.refreshMap = function(state, elementId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
              
      net.geoprism.dashboard.DashboardMap.refresh(request, state.mapId, state);
    }
    
    service.getDashboardJSON = function(dashboardId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
          
      net.geoprism.dashboard.Dashboard.getJSON(request, dashboardId);
    }
    
    service.getAvailableDashboardsAsJSON = function(dashboardId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
      net.geoprism.dashboard.Dashboard.getAvailableDashboardsAsJSON(request, dashboardId);
    }
    
    service.saveDashboardState = function(dashboardId, state, global, elementId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
      
      net.geoprism.dashboard.Dashboard.saveState(request, dashboardId, state, global);      
    }
    
    service.getGeoEntitySuggestions = function(dashboardId, text, size, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
      net.geoprism.dashboard.Dashboard.getGeoEntitySuggestions(request, dashboardId, text, size);
    }
    
    service.getTextSuggestions = function(mdAttributeId, term, limit, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);

      net.geoprism.dashboard.Dashboard.getTextSuggestions(request, mdAttributeId, term, limit);
    }
    
    service.getFeatureInformation = function(feature, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);

      var layerId = feature.layerId;
      var geoId = feature.geoId;

      net.geoprism.dashboard.layer.DashboardThematicLayer.getFeatureInformation(request, layerId, geoId); 	
    }
    
    service.hasReport = function(dashboardId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
      
      net.geoprism.dashboard.Dashboard.hasReport(request, dashboardId);    	
    }
    
    service.runReport = function(dashboardId, configuration, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
    	
      net.geoprism.report.ReportItemController.run(request, dashboardId, configuration);
    }
    
    service.removeDashboard = function(dashboardId, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
            
      com.runwaysdk.Facade.deleteEntity(request, dashboardId);    	
    }
    
    service.cloneDashboard = function(dashboardId, label, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
    
      net.geoprism.dashboard.DashboardController.clone(request, dashboardId, label);                    
    }
    
    service.newClone = function(dashboardId, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
      
      net.geoprism.dashboard.DashboardController.newClone(request, dashboardId);    
    }
    
    service.getClassifierTree = function(mdAttributeId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);    
      
      net.geoprism.dashboard.Dashboard.getClassifierTree(request, mdAttributeId);
    }
    
    service.generateThumbnailImage = function(dashboardId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);    
      
      net.geoprism.dashboard.Dashboard.generateThumbnailImage(request, dashboardId);      
    }
    
    service.setDataSetOrder = function(dashboardId, typeIds, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
        
      net.geoprism.dashboard.Dashboard.setMetadataWrapperOrder(request, dashboardId, typeIds);
    }
    
    service.setDataSetAttributeOrder = function(dashboardId, typeId, attributeIds, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
      net.geoprism.dashboard.Dashboard.setDashboardAttributesOrder(request, dashboardId, typeId, attributeIds);
    }
    
    service.isEmptyFilter = function(filter) {
      for(var key in filter) {
        if(key != 'type' && key != 'operation' && key != 'mdAttribute' && filter.hasOwnProperty(key)) {
          var value = filter[key];
          
          if(value != null) {
            if( $.type( value ) === "string") {
              if(value.length > 0) {
                return false;
              }
            }
            else {
              return false;            
            }
          }
        }
      }
      return true;
    }
    
    return service;
  }
  
  angular.module("dashboard-service", ["runway-service"]);
  angular.module("dashboard-service")
    .factory('dashboardService', DashboardService)
})();