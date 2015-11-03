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
  
  function ThematicLayersController($scope, $timeout) {
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
  }
    
  function ThematicLayers() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: '/partial/dashboard/thematic-layers.jsp',
      scope: {
        cache:'=',
        edit:'=',
        dashboard:'='
      },
      controller : ThematicLayersController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {        
        // open the overlay panel if there are cache and it is collapsed        
        scope.$watch('cache', function(newVal, oldVal){
          var length = scope.cache.ids.length;
        
          element.ready(function() {
            if(length > 0  && !$(element).find("#collapse-overlay").hasClass("in")) {
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
    
  angular.module("dashboard-layer", []);
  angular.module('dashboard-layer')
    .directive('thematicLayers', ThematicLayers);  
})();