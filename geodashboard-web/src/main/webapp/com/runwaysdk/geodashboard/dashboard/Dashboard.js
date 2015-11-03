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
    
    
    /* Initialization Function */
    $scope.init = function(dashboardId) {
      controller.dashboardId = dashboardId;
    }

    /* Getters */
    controller.getDashboardId = function() {
      return controller.dashboardId;
    }  

    $scope.$watch(controller.getDashboardId(), function(newVal, oldVal){
      controller.load();    
    }, true);
    
    /* Controller Functions */
    controller.load = function() {
      var request = new Mojo.ClientRequest({
        onSuccess : function(json){
          $timeout(function() {
            controller.model = JSON.parse(json);
            $scope.$apply();
            
            controller.refresh();
          }, 0);
        },
        onFailure : function(e){
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
        onSuccess : function(json) {
          var map = Mojo.Util.toObject(json);

          $timeout(function() {
            controller.refreshMap(map);
          }, 0);
        },
        onFailure : function(e) {
          console.log(e);
        }
      });
          
      com.runwaysdk.geodashboard.gis.persist.DashboardMap.refresh(request, controller.model.mapId, controller.model);
    }
    
    controller.save = function() {
      var request = new Mojo.ClientRequest({
        onSuccess : function() {
        },
        onFailure : function(e) {
          alert(e);
        }
      });
          
      com.runwaysdk.geodashboard.Dashboard.saveState(request, controller.dashboardId, controller.model);      
    }
    
    /* Create a new layer */
    controller.newLayer = function(mdAttributeId) {
    
      var form = new com.runwaysdk.geodashboard.gis.ThematicLayerForm(controller, controller.model.mapId);
      form.open(mdAttributeId);
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
      controller.refreshMap(map);
//      controller._addUserLayersToMap(true);    
    }
    
    controller.handleLayerEvent = function(map) {
      controller.refreshMap(map);
//        this._mapFactory.removeClickPopup();
//        this._addUserLayersToMap(true);    
    }
    
    controller.refreshMap = function(map) {
    
      if(map.layers){
        var cache = controller.getLayerCache();

        // Build thematic layer objects and populate the cache
        for (var i = 0; i < map.layers.length; ++i) {
          var layer = map.layers[i];
            
          var oldLayer = cache[layer.layerId];
          
          if (oldLayer != null) {
            layer.wmsLayerObj = oldLayer.wmsLayerObj;
            layer.isActive = oldLayer.isActive;                        
          }
          else {
            layer.isActive = true;
          }
          
          cache.values[layer.layerId] = layer;          
          cache.ids.unshift(layer.layerId);
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
//      if (map.bbox != null) {
//        controller.setBBOX(map.bbox);
//      }
      
      $scope.$apply();
    }
  }
  
  angular.module("dashboard", ["dashboard-accordion", "dashboard-layer"]);
  angular
  .module('dashboard')
  .controller('DashboardController', DashboardController)
})();