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
  function DashboardController($scope, $timeout) {
    var controller = this;

    /* Default State */ 
    controller.dashboardId = '';
    controller.workspace = '';
    controller.model = {
      location : {label:'',value:''},
      editDashboard : false,
      editData : false,
      types : [],
      mapId : '',
      dashboardId : controller.dashboardId
    };
    /* Map state */
    controller.thematicLayerCache = {values:{}, ids:[]};
    controller.referenceLayerCache = {values:{}, ids:[]};
    controller.bbox = [];
    controller.mapFactory = new com.runwaysdk.geodashboard.gis.OpenLayersMap("mapDivId", null, null, true, controller);
    controller.baseLayers = controller.mapFactory.createBaseLayers();            	
    
    /* Initialization Function */
    $scope.init = function(dashboardId, workspace) {
      controller.dashboardId = dashboardId;
      controller.workspace = workspace;
    }

    /* Controller Functions */
    controller.load = function() {
      var request = new Mojo.ClientRequest({
        onSuccess : function(json){
          $timeout(function() {
            controller.model = JSON.parse(json);

            // Initialize the default base map
            for(var i = 0; i < controller.baseLayers.length; i++) {
              var layer = controller.baseLayers[i];
              
              if(layer.layerType == controller.model.activeBaseMap["LAYER_SOURCE_TYPE"]) {
                layer.isActive = true;
              }
            }
            

            $scope.$apply();
            
            controller.refresh();
            controller.refreshBaseLayer();
          }, 0);
        },
        onFailure : function(e){
          GDB.ExceptionHandler.handleException(e);        	
        }
      });
      
      /* Clear the current state */
      controller.model = {
        location : {label:'',value:''},
        editDashboard : false,
        editData : false,
        types : [],
        mapId : '',
        dashboardId : controller.dashboardId
      };
      controller.thematicLayerCache = {values:{}, ids:[]};
      controller.referenceLayerCache = {values:{}, ids:[]};
      
      com.runwaysdk.geodashboard.Dashboard.getJSON(request, controller.dashboardId);
    }
    
    /* Refresh Map Function */
    controller.refresh = function() {
      var request = new Mojo.ClientRequest({
        onSuccess : function(json, dto, response) {
          controller.hideLayers();
                    	
          var map = Mojo.Util.toObject(json);

          $timeout(function() {
            controller.setMapState(map, false);
          }, 10);
          
          GDB.ExceptionHandler.handleInformation(response.getInformation());            
        },
        onFailure : function(e) {
          GDB.ExceptionHandler.handleException(e);
        }
      });
          
      com.runwaysdk.geodashboard.gis.persist.DashboardMap.refresh(request, controller.model.mapId, controller.model);
    }
    
    controller.save = function() {
      var request = new Mojo.ClientRequest({
        onSuccess : function() {
        },
        onFailure : function(e) {
          GDB.ExceptionHandler.handleException(e);
        }
      });
          
      com.runwaysdk.geodashboard.Dashboard.saveState(request, controller.dashboardId, controller.model);      
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
      return controller.workspace;
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
      var rLayers = controller.getReferenceLayers();
      controller.mapFactory.createReferenceLayers(rLayers, controller.workspace, true);
            
      var tLayers = controller.getThematicLayers();
      controller.mapFactory.createUserLayers(tLayers, controller.workspace, true);    	  
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
        if(controller.canEdit()){
          var request = new Mojo.ClientRequest({
            onSuccess : function() {
              // No action needed
            },
            onFailure : function(e) {
              GDB.ExceptionHandler.handleException(e);
            }
          });
                
          // Persist base layer option to the db
          com.runwaysdk.geodashboard.Dashboard.setBaseLayerState(request, controller.dashboardId, JSON.stringify(baseMap));
        }
      }
    }
    
    controller.centerMap = function() {
      if(controller.bbox.length === 2){
        controller.mapFactory.setView(null, controller.bbox, 5);
      }
      else if(controller.bbox.length === 4){
        controller.mapFactory.setView(controller.bbox, null, null);
      }    	
    }
    
    /* Setup all watch functions */
    $scope.$watch(controller.getDashboardId(), function(newVal, oldVal){
      controller.load();    
    }, true);    
  }
  
  angular.module("dashboard", ["dashboard-accordion", "dashboard-layer"]);
  angular
  .module('dashboard')
  .controller('DashboardController', DashboardController)
})();