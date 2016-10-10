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
  function LocationController($scope, $timeout, locationService) {
    var controller = this;
    
    controller.init = function() {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(data) {
          $scope.previous.push(data.entity);
          
          controller.load(data);
        }      
      };      
      
      $scope.children = [];
      $scope.previous = [];
      
      locationService.select(connection, "", "", "");
    }
    
    controller.load = function(data) {
      $scope.children = data.children.resultSet;
      $scope.layers = data.layers;
        
      $scope.entity = data.entity;
      $scope.universal = {
        value : data.universal,
        options : data.universals
      };
      
      $scope.$broadcast('sharedGeoData', data);
    }
    
    controller.select = function(entity) {
      var connection = {
        elementId : '#innerFrameHtml',      
        onSuccess : function(data) {
          $scope.previous.push(entity);          
          
          controller.load(data);
        }
      };    

      locationService.select(connection, entity.id, "", $scope.layers);
    }
    
    controller.back = function(index) {
      if(index !== ($scope.previous.length - 1)) {
        var connection = {
          elementId : '#innerFrameHtml',        		
          onSuccess : function(data) {            
            $scope.previous.splice(index + 1);
            
            controller.load(data);
          }
        };    
                
        var id = $scope.previous[index].id;
                
        locationService.select(connection, id, "", $scope.layers);        
      }
    }
    
    controller.setUniversal = function() {
      var connection = {
        elementId : '#innerFrameHtml',
        onSuccess : function(data) {          
          $scope.children = data.children.resultSet;
          $scope.layers = data.layers;
            
          $scope.$broadcast('sharedGeoData', data);          
        }
      };
      
      console.log(JSON.stringify($scope.universal));

      locationService.select(connection, $scope.entity.id, $scope.universal.value, $scope.layers);      
    }

    controller.init();
  }
  
  angular.module("location-management", ["location-service", "editable-map"]);
  angular.module("location-management")
   .controller('LocationController', LocationController)
})();
