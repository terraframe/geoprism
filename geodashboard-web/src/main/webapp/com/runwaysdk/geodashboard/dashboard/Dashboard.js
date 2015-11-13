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
  function DashboardController($scope, $timeout, $compile) {
    var controller = this;
    
    /* Getting the $compile method reference for use with later functions  */
    controller.$compile = $compile;
    controller.$scope = $scope;
    
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
            
            console.log("Location: (" + controller.model.location.value + ", " + controller.model.location.label + ")");
            
            $scope.$apply();
            
            controller.refresh();
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
      form.open(mdAttributeId, controller.$compile, controller.$scope);
    }  
    
    controller.getDashboardId = function() {
      return controller.dashboardId;
    }  
    
    controller.getModel = function() {
      return controller.model;
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
    
    controller.setCurrentBaseMap = function(baseMap) {
      controller.model.baseMap = baseMap;    
    }
    
    controller.setBBOX = function(bbox) {
      controller.model.bbox = bbox;    
    }
    
    controller.handleReferenceLayerEvent = function(map) {
      controller.setMapState(map, true);
    }
    
    controller.handleLayerEvent = function(map) {
      controller.setMapState(map, true);
    }
    
    controller.setMapState = function(map, reverse) {
      if (map.bbox != null) {
        controller.bbox = map.bbox;
      }   	
    
      if(map.layers){
        var cache = controller.getLayerCache();

        // Build thematic layer objects and populate the cache
        for (var i = 0; i < map.layers.length; ++i) {
          var layer = map.layers[i];
            
          var oldLayer = cache.values[layer.layerId];
          
          if (oldLayer != null) {
            layer.isActive = oldLayer.isActive;                        
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
          }
          
          cache.values[layer.layerId] = layer;          
        }
      }
        
      // Build reference layer objects and populate the cache
//      if(map.refLayers){
//        var cache = controller.getReferenceLayerCache();
//      
//        for (var r = 0; r < map.refLayers.length; ++r) {
//          var layer = map.refLayers[r];
//            
//          // Construct the layer view objects
//          var view = com.runwaysdk.geodashboard.gis.LayerViewFactory.createLayerView(layer);
//          
//          var oldLayer = cache.get(view.universalId);
//          
//          if (oldLayer != null) {
//            view.wmsLayerObj = oldLayer.wmsLayerObj;
//          }
//            
//          cache.put(view.universalId, view);
//        }
//      }
//        
//      if (map.activeBaseMap != null) {
//        controller.setCurrentBaseMap(map.activeBaseMap);
//      }
//        
      $scope.$apply();
      
      controller.renderMap();
    }
    
    controller.renderMap = function() {
      if(controller.bbox.length === 2){
        controller.mapFactory.setView(null, controller.bbox, 5);
      }
      else if(controller.bbox.length === 4){
        controller.mapFactory.setView(controller.bbox, null, null);
      }
              
      if(controller.baseLayers == null) {
        controller.baseLayers = controller.mapFactory.createBaseLayers();                                    	  
//        this._renderBaseLayerSwitcher(baseLayers);          
      }
            
//    var refLayers = this._refLayerCache.values();
//    controller.mapFactory.createReferenceLayers(refLayers, this._workspace, removeExisting);
            
      var layers = controller.getThematicLayers();
      controller.mapFactory.createUserLayers(layers, controller.workspace, true);    	  
    }
    
    controller.toggleLayer = function(layer) {
      if(!layer.isActive) {
        controller.mapFactory.hideLayer(layer);            
      }
      else {
        controller.renderMap();
      }
    }
    
    controller.hideLayers = function() {
     var layers = controller.getThematicLayers();
     controller.mapFactory.hideLayers(layers);     
    }
    
    /* Setup all watch functions */
    $scope.$watch(controller.getDashboardId(), function(newVal, oldVal){
      controller.load();    
    }, true);    
  }
  
  angular.module("dashboard", ["dashboard-accordion", "dashboard-layer","dashboard-layer-form"]);
  angular
  .module('dashboard')
  .controller('DashboardController', DashboardController)
})();