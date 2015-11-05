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
  function DashboardController($scope, $timeout, dashboardService) {
    var controller = this;

    /* Default State */ 
    controller.dashboardId = '';
    controller.model = {
      location : {label:'',value:''},
      editDashboard : false,
      editData : false,
      types : [],
      mapId : ''
    };
    
    /* Map state */
    controller.thematicLayerCache = {values:{}, ids:[]};
    controller.referenceLayerCache = {values:{}, ids:[]};
    controller.bbox = [];
    controller.mapFactory = new com.runwaysdk.geodashboard.gis.OpenLayersMap("mapDivId", null, null, true, controller);
    controller.baseLayers = controller.mapFactory.createBaseLayers();
    controller.renderBase = true;
    
    /* Initialization Function */
    $scope.init = function(dashboardId, workspace, edit) {
      controller.dashboardId = dashboardId;
      
      dashboardService.setWorkspace(workspace);
      dashboardService.setEdit(edit);
    }

    /* Controller Functions */
    controller.load = function() {
        
      /* Clear the current state */
      controller.model = {
        location : {label:'',value:''},
        editDashboard : false,
        editData : false,
        types : [],
        mapId : ''
      };
      controller.thematicLayerCache = {values:{}, ids:[]};
      controller.referenceLayerCache = {values:{}, ids:[]};
    
      var onSuccess = function(json){
        $timeout(function() {
          controller.model = JSON.parse(json);
          controller.renderBase = true;
          
          // Initialize the default base map
          for(var i = 0; i < controller.baseLayers.length; i++) {
            var layer = controller.baseLayers[i];
              
            if(layer.layerType == controller.model.activeBaseMap["LAYER_SOURCE_TYPE"]) {
              layer.isActive = true;
            }
          }            

          $scope.$apply();
            
          controller.refresh();
        }, 0);
      };
      
      dashboardService.getDashboardJSON(controller.dashboardId, onSuccess);
    }
    
    /* Refresh Map Function */
    controller.refresh = function() {
      onSuccess = function(json, dto, response) {
        controller.hideLayers();
                    
        var map = Mojo.Util.toObject(json);

        $timeout(function() {
          controller.setMapState(map, false);
        }, 10);
          
        GDB.ExceptionHandler.handleInformation(response.getInformation());            
      };
      
      dashboardService.refreshMap(controller.model, onSuccess);
    }
    
    controller.save = function() {
      dashboardService.saveDashboardState(controller.dashboardId, controller.model, '#filter-buttons-container');
    }
    
    /* Create a new layer */
    controller.newLayer = function(mdAttributeId) {    
      var form = new com.runwaysdk.geodashboard.gis.ThematicLayerForm(controller, controller.model.mapId);
      form.open(mdAttributeId);
    }  
    
    controller.getDashboardId = function() {
      return controller.dashboardId;
    }

    controller.getWorkspace = function() {
      return dashboardService.getWorkspace();
    } 
    
    controller.getModel = function() {
      return controller.model;
    }

    controller.canEdit = function() {
      return controller.model.editDashboard;
    }

    /* Layer Cache Getters/Setters */
    controller.getLayerCache = function() {
      return controller.thematicLayerCache;
    }
    
    controller.getLayer = function(layerId) {
      return controller.getLayerCache()[layerId];        
    }
    
    controller.getThematicLayers = function() {
      var layers = [];
      
      for(var i = 0; i < controller.thematicLayerCache.ids.length; i++) {
        var layerId = controller.thematicLayerCache.ids[i];
        
        layers.push(controller.thematicLayerCache.values[layerId]);
      }
      
      return layers;
    }
    
    /* Reference Layer Cache Getters/Setters */
    controller.getReferenceLayerCache = function() {
      return controller.referenceLayerCache;
    }
    
    controller.getReferenceLayer = function(layerId) {
      return controller.getReferenceLayerCache()[layerId];        
    }
    
    controller.getReferenceLayers = function() {
      var layers = [];
        
      // References are reverse order
      for(var i = (controller.referenceLayerCache.ids.length - 1); i >= 0; i--) {
        var layerId = controller.referenceLayerCache.ids[i];
          
        layers.push(controller.referenceLayerCache.values[layerId]);
      }
        
      return layers;
    }    
    
    controller.handleLayerEvent = function(map) {
      controller.setMapState(map, true);
    }
    
    controller.setMapState = function(map, reverse) {
      if (map.bbox != null) {
        angular.copy(map.bbox, controller.bbox);
        
        controller.centerMap();
      }   
    
      if(map.layers){
        var cache = controller.getLayerCache();

        // Build thematic layer objects and populate the cache
        for (var i = 0; i < map.layers.length; ++i) {
          var layer = map.layers[i];
          layer.key = layer.layerId;
            
          var oldLayer = cache.values[layer.layerId];
          
          if (oldLayer != null) {
            layer.isActive = oldLayer.isActive;
          
          angular.copy(layer, oldLayer);            
          }
          else {
            layer.isActive = true;
            
            /* Add new item to the map */            
            if(reverse) {
              cache.ids.unshift(layer.layerId);
            }
            else {
              cache.ids.push(layer.layerId);
            }
            
            cache.values[layer.layerId] = layer;
          }          
        }
      }
        
      // Build reference layer objects and populate the cache
      if(map.refLayers){
        var cache = controller.getReferenceLayerCache();
      
        for (var r = 0; r < map.refLayers.length; ++r) {
          var layer = map.refLayers[r];
          layer.key = layer.universalId;
            
          var oldLayer = cache.values[layer.universalId];
            
          if (oldLayer != null) {
          if((oldLayer.layerExists && layer.layerExists) || (!oldLayer.layerExists && !layer.layerExists)) {          
              layer.isActive = oldLayer.isActive;
          }
          
          angular.copy(layer, oldLayer);
          }
          else {          
            /* Add new item to the cache */
          
            cache.ids.unshift(layer.universalId);
            cache.values[layer.universalId] = layer;  
          }
        }
      }
      
      $scope.$apply();
      
      controller.renderMap();
    }
    
    controller.renderMap = function() {
      if(controller.renderBase) {
        controller.refreshBaseLayer();
        
        controller.renderBase = false;
      }
    
      var rLayers = controller.getReferenceLayers();
      controller.mapFactory.createReferenceLayers(rLayers, controller.getWorkspace(), true);
            
      var tLayers = controller.getThematicLayers();
      controller.mapFactory.createUserLayers(tLayers, controller.getWorkspace(), true);      
    }
    
    controller.toggleLayer = function(layer) {
      if(!layer.isActive) {
        controller.mapFactory.hideLayer(layer, true);            
      }
      else {
        controller.renderMap();
      }
    }
    
    controller.hideLayers = function() {
      var layers = controller.getThematicLayers();
      controller.mapFactory.hideLayers(layers);     
    }
    
    controller.refreshBaseLayer = function() {
      if(controller.baseLayers.length > 0) {
        var baseMap = {"LAYER_SOURCE_TYPE" : ""};

        for(var i = 0; i < controller.baseLayers.length; i++) {
          var layer = controller.baseLayers[i];
          
          if(layer.isActive) {
            baseMap = {"LAYER_SOURCE_TYPE" : layer.layerType};

            controller.mapFactory.showLayer(layer, 0);          
          }
          else {
            controller.mapFactory.hideLayer(layer, false);        
          }
        }
  
        /* Persist the changes to the active base map */
        dashboardService.setDashboardBaseLayer(controller.dashboardId, JSON.stringify(baseMap));
      }
    }

    controller.getActiveBaseLayer = function() {
      if(controller.baseLayers.length > 0) {
        for(var i = 0; i < controller.baseLayers.length; i++) {
          var layer = controller.baseLayers[i];
          
          if(layer.isActive) {
            return layer
          }
        }
      }
      
      return null;
    }    
    
    controller.centerMap = function() {
      if(controller.bbox.length === 2){
        controller.mapFactory.setView(null, controller.bbox, 5);
      }
      else if(controller.bbox.length === 4){
        controller.mapFactory.setView(controller.bbox, null, null);
      }    
    }
    
    controller.getMapExtent = function() {
      var mapBounds = {};
      
      var mapExtent = controller.mapFactory.getCurrentBounds(com.runwaysdk.geodashboard.gis.DynamicMap.SRID);
      mapBounds.left = mapExtent._southWest.lng;
      mapBounds.bottom = mapExtent._southWest.lat;
      mapBounds.right = mapExtent._northEast.lng;
      mapBounds.top = mapExtent._northEast.lat;
      
      return mapBounds;      
    }
    
    controller.getMapSize = function() {
      var mapSize = {};
      mapSize.width = $("#mapDivId").width();
      mapSize.height = $("#mapDivId").height();
      
      return mapSize;
    }
    
    controller.exportMap = function() {
      var map = controller.mapFactory.getMap();
      var mapId = controller.model.mapId;
      var mapBounds = controller.getMapExtent();
      var mapSize = controller.getMapSize();
      
      var layer = controller.getActiveBaseLayer();
      var activeBaseMap = (layer != null ? {"LAYER_SOURCE_TYPE" : layer.layerType} : {"LAYER_SOURCE_TYPE" : ""});
      
      var outFileName = "GeoDashboard_Map";
      var outFileFormat = "png";
      var mapBoundsStr = JSON.stringify(mapBounds);          
      var mapSizeStr = JSON.stringify(mapSize);            
      var activeBaseMapStr = JSON.stringify(activeBaseMap);
            
      if(activeBaseMap.LAYER_SOURCE_TYPE.toLowerCase() !== "osm"){        
        var title = com.runwaysdk.Localize.get("rWarning", "Warning");
        var message = com.runwaysdk.Localize.localize("dashboard", "InvalidBaseMap");
        
        GDB.ExceptionHandler.renderDialog(title, message);            
      }
      
      var params = {
        'mapId' : mapId,
        'outFileName' : outFileName,
        'outFileFormat' : outFileFormat,
        'mapBounds' : mapBoundsStr,
        'mapSize' : mapSizeStr,
        'activeBaseMap' : activeBaseMapStr
      };
      
      var url = 'com.runwaysdk.geodashboard.gis.persist.DashboardMapController.exportMap.mojo?' + $.param(params);
              
      window.location.href = url;            
    }
    
    /* Setup all watch functions */
    $scope.$watch(controller.getDashboardId(), function(newVal, oldVal){
      controller.load();    
    }, true);    
  }
  
  angular.module("dashboard", ["dashboard-services", "dashboard-accordion", "dashboard-layer"]);
  angular
  .module('dashboard')
  .controller('DashboardController', DashboardController)
})();