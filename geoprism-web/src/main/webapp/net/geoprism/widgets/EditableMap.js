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
  function EditableMapController($scope, localizationService, mapService, locationService, $attrs) {
	 var controller = this;
	  
	 $scope.enableEdits = $attrs.enableedits == "true"; // evaluate string to boolean... JavaScript is ridiculous
	 $scope.editWidgetEnabled = false;
	 $scope.renderBase = true;
	 $scope.baseLayers = [];
	 $scope.contextStyle = {fill:"rgba(255, 255, 255, 0.0)", strokeColor:"black", strokeWidth:3};
	 $scope.targetStyle = {fill:"rgba(255, 255, 255, 0.0)", strokeColor:"red", strokeWidth:2};
	 $scope.targetFeature = null;
	 $scope.sharedGeoData = {};
	 
	 
	 controller.init = function() {
		 
		$scope.baseLayers = mapService.createBaseLayers();
		 
		// enable the default base layer 
	    $scope.baseLayers[0].isActive = true;
		controller.refreshBaseLayer();
		
		controller.addVectorHoverEvents();
		controller.addVectorClickEvents();
	 }
	 
	 controller.enableEdits = function() {
		 mapService.enableEdits();
		 $scope.editWidgetEnabled = true;
	 }
	 
	 controller.disableEdits = function() {
		 mapService.disableEdits();
	 }
	 
	 controller.removeVectorData = function() {
		 mapService.removeAllVectorLayers();
	 }
	 
	 controller.getMapData = function(callback, data, workspace) {
		 mapService.getGeoJSONData(callback, data, workspace);
	 }
	 
	 controller.addVectorClickEvents = function() {
		 mapService.addVectorClickEvents();
	 }
	 
	 controller.addVectorHoverEvents = function() {
		 mapService.addVectorHoverEvents();
	 }
	 
	 controller.zoomToVectorDataExtent = function() {
		 mapService.zoomToVectorDataExtent();
	 }
	  
	 controller.addVectorLayer = function(layerGeoJSON, styleObj, stackingIndex) {
		 mapService.addVectorLayer(layerGeoJSON, styleObj, stackingIndex);
	 }
	  
	  
	 controller.refreshBaseLayer = function() {
	      if($scope.baseLayers.length > 0) {
	        for(var i = 0; i < $scope.baseLayers.length; i++) {
	          var layer = $scope.baseLayers[i];
	          
	          if(layer.isActive) {
	            mapService.showLayer(layer, 0);          
	          }
	          else {
	            mapService.hideLayer(layer);        
	          }
	        }
	      }
	  }
	  
      function isEmptyJSONObject(obj) {
	    for(var prop in obj) {
	        if(obj.hasOwnProperty(prop))
	            return false;
	    }

	    return true && JSON.stringify(obj) === JSON.stringify({});
	  }
      
      $scope.$watch("targetFeature", function(newVal, oldVal) {
    	  $scope.$emit('locationEdit', {
	        wkt : newVal,
	        universal : {
	            value : $scope.sharedGeoData.universal,
	            options : $scope.sharedGeoData.universals
	        },
	        parent : $scope.sharedGeoData.entity
	      });
      });
      
      $scope.$on("locationChange", function(event, data){
    	  console.log(data)
      })
      
      // Recieve shared data from LocationManagement controller based on user selection of target location
      $scope.$on('sharedGeoData', function(event, data) {
    	  if(!isEmptyJSONObject(data)){
    		  
    		  $scope.sharedGeoData = data;
    		  
    		  var contextCallback = function(data) {
    			  for(var i=0; i<data.features.length; i++){
      	    		var feature = data.features[i];
      	    		feature.properties.isHoverable = false;
      	    		feature.properties.isClickable = false;
      			  }
    			  
    			  controller.addVectorLayer(data, $scope.contextStyle, 1);
        		  controller.zoomToVectorDataExtent();
    		  }
    		  
    		  var targetCallback = function(data) {
    			  for(var i=0; i<data.features.length; i++){
    	    		var feature = data.features[i];
    	    		feature.properties.isHoverable = true;
    	    		feature.properties.isClickable = true;
    			  }
    			  
    			  controller.addVectorLayer(data, $scope.targetStyle, 2);
    	    	  controller.zoomToVectorDataExtent();
    		  }
    		  
    		  controller.removeVectorData();
    		  
    		  // get context geo data
    		  controller.getMapData(contextCallback, data.layers[0], data.workspace);
    		  
    		  // get target geo data
    		  controller.getMapData(targetCallback, data.layers[1], data.workspace);
    		  
    		  if($scope.enableEdits && data.layers.length > 1 && data.layers[1].layerType === "POINT" && data.layers[0].layerType === "POLYGON"){
    			controller.enableEdits();
    		  }
    		  else if($scope.editWidgetEnabled){
    			controller.disableEdits();
    			$scope.editWidgetEnabled = false;
    		  }
    	  }
      });
      
      controller.init();
  }
  
  
  function EditableMap() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/widgets/editable-map.jsp',
      scope: true,
      controller : EditableMapController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  function EditableMapPopupController($scope, localizationService, mapService) {
	  var controller = this;
  }
  
  
  function EditableMapPopup() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/widgets/editable-map-popup.jsp',
      scope: {
        feature:'='
      },
      controller : EditableMapPopupController,
      controllerAs : 'ctrl',
      link: function (scope, element, attrs, ctrl) {
      }
    }    
  }
  
  
  angular.module("editable-map", ["styled-inputs", "localization-service", "map-service", "location-service"]);
  angular.module("editable-map")
   .controller('EditableMapController', EditableMapController)
   .directive('editableMap', EditableMap)
   .directive("editableMapPopup", EditableMapPopup)
})();
