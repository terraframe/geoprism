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
                   
//        net.geoprism.dashboard.layer.DashboardLayer.updateLegend(request, layer.layerId, layer.legendXPosition, layer.legendYPosition, layer.groupedInLegend);        
        
        runwayService.http({
          url: com.runwaysdk.__applicationContextPath + '/dashboard-layer/update-legend', 
          method: "POST",
          data: {layerId: layer.layerId, legendXPosition: layer.legendXPosition, legendYPosition: layer.legendYPosition, groupedInLegend: layer.groupedInLegend}
        }, request);                              
      }
    };
    
    service.removeLayer = function(layerId, elementId, onSuccess, onFailure) {
      if(service.canEdit()) {      
        var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
          
//        com.runwaysdk.Facade.deleteEntity(request, layerId);
        
        runwayService.http({
          url: com.runwaysdk.__applicationContextPath + '/dashboard-layer/remove', 
          method: "POST",
          data: {layerId: layerId}
        }, request);                                      
      }
    };
    
    service.orderLayers = function(mapId, layerIds, elementId, onSuccess, onFailure) {
      if(service.canEdit()) {      
        var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
        
//        net.geoprism.dashboard.DashboardMap.orderLayers(request, mapId, layerIds);
        
        runwayService.http({
          url: com.runwaysdk.__applicationContextPath + '/dashboard-map/order-layers', 
          method: "POST",
          data: {mapId: mapId, layerIds:layerIds}
        }, request);                      
      }
    };
    
    service.setDashboardBaseLayer = function(dashboardId, baseMap, onSuccess, onFailure) {
      if(service.canEdit()){
        var request = runwayService.createRequest(onSuccess, onFailure);
      
//        net.geoprism.dashboard.Dashboard.setBaseLayerState(request, dashboardId, baseMap);
        
        runwayService.http({
          url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/base-layer-state', 
          method: "POST",
          data: {dashboardId: dashboardId, baseLayerState:baseMap}
        }, request);              
      }
    }
    
    service.refreshMap = function(state, elementId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
              
//      net.geoprism.dashboard.DashboardMap.refresh(request, state.mapId, state);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-map/refresh', 
        method: "POST",
        data: {mapId: state.mapId, state:state}
      }, request);      
    }
    
    service.getDashboardJSON = function(dashboardId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
          
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/get-json', 
        method: "GET",
        params: {dashboardId: dashboardId}
      }, request);      
    }
    
    service.getAvailableDashboardsAsJSON = function(dashboardId, onSuccess, onFailure) {
//      var request = runwayService.createRequest(onSuccess, onFailure);
      var request = runwayService.createStandbyRequest("#container", onSuccess, onFailure);
//    
//      net.geoprism.dashboard.Dashboard.getAvailableDashboardsAsJSON(request, dashboardId);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/available-dashboards', 
        method: "GET",
        params: {dashboardId: dashboardId}
      }, request);      
    }
    
    service.saveDashboardState = function(dashboardId, state, global, elementId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
      
//      net.geoprism.dashboard.Dashboard.saveState(request, dashboardId, state, global);      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/save-state', 
        method: "POST",
        data: {dashboardId: dashboardId, state:state, global:global}
      }, request);      
      
    }
    
    service.getGeoEntitySuggestions = function(dashboardId, text, limit, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
//      net.geoprism.dashboard.Dashboard.getGeoEntitySuggestions(request, dashboardId, text, size);
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/geo-suggestions', 
        method: "GET",
        params: {dashboardId: dashboardId, text:text, limit:limit}
      }, request);            
    }
    
    service.getTextSuggestions = function(mdAttributeId, term, limit, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);

//      net.geoprism.dashboard.Dashboard.getTextSuggestions(request, mdAttributeId, term, limit);
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/text-suggestions', 
        method: "GET",
        params: {mdAttributeId: mdAttributeId, text:text, limit:limit}
      }, request);                  
    }
    
    service.getFeatureInformation = function(feature, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);

      var layerId = feature.layerId;
      var featureId = feature.geoId;

