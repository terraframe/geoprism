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

  function DashboardController($scope, $rootScope, $compile, $timeout, dashboardService, localizationService, mapService) {
    var controller = this;
    
    /* Getting the $compile method reference for use with later functions  */
    controller.$compile = $compile;
    controller.$scope = $scope;
    
    controller.baseLayers = mapService.createBaseLayers();

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
    
    // Variable used to dispatch events the builder modal
    controller.builder = null;
    
    /* Map state */
    controller.thematicLayerCache = {values:{}, ids:[]};
    controller.referenceLayerCache = {values:{}, ids:[]};
    controller.bbox = [];
    controller.renderBase = true;
    
    /* Initialization Function */
    $scope.init = function(workspace, editDashboard, editData) {
      
      dashboardService.setEdit(editDashboard);
      dashboardService.setEditData(editData);
      dashboardService.setDashboard(controller);

      mapService.setWorkspace(workspace);
      mapService.setClickHandler(controller.onMapClick);
      
      controller.loadDashboardOptions();
    }
    
    controller.loadDashboardOptions = function(){
      
      onSuccess = function(json) {
        $timeout(function() {
          var response = JSON.parse(json);
        
          controller.dashboards = response.dashboards;
          controller.dashboardId = response.state.id;
        
          controller.setDashboardState(response.state, true);
          
          $scope.$apply();
        }, 0);
      };
          
      var dashboardId =  controller.getQueryParameters().dashboard;
      
      dashboardService.getAvailableDashboardsAsJSON(dashboardId, onSuccess);      
    }
    
    controller.getQueryParameters = function() {
      var query_string = {};
      var query = window.location.search.substring(1);
      var vars = query.split("&");
      
      for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
            // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
          query_string[pair[0]] = decodeURIComponent(pair[1]);
            // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
        
          var arr = [ query_string[pair[0]],decodeURIComponent(pair[1]) ];
          query_string[pair[0]] = arr;
            // If third or later entry with this name
        } else {
          query_string[pair[0]].push(decodeURIComponent(pair[1]));
        }
      }
       
      return query_string;
    }
    
    
    
    /* Refresh Map Function */
    controller.refresh = function(buttonId, initialRender) {
      var onSuccess = function(json, dto, response) {
    	
        if(buttonId){
        	controller.icoSpin = null;
        	$scope.$apply();
        }
        
        controller.hideLayers();
                    
        var map = Mojo.Util.toObject(json);

        $timeout(function() {
          controller.setMapState(map, false, initialRender);
          
          controller.renderReport();
        }, 10);
                  
        GDB.ExceptionHandler.handleInformation(response.getInformation());            
      };      
      
      if(buttonId){
    	  controller.icoSpin = buttonId;
      }
      
      var state = controller.getCompressedState();
      dashboardService.refreshMap(state, '#filter-buttons-container', onSuccess);
    }
    
    controller.save = function(global, buttonId) {
      var state = controller.getCompressedState();
       
      var onSuccess = function(json, dto, response) {
          if(buttonId){
        	  controller.icoSpin = null;
        	  $scope.$apply();
          }
      };  
      
      if(buttonId){
    	  controller.icoSpin = buttonId;
      }
      
      dashboardService.saveDashboardState(controller.dashboardId, state, global, '#filter-buttons-container', onSuccess);
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
      return mapService.getWorkspace();
    } 
    
    controller.getModel = function() {
      return controller.model;
    }

    controller.canEdit = function() {
      return controller.model.editDashboard;
    }
    
    controller.clearDashboardState = function() {
        
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
       
      /* Remove all current layers */
      mapService.clear();    
    }
    
    controller.loadDashboardState = function() {      
      controller.clearDashboardState();
      
      var onSuccess = function(json){
        var state = JSON.parse(json);
          
        controller.setDashboardState(state, true);
      };
        
      dashboardService.getDashboardJSON(controller.dashboardId, onSuccess);
    }
    
    controller.setDashboardState = function(state, initialRender) {
      $timeout(function() {
        controller.model = state;
        controller.renderBase = true;
        
        // overwrite title with dashboard name
        document.title = state.label;

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
              
        controller.refresh(null, true);
      }, 5);
    }
    

    /* Layer Cache Getters/Setters */
    controller.getLayerCache = function() {
      return controller.thematicLayerCache;
    }
    
    controller.getLayer = function(layerId) {
      //// replaced because this doesn't actually get the layer
      //return controller.getLayerCache()[layerId];   
      ////
      return controller.getLayerCache().values[layerId];
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
      controller.setMapState(map, true, true);
      
      dashboardService.generateThumbnailImage(controller.dashboardId);
    }
    
    controller.setMapState = function(map, reverse, zoomToDataExtent) {
      controller.feature = null;

      if (map.bbox != null && zoomToDataExtent) {
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
            var index = cache.ids.length;
          
            for(var i = 0; i < cache.ids.length; i++) {
              var id = cache.ids[i];
              var cLayer = cache.values[id];              
              
              if(layer.index > cLayer.index) {
                index = i;
              }
            }
            
            cache.ids.splice(index, 0, layer.universalId);
          
            cache.values[layer.universalId] = layer;  
          }
        }
      }
      
      $scope.$apply();
      
      controller.renderMap();
    }
    
    controller.renderMap = function() {
      var rLayers = controller.getReferenceLayers();
      mapService.createReferenceLayers(rLayers, true);
            
      var tLayers = controller.getThematicLayers();
      mapService.createUserLayers(tLayers, true);      

      if(controller.renderBase) {
        controller.refreshBaseLayer();
          
        controller.renderBase = false;
      }
    }
    
    controller.toggleLayer = function(layer) {
      if(!layer.isActive) {
        mapService.hideLayer(layer);            
      }
      else {
        controller.renderMap();
      }
    }
    
    controller.hideLayers = function() {
      var layers = controller.getThematicLayers();
      mapService.hideLayers(layers);     
    }
    
    controller.refreshBaseLayer = function() {
      if(controller.baseLayers.length > 0) {
        var baseMap = {"LAYER_SOURCE_TYPE" : ""};

        for(var i = 0; i < controller.baseLayers.length; i++) {
          var layer = controller.baseLayers[i];
          
          if(layer.isActive) {
            baseMap = {"LAYER_SOURCE_TYPE" : layer.layerType};

            mapService.showLayer(layer, 0);          
          }
          else {
            mapService.hideLayer(layer);        
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
        mapService.setView(null, controller.bbox, 5, "EPSG:4326");
      }
      else if(controller.bbox.length === 4){
        mapService.setView(controller.bbox, null, null, "EPSG:4326");
      }    
    }
    
    controller.getMapExtent = function() {
      var mapBounds = {};
      
      var mapExtent = mapService.getCurrentBounds('EPSG:4326');
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
        var title = localizationService.localize("dashboard", "warning");
        var message = localizationService.localize("dashboard", "InvalidBaseMap");
        
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
      
      var url = 'net.geoprism.dashboard.DashboardMapController.exportMap.mojo?' + $.param(params);
              
      window.location.href = url;            
    }
    
    controller.exportLayerData = function(layerId) {
      var mapId = controller.model.mapId;      
      var state = controller.getCompressedState();
      
      var params = {
        'mapId' : mapId,
        'state' : state,
        'layerId' : layerId
      };
            
      var url = 'net.geoprism.dashboard.DashboardMapController.exportLayerData.mojo?' + $.param(params);
                    
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
      
      mapService.clearOverlays();
    	
      var layers = controller.getThematicLayers();
      
      mapService.getFeatureInfo(layers, e, controller.setFeatureInfo);        
    }
    
    controller.setFeatureInfo = function(info) {
      if(info != null) {
      
        var attributeValue = info.attributeValue;
      
        /* Localize the value if needed */
        if(typeof attributeValue === 'number'){
          info.attributeValue = localizationService.formatNumber(attributeValue);
        }
        else if(attributeValue != null && !isNaN(Date.parse(attributeValue.substring(0, attributeValue.length - 1)))){
          var slicedAttr = attributeValue.substring(0, attributeValue.length - 1);
          var parsedAttr = $.datepicker.parseDate('yy-mm-dd', slicedAttr);
            
          var formatter = Globalize.dateFormatter({ date: "short" })
            
          info.attributeValue = formatter(parsedAttr);
        }    
        
        controller.feature = info;
        controller.feature.show = true;

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
          
          //
          // Removing empty chart container div's from the report area
          //
          var chartEls = $("#__BIRT_ROOT").find("div")
          for(var i=0; i<chartEls.length; i++){
        	  var thisChart = chartEls[i];
        	  var thisClass = $(thisChart).attr('class');
        	  if(thisClass && thisClass.indexOf("style_") !== -1){
        		  $(thisChart).hide();
        	  }
          }
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
        
        if(format === "rptdesign"){
        	var url = 'net.geoprism.report.ReportItemController.download.mojo?' + $.param({dashboardId:dashboardId});
        }
        else{
        	var url = 'net.geoprism.report.ReportItemController.run.mojo?' + $.param(params);
        }
                  
        window.location.href = url;    
      }
      else {
        var message = localizationService.localize("dashboard", "MissingReport");                    
          
        GDB.ExceptionHandler.handleException(message);        
      }
    }
    
    controller.getCompressedState = function() {
      var oState = controller.model;
      
      var state = {
        mapId : oState.mapId,
        types : []
      };
      
      if(!dashboardService.isEmptyFilter(oState.location)) {
        state.location = oState.location;
      }
      
      for(var i = 0; i < oState.types.length; i++) {
        var oType = oState.types[i];
        var oAttributes = oType.attributes;

        var attributes = [];
        
        if(oAttributes != null) {          
          for(var j = 0; j < oAttributes.length; j++) {
            var oAttribute = oAttributes[j];
            
            if(oAttribute.filter != null && !dashboardService.isEmptyFilter(oAttribute.filter)) {
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
    
    controller.addDashboard = function(dashboardId, label) {
      controller.dashboards.push({
        dashboardId : dashboardId,
        label : label
      });
      
      controller.setDashboardId(dashboardId);
    }
    
    controller.editOptions = function() {
      $scope.$emit('editDashboard', {dashboardId : controller.dashboardId, element : "#mapDivId"});
    }
    
    controller.getFilterMap = function() {
      var map = {};
        
      for(var i = 0; i < controller.model.types.length; i++) {
        var type = controller.model.types[i];
      
        map[type.id] = {};
          
        for(var j = 0; j < type.attributes.length; j++) {
          var attribute = type.attributes[j];
            
          map[type.id][attribute.mdAttributeId] = attribute.filter;
        }        
      }
      
      return map;
    }
    
    // Copies the map of filters into the given state
    controller.copyFilters = function(state) {
      state.location = controller.model.location;
    	
      var filters = controller.getFilterMap();      
        
      for(var i = 0; i < state.types.length; i++) {
        var type = state.types[i];
            
        for(var j = 0; j < type.attributes.length; j++) {
          var attribute = type.attributes[j];          
          
          if(filters[type.id] != null) {        	  
            var filter = filters[type.id][attribute.mdAttributeId];
            
            if(filter != null) {
              attribute.filter = filter;
            }
          }
        }        
      }
    }
    
    controller.refreshDashboard = function(state) {
      // Merge state to preserve all the applicable current filters
      controller.copyFilters(state);
    	
      // Clear the current dashboard state
      controller.clearDashboardState();
      
      // Load the new dashboard state and refresh the map
      controller.setDashboardState(state, false);
    }
    
    /*
     * Dashboard events
     */    
    $scope.$on('dashboardChange', function(event, data){
      controller.refreshDashboard(data.dashboard);
      
      event.stopPropagation();            
    });
    
    
    /*
     * Layer events
     */ 
    $scope.$on('editReferenceLayer', function(event, data) {
      data.mapId = controller.model.mapId;
      data.state = controller.getCompressedState();
    });    
    
    $scope.$on('newReferenceLayer', function(event, data) {
      data.mapId = controller.model.mapId;
      data.state = controller.getCompressedState();
    });
    
    $scope.$on('editThematicLayer', function(event, data) {
      data.mapId = controller.model.mapId;
      data.state = controller.getCompressedState();
    });    
    
    $scope.$on('newThematicLayer', function(event, data) {
      data.mapId = controller.model.mapId;
      data.state = controller.getCompressedState();
    });
    
    $scope.$on('toggleLayer', function(event, data) {
      controller.toggleLayer(data.layer);    
    });
    
    $scope.$on('exportLayerData', function(event, data) {
      controller.exportLayerData(data.layerId);
              
      event.stopPropagation();
    });
    
    $rootScope.$on('layerChange', function(event, data) {
      controller.handleLayerEvent(data.map);      
    });
          
    /*
     * Report Events
     */ 
    $scope.$on('exportReport', function(event, data) {
      controller.exportReport(data.format);
    });   
    
    /*
     * Map Events
     */    
    $scope.$on('exportMap', function(event, data) {
      controller.exportMap();
        
      event.stopPropagation();
    });    
      
    $scope.$on('centerMap', function(event, data) {
      controller.centerMap();
        
      event.stopPropagation();
    });
  }
  
  angular.module("dashboard", ["dashboard-service", "localization-service", "map-service", "report-panel", "dashboard-layer", "dashboard-map", "dashboard-panel", "dashboard-layer-form", "dashboard-builder", "data-uploader"]);
  angular.module("dashboard")
   .controller('DashboardController', DashboardController)
})();