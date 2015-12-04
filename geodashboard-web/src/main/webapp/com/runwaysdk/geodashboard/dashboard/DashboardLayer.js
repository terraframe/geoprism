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
	
  /**
   * 
   * MAP PANEL CONTROLLER AND WIDGET
   * 
   */
  function MapPanelController($scope, $timeout, jQueryService) {
    var controller = this;
    controller.expanded = true;
    
    controller.toggle = function() {
      var speed = 500;
        
      if(controller.expanded){
        jQueryService.animate("#control-form", {left: "-=236"}, speed, function(){
          controller.expanded = false;
          $scope.$apply();
        });
        
        jQueryService.animate(".ol-zoom.ol-unselectable.ol-control", {left: "-=236"}, speed);
      }
      else{
        jQueryService.animate("#control-form", {left: "+=236"}, speed, function(){
            controller.expanded = true;
            $scope.$apply();
        });        
        jQueryService.animate(".ol-zoom.ol-unselectable.ol-control", {left: "+=236"}, speed);
      }
    }
  }
  
  function MapPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/map-panel.jsp',
      scope: {
        dashboard:'='
      },
      controller : MapPanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }  


  /**
   * 
   * THEMATIC LAYER CONTROLLER AND WIDGET
   * 
   */
  function ThematicPanelController($scope, $compile, $timeout, dashboardService) {
    var controller = this;
    /* Getting the $compile method reference for use with later functions  */
    controller.$compile = $compile;
    controller.$scope = $scope;
    
    controller.getLayer = function(layerId) {
      return $scope.cache.values[layerId];
    }
    
    controller.toggle = function(layerId) {
      var layer = $scope.cache.values[layerId];      
      layer.isActive = !layer.isActive;

      $scope.dashboard.toggleLayer(layer);
    }
    
    controller.edit = function(layerId) {
      var form = new com.runwaysdk.geodashboard.gis.ThematicLayerForm($scope.dashboard, $scope.dashboard.model.mapId);
      form.edit(layerId, controller.$compile, controller.$scope);    	
    }
    
    controller.remove = function(layerId) {    	
      var onSuccess =  function(){
          
        if($scope.cache.values[layerId] != null) {
            
          // Remove the layer from the map
          var layer = $scope.cache.values[layerId];
          layer.isActive = false;
              
          $scope.dashboard.toggleLayer(layer);
              
          // Remove value from cache
          delete $scope.cache.values[layerId];        
          $scope.cache.ids.splice( $.inArray(layerId, $scope.cache.ids), 1 );
        }
            
        $scope.$apply();
      };
          
      dashboardService.removeLayer(layerId, '#overlay-container', onSuccess);  
    },
    
    controller.setLayerIndexes = function(layerIds) {
      $scope.cache.ids = layerIds;

      $scope.$apply();       	
      
      // // At this point, the cache are already ordered properly in the HTML. All we need to do is inform the map of the new ordering.
      $scope.dashboard.renderMap();                  
    }
    
    controller.canEdit = function() {
      return dashboardService.canEdit();    	
    }
    
    controller.move = function(e, ui) {
      var layerIds = [];
        
      var elements = $("#overlayLayerContainer").find(".row-form");
        
      for (var i = 0; i < elements.length; i++) {
        var layerId = $(elements[i]).data('id');
        
        layerIds.push(layerId);
      }  
      
      controller.orderLayers(layerIds);
    }
    
    controller.orderLayers = function(layerIds) {
    	
      if(dashboardService.edit){
        var mapId = $scope.dashboard.model.mapId;
        
        var onSuccess = function(json) {
          controller.setLayerIndexes(layerIds);
        };
          
        var onFailure = function(e) {
          $scope.$apply();
        };
          
        /* 
         * DashboardService.orderedLayers is expecting the ids to be passed
         * in the opposite order of the front end.
         */
        var reversed = layerIds.slice();
        reversed.reverse();
        
        dashboardService.orderLayers(mapId, reversed, '#overlay-container', onSuccess, onFailure);
      }
      else{
        controller.setLayerIndexes(layerIds);        
      }    	
    }
    
    controller.hasValues = function() {
      var length = $scope.cache.ids.length;
      
      return (length > 0);
    }
    
    controller.expand = function(element) {
      $timeout(function(){
        if(controller.hasValues() && !$(element).find("#collapse-overlay").hasClass("in")) {      
          $(element).find("#overlay-opener-button").click();            
        }        
      }, 1000);
    }    
  }
    
  function ThematicPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/thematic-layer-panel.jsp',
      scope: {
        cache:'=',
        dashboard:'='
      },
      controller : ThematicPanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
    	
        // open the overlay panel if there are cache and it is collapsed        
        scope.$watch('cache', function(newVal, oldVal){
          ctrl.expand(element);        
        }, true);
        
        /* Hook-up drag and drop */
        element.ready(function(){
          var el = $(element).find('.holder');
          
          el.sortable({
            update: ctrl.move
          });
          el.disableSelection();            	
        });
      }
    }    
  }

  /**
   * 
   * REFERENCE LAYER CONTROLLER AND WIDGET
   * 
   */  
  function ReferencePanelController($scope, $timeout, dashboardService) {
    var controller = this;
	  
    controller.canEdit = function() {
      return dashboardService.canEdit();      
    }
    
    controller.edit = function(layerId, universalId) {
      var form = new com.runwaysdk.geodashboard.gis.ReferenceLayerForm($scope.dashboard, $scope.dashboard.model.mapId);
      form.edit(layerId);    	
    }    
    
    controller.add = function(layerId, universalId) {
      var form = new com.runwaysdk.geodashboard.gis.ReferenceLayerForm($scope.dashboard, $scope.dashboard.model.mapId);
      form.open(universalId);
    }

    controller.toggle = function(layerId, universalId) {
      var layer = $scope.cache.values[universalId];      
      layer.isActive = !layer.isActive;

      $scope.dashboard.toggleLayer(layer);
    }
    
    controller.remove = function(layerId, universalId) {
      	
      var onSuccess = function(){
        // remove the map layer from the map
        var layer = $scope.cache.values[universalId];
        layer.isActive = false;
        layer.layerExists = false;
        layer.layerType = "REFERENCEJSON";
          
        $scope.dashboard.toggleLayer(layer);          
        	  
        $scope.$apply();
      };
      
      dashboardService.removeLayer(layerId, '#ref-layer-container', onSuccess);
    }
    
    controller.hasValues = function() {
      if($scope.cache != null && $scope.cache.ids.length > 0) {
        for(var i = 0; i < $scope.cache.ids.length; i++) {
          var id = $scope.cache.ids[i];
          var layer = $scope.cache.values[id];
          
          if(layer.layerExists) {
            return true;
          }
        }
      }  
      
      return false;
    }
  }
    
  function ReferencePanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/reference-layer-panel.jsp',
      scope: {
        cache:'=',
        dashboard:'='
      },
      controller : ReferencePanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {    
          
        // open the overlay panel if there are cache and it is collapsed        
        scope.$watch('cache', function(newVal, oldVal){
          element.ready(function() {
            if(!$(element).find("#collapse-ref-layer").hasClass("in") && ctrl.hasValues()) {
              $(element).find("#ref-layer-opener-button").click();            
            }            
          });
        }, true);
      }
    }    
  }  
  
  /**
   * 
   * BASE LAYER CONTROLLER AND WIDGET
   * 
   */
  function BasePanelController($scope, $timeout) {
    var controller = this;
    
    controller.toggle = function(layerId) {
      for(var i = 0; i < $scope.layers.length; i++) {
        var layer = $scope.layers[i];
        
        if(layer.layerId == layerId) {
          layer.isActive = !layer.isActive;          
        }
        else {
          layer.isActive = false;                    
        }
      }
      
      $scope.dashboard.refreshBaseLayer();
    }    
  }
  
  function BasePanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/base-layer-panel.jsp',
      scope: {
        layers:'=',
        dashboard:'='
      },
      controller : BasePanelController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {    
      }
    }    
  }    
  
  /**
   * 
   * LEGEND LAYER CONTROLLER AND WIDGET
   * 
   */
  function LegendController($scope, $timeout, dashboardService, mapService) {
    var controller = this;
    
    controller.getSrc = function(layer) {
      var params = {
        REQUEST:"GetLegendGraphic",
        VERSION:"1.0.0",        
        FORMAT:"image/png",        
        WIDTH:25,        
        HEIGHT:25,        
        TRANSPARENT:true,
        LEGEND_OPTIONS:"fontName:Arial;fontAntiAliasing:true;fontColor:0xececec;fontSize:11;fontStyle:bold;",      
        LAYER: mapService.getWorkspace() + ":" + layer.viewName
      };

      if(layer.showFeatureLabels){
        params.LEGEND_OPTIONS = params.LEGEND_OPTIONS + 'forceLabels:on;';
      }      
      
      var src = window.location.origin + '/geoserver/wms?' + $.param(params);
        
      return src;      
    }
    
    controller.detach = function($event, layer) {
    	
      layer.groupedInLegend = false;
      
      if(layer.legendXPosition == 0) {    	  
        layer.legendXPosition = $event.clientX + 50;
      }      
      
      if(layer.legendYPosition == 0) {
        layer.legendYPosition = $event.clientY - 50;        
      }   
      
      dashboardService.updateLegend(layer);        
    }
    
    controller.attach = function(layer) {
      layer.groupedInLegend = true;
      
      dashboardService.updateLegend(layer);        
    }
  }
  
  function LegendPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/legend-panel.jsp',
      scope: {
        thematicCache:'=',
        referenceCache:'='
      },
      controller : LegendController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }
  
  function FloatingLegends() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/floating-legends.jsp',
      scope: {
        thematicCache:'=',
        referenceCache:'='
      },
      controller : LegendController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }   
  
  function LegendDragController($scope, dashboardService) {
    var controller = this;

    controller.move = function(position) {      
      var layer = $scope.layer;
      
      var x = position.left;
      var y = position.top;
        
      layer.legendXPosition = x;        
      layer.legendYPosition = y;   
        
      dashboardService.updateLegend(layer);        
    }
  }
  
  function LegendDrag() {
    return {
      restrict:'A',
      scope: {
        layer: "="
      },
      controller : LegendDragController,
      controllerAs : 'ctrl',      
	  link: function(scope, element, attrs, ctrl) {
        element.ready(function(){
          $(element).draggable({
            containment: "body", 
            snap: true, 
            snap: ".legend-snapable", 
            snapMode: "outer", 
            snapTolerance: 5,
            stack: ".legend-container"
          });
          
          $(element).on('dragstop', function(e, ui){
            var target = e.currentTarget;
            var newPosition = $(target).position();
            
            ctrl.move(newPosition);
          }); 
        });
	  }
    }
  }
  
  angular.module("dashboard-layer", ["dashboard-service", "map-service", "jquery-service"]);
  angular.module('dashboard-layer')
    .directive('mapPanel', MapPanel)
    .directive('thematicPanel', ThematicPanel)
    .directive('referencePanel', ReferencePanel)
    .directive('basePanel', BasePanel)  
    .directive('floatingLegends', FloatingLegends)
    .directive('legendPanel', LegendPanel)
    .directive('legendDrag', LegendDrag);  
})();