//      net.geoprism.dashboard.layer.DashboardThematicLayer.getFeatureInformation(request, layerId, geoId); 	
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/thematic-layer/feature-information', 
        method: "GET",
        params: {layerId: layerId, featureId:featureId}
      }, request);                  
    }
    
    service.hasReport = function(dashboardId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
      
//      net.geoprism.dashboard.Dashboard.hasReport(request, dashboardId);    	
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/has-report', 
        method: "GET",
        params: {dashboardId: dashboardId}
      }, request);                  
    }
    
    service.runReport = function(dashboardId, configuration, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
//      net.geoprism.report.ReportItemController.run(request, dashboardId, configuration);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-report/run', 
        method: "POST",
        data: {report: dashboardId, configuration:configuration}
      }, request);      
    }
    
    service.removeDashboard = function(dashboardId, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
            
//      com.runwaysdk.Facade.deleteEntity(request, dashboardId);    	
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/remove', 
        method: "POST",
        data: {dashboardId: dashboardId}
      }, request);            
    }
    
    service.editReport = function(dashboardId, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
        
//      net.geoprism.report.ReportItemController.remove(request, dashboardId);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-report/edit', 
        method: "POST",
        data: {dashboardId: dashboardId}
      }, request);            
    }
    
    service.unlockReport = function(oid, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-report/unlock', 
        method: "POST",
        data: {oid: oid}
      }, request);            
    }
    
    service.removeReport = function(dashboardId, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-report/remove', 
        method: "POST",
        data: {dashboardId: dashboardId}
      }, request);            
    }
    
    service.uploadReport = function(dashboardId, file, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
      
//      net.geoprism.report.ReportItemController.remove(request, dashboardId);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-report/upload', 
        method: "POST",
        headers: {'Content-Type': undefined},
        data: {dashboardId: dashboardId, file:file},
        transformRequest: function (data, headersGetter) {
          var formData = new FormData();
          angular.forEach(data, function (value, key) {
            formData.append(key, value);
          });

          return formData;
        }        
      }, request);            
    }
    
    service.cloneDashboard = function(dashboardId, label, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
    
//      net.geoprism.dashboard.DashboardController.clone(request, dashboardId, label);                    
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/clone', 
        method: "POST",
        data: {dashboardId: dashboardId, label:label}
      }, request);                  
    }
    
    service.newClone = function(dashboardId, elementId, onSuccess, onFailure) {
      var request = runwayService.createStandbyRequest(elementId, onSuccess, onFailure);
      
//      net.geoprism.dashboard.DashboardController.newClone(request, dashboardId);    
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/new-clone', 
        method: "POST",
        data: {dashboardId: dashboardId}
      }, request);                        
    }
    
    service.getClassifierTree = function(mdAttributeId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);    
      
//      net.geoprism.dashboard.Dashboard.getClassifierTree(request, mdAttributeId);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/classifier-tree', 
        method: "GET",
        params: {mdAttributeId: mdAttributeId}
      }, request);                              
    }
    
    service.generateThumbnailImage = function(dashboardId, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);    
      
//      net.geoprism.dashboard.Dashboard.generateThumbnailImage(request, dashboardId);      
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/generate-thumbnail', 
        method: "POST",
        data: {dashboardId: dashboardId}
      }, request);                              
    }
    
    service.setDataSetOrder = function(dashboardId, typeIds, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
        
//      net.geoprism.dashboard.Dashboard.setMetadataWrapperOrder(request, dashboardId, typeIds);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/set-dataset-order', 
        method: "POST",
        data: {dashboardId: dashboardId, typeIds:typeIds}
      }, request);                                    
    }
    
    service.setDataSetAttributeOrder = function(dashboardId, typeId, attributeIds, onSuccess, onFailure) {
      var request = runwayService.createRequest(onSuccess, onFailure);
    
//      net.geoprism.dashboard.Dashboard.setDashboardAttributesOrder(request, dashboardId, typeId, attributeIds);
      
      runwayService.http({
        url: com.runwaysdk.__applicationContextPath + '/dashboard-controller/set-attribute-order', 
        method: "POST",
        data: {dashboardId: dashboardId, typeId: typeId, attributeIds: attributeIds}
      }, request);                                          
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