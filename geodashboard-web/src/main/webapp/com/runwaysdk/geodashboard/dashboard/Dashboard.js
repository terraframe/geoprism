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
    controller.mapFactory = new com.runwaysdk.geodashboard.gis.OpenLayersMap("mapDivId", null, null);
    
    controller.baseLayers = controller.mapFactory.createBaseLayers();

    /* Default Dashboard State */ 
    controller.dashboards = [];
    controller.dashboardId = '';
    controller.model = {
      location : {label:'',value:''},
      editDashboard : false,
      editData : false,
      types : [],
      mapId : '',
      label : ''
    };
    
    /* Map state */
    controller.thematicLayerCache = {values:{}, ids:[]};
    controller.referenceLayerCache = {values:{}, ids:[]};
    controller.bbox = [];
    controller.renderBase = true;
    
    /* Initialization Function */
    $scope.init = function(workspace, editDashboard, editData) {
      dashboardService.setWorkspace(workspace);
      dashboardService.setEdit(editDashboard);
      dashboardService.setEditData(editData);
      dashboardService.setDashboard(controller);
      
      controller.mapFactory.setClickHandler(controller.onMapClick);

      controller.loadDashboardOptions();
    }
    
    controller.loadDashboardOptions = function(){
      
      onSuccess = function(json) {
        $timeout(function() {
          var response = JSON.parse(json);
        
          controller.dashboards = response.dashboards;
          controller.dashboardId = response.state.id;
        
          controller.setDashboardState(response.state);
          
          $scope.$apply();
        }, 0);
      };
          
      dashboardService.getAvailableDashboardsAsJSON(onSuccess);      
    }
    
    /* Refresh Map Function */
    controller.refresh = function() {
      onSuccess = function(json, dto, response) {
        controller.hideLayers();
                    
        var map = Mojo.Util.toObject(json);

        $timeout(function() {
          controller.setMapState(map, false);          
        }, 10);
        
        controller.renderReport();
          
        GDB.ExceptionHandler.handleInformation(response.getInformation());            
      };
      
      
      var state = controller.getCompressedState();
      dashboardService.refreshMap(state, '#filter-buttons-container', onSuccess);
    }
    
    controller.save = function() {
      var state = controller.getCompressedState();
      
      dashboardService.saveDashboardState(controller.dashboardId, state, '#filter-buttons-container');
    }
    
    /* Create a new layer */
    controller.newLayer = function(mdAttributeId) {    
      var form = new com.runwaysdk.geodashboard.gis.ThematicLayerForm(controller, controller.model.mapId);
      form.open(mdAttributeId);
    }  
    
    controller.getDashboardId = function() {
      return controller.dashboardId;
    }
    
    controller.setDashboardId = function(dashboardId) {
      if(controller.dashboardId != dashboardId) {
        controller.dashboardId = dashboardId;
        
        controller.loadDashboardState();
      }
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
    
    controller.loadDashboardState = function() {
        
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
        var state = JSON.parse(json);
          
        controller.setDashboardState(state);
      };
        
      dashboardService.getDashboardJSON(controller.dashboardId, onSuccess);
    }
    
    controller.setDashboardState = function(state) {
      $timeout(function() {
        controller.model = state;
        controller.renderBase = true;

        // Initialize the default base map
        var layerSourceType = controller.model.activeBaseMap["LAYER_SOURCE_TYPE"];
            
        for(var i = 0; i < controller.baseLayers.length; i++) {
          var layer = controller.baseLayers[i];
                
          if(layer.layerType == layerSourceType) {
            layer.isActive = true;
          }
          else {
            layer.isActive = false;            
          }
        }            

        $scope.$apply();
              
        controller.refresh();
      }, 5);
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
      controller.feature = null;

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
            cache.ids.unshift(layer.layerId);
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
            controller.mapFactory.hideLayer(layer);        
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
            return layer;
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
    
    controller.openDashboard = function(){
      var url = "?" + $.param({'dashboard' : controller.dashboardId}) ;
      
      var win = window.open(url, '_blank');
      win.focus();
    }
    
    /**
     * Performs the identify request when a user clicks on the map
     * 
     * @param id
     * 
     * <private> - internal method
     */
    controller.onMapClick = function(e) {
    
      var point = e.pixel;
      var workspace = controller.getWorkspace();
      var layers = controller.getThematicLayers();
      
      controller.mapFactory.getFeatureInfo(workspace, layers, point, controller.setFeatureInfo);        
    }
    
    controller.setFeatureInfo = function(info) {
      if(info != null) {
      
      var attributeValue = info.attributeValue;
      
        /* Localize the value if needed */
        if(typeof attributeValue === 'number'){
          var formatter = Globalize.numberFormatter();  
              
          info.attributeValue = formatter(attributeValue);
        }
        else if(attributeValue != null && !isNaN(Date.parse(attributeValue.substring(0, attributeValue.length - 1)))){
          var slicedAttr = attributeValue.substring(0, attributeValue.length - 1);
          var parsedAttr = $.datepicker.parseDate('yy-mm-dd', slicedAttr);
            
          var formatter = Globalize.dateFormatter({ date: "short" })
            
          info.attributeValue = formatter(parsedAttr);
        }    
        
        controller.feature = info;
        controller.geoId = info.geoId;
        
        $scope.$apply();
        
        controller.renderReport();
      }
    }
    
    controller.renderReport = function() {
      if($( "#report-viewport" ).length > 0) {
        
        // Get the width of the reporting div, make sure to remove some pixels because of
        // the side bar and some padding. convert px to pt
        var widthPt = Math.round(($('#report-content').width() - 20) * 72 / 96);
        var heightPt = Math.round($('#report-content').height() * 72 / 96);
        
        var layerId = (controller.feature != null ? controller.feature.layerId : '');
        var geoId = (controller.feature != null ? controller.feature.geoId : '');
        
        var configuration = {};
        configuration.parameters = [];
        configuration.parameters.push({'name' : 'width', 'value' : widthPt});
        configuration.parameters.push({'name' : 'height', 'value' : heightPt});
        configuration.parameters.push({'name' : 'layerId', 'value' : layerId});
        configuration.parameters.push({'name' : 'category', 'value' : geoId});
        configuration.parameters.push({'name' : 'state', 'value' : JSON.stringify(controller.getCompressedState())});
        
        var onSuccess = function(html){
          $( "#report-content" ).html(html);
        };
        
        dashboardService.runReport(controller.dashboardId, JSON.stringify(configuration), "#report-viewport", onSuccess);
      }
    }
    
    controller.exportReport = function(format) {
      var dashboardId = controller.getDashboardId();
      
      var layerId = (controller.feature != null ? controller.feature.layerId : '');
      var geoId = (controller.feature != null ? controller.feature.geoId : '');
      
      if(controller.model.hasReport) {          
        var configuration = {};
        configuration.parameters = [];
        configuration.parameters.push({'name' : 'format', 'value' : format});
        configuration.parameters.push({'name' : 'layerId', 'value' : layerId});
        configuration.parameters.push({'name' : 'category', 'value' : geoId});
        configuration.parameters.push({'name' : 'state', 'value' : JSON.stringify(controller.getCompressedState())});
            
        var params = {
          report : dashboardId,
          configuration : JSON.stringify(configuration) 
        }
                  
        var url = 'com.runwaysdk.geodashboard.report.ReportItemController.run.mojo?' + $.param(params);
                  
        window.location.href = url;  	  
      }
      else {
        var message = com.runwaysdk.Localize.localize("dashboard", "MissingReport");                    
          
        GDB.ExceptionHandler.handleException(message);        
      }
    }
    
    controller.getCompressedState = function() {
      var oState = controller.model;
      
      var state = {
        mapId : oState.mapId,
        types : []
      };
      
      if(!controller.isEmpty(oState.location)) {
        state.location = oState.location;
      }
      
      for(var i = 0; i < oState.types.length; i++) {
        var oType = oState.types[i];
        var oAttributes = oType.attributes;

        var attributes = [];
        
        if(oAttributes != null) {          
          for(var j = 0; j < oAttributes.length; j++) {
            var oAttribute = oAttributes[j];
            
            if(oAttribute.filter != null && !controller.isEmpty(oAttribute.filter)) {
              attributes.push(oAttribute);
            }            
          }          
        }
        
        if(attributes.length > 0) {
          var type = {
            id : oType.id,
            attributes : attributes
          }
            
          state.types.push(type);
        }
      }
      
      return state;
    }
    
    controller.isEmpty = function(filter)
    {
      for(var key in filter) {
        if(key != 'type' && key != 'operation' && filter.hasOwnProperty(key)) {
          return false;
        }
      }
      return true;
    }

  }
  
  angular.module("dashboard", ["dashboard-services", "report-panel", "dashboard-accordion", "dashboard-layer", "dashboard-map"]);
  angular
  .module('dashboard')
  .controller('DashboardController', DashboardController)
})();