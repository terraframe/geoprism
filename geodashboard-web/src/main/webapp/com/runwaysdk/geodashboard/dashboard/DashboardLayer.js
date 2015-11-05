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
   * THEMATIC LAYER CONTROLLER AND WIDGET
   * 
   */
  function ThematicPanelController($scope, $timeout) {
    var controller = this;
    
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
      form.edit(layerId);    	
    }
    
    controller.remove = function(layerId) {
    	
      var request = new GDB.StandbyClientRequest({
        onSuccess : function(){
        	
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
        },
        onFailure : function(e){
          GDB.ExceptionHandler.handleException(e);
        }
      }, '#overlay-container');
    	
      com.runwaysdk.Facade.deleteEntity(request, layerId);      
    },
    
    controller.setLayerIndexes = function(layerIds) {
      $scope.cache.ids = layerIds;

      $scope.$apply();       	
      
      // // At this point, the cache are already ordered properly in the HTML. All we need to do is inform the map of the new ordering.
      $scope.dashboard.renderMap();                  
    }
    
    controller.canEdit = function() {
      return $scope.dashboard.canEdit();    	
    }
    
    controller.move = function(e, ui) {
      var layerIds = [];
        
      var elements = $("#overlayLayerContainer").find(".row-form");
        
      for (var i = 0; i < elements.length; i++) {
        var layerId = $(elements[i]).data('id');
        
        layerIds.push(layerId);
      }      
    	
      if($scope.edit){
        var request = new GDB.StandbyClientRequest({
          onSuccess : function(json) {
            controller.setLayerIndexes(layerIds);
          },
          onFailure : function(e) {
            GDB.ExceptionHandler.handleException(e);
              
            $scope.$apply();
          }
        }, '#overlay-container');
      
        com.runwaysdk.geodashboard.gis.persist.DashboardMap.orderLayers(request, $scope.dashboard.model.mapId, layerIds);
      }
      else{
        controller.setLayerIndexes(layerIds);    	  
      }
    }
    
    controller.hasValues = function(cache) {
      var length = cache.ids.length;
      
      return (length > 0);
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
        
          element.ready(function() {
            if(ctrl.hasValues(scope.cache) && !$(element).find("#collapse-overlay").hasClass("in")) {
              $(element).find("#overlay-opener-button").click();            
            }            
          });
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
  function ReferencePanelController($scope, $timeout) {
    var controller = this;
	  
    controller.canEdit = function() {
      return $scope.dashboard.canEdit();      
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
    	
      var request = new GDB.StandbyClientRequest({
        onSuccess : function(){
          // remove the map layer from the map
          var layer = $scope.cache.values[universalId];
          layer.isActive = false;
          layer.layerExists = false;
          layer.layerType = "REFERENCEJSON";
          
          $scope.dashboard.toggleLayer(layer);          
        	  
          $scope.$apply();
        },
        onFailure : function(e){
          GDB.ExceptionHandler.handleException(e);
        }
      }, '#ref-layer-container');
      	
      com.runwaysdk.Facade.deleteEntity(request, layerId);      
    }
    
    controller.hasValues = function(cache) {
      if(cache.ids.length > 0) {
        for(var i = 0; i < cache.ids.length; i++) {
          var id = cache.ids[i];
          var layer = cache.values[id];
          
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
            if(!$(element).find("#collapse-ref-layer").hasClass("in") && ctrl.hasValues(scope.cache)) {
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
  function LegendController($scope, $timeout) {
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
        LAYER: $scope.workspace + ":" + layer.viewName
      };

      if(layer.showFeatureLabels){
        params.LEGEND_OPTIONS = params.LEGEND_OPTIONS + 'forceLabels:on;';
      }      
      
      var src = window.location.origin + '/geoserver/wms?' + $.param(params);
        
      return src;      
    }
    
    controller.detach = function(layer) {
      layer.groupedInLegend = false;
      
      if(layer.legendXPosition == 0) {
        layer.legendXPosition = 50;
      }      
      
      if(layer.legendYPosition == 0) {
        layer.legendYPosition = 50;        
      }   
      
      controller.persist(layer);
    }
    
    controller.attach = function(layer) {
      layer.groupedInLegend = true;
      
      controller.persist(layer);
    }

    controller.move = function(e,ui) {
      var layer = $scope.layer;
    	
      var target = e.currentTarget;
      var newPosition = $(target).position();
      var x = newPosition.left;
      var y = newPosition.top;
      
      layer.legendXPosition = x;        
      layer.legendYPosition = y;   
      
      controller.persist(layer);
    }
    
    controller.persist = function(layer) {
//      if(this.canEditDashboards()){
      var request = new Mojo.ClientRequest({
        onSuccess : function() {
          // No action needed
        },
        onFailure : function(e) {
          GDB.ExceptionHandler.handleException(e);
        }
      });
            
      com.runwaysdk.geodashboard.gis.persist.DashboardLayer.updateLegend(request, layer.layerId, layer.legendXPosition, layer.legendYPosition, layer.groupedInLegend);
//          }
    }
  }
  
  function LegendPanel() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/legend-panel.jsp',
      scope: {
        thematicCache:'=',
        referenceCache:'=',
        workspace:'='
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
        referenceCache:'=',
        workspace:'='
      },
      controller : LegendController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }   
  
  function LegendDrag() {
    return {
      restrict:'A',
      scope: {
        layer: "=",
      },
      controller : LegendController,
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
          
          $(element).on('dragstop', ctrl.move); 
        });
	  }
    }
  }
  
  angular.module("dashboard-layer", []);
  angular.module('dashboard-layer')
    .directive('thematicPanel', ThematicPanel)
    .directive('referencePanel', ReferencePanel)
    .directive('basePanel', BasePanel)  
    .directive('floatingLegends', FloatingLegends)
    .directive('legendPanel', LegendPanel)
    .directive('legendDrag', LegendDrag);  
})();