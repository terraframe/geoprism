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
  function SimpleMapController($scope, $rootScope, localizationService, mapService, locationService) {
	 var controller = this;
	  
	 $scope.renderBase = true;
	 $scope.baseLayers = [];
	 $scope.contextStyle = {fill:"rgba(0, 0, 0, 0.25)", strokeColor:"rgba(0, 0, 0, 0.5)", strokeWidth:5, radius:7};
	 $scope.targetStyle = {fill:"rgba(255, 0, 0, 0.1)", strokeColor:"rgba(255, 0, 0, 0.5)", strokeWidth:3, radius:7};
	 $scope.sharedGeoData = {};
	 
	 
	 controller.init = function() {
		 
		$scope.baseLayers = mapService.createBaseLayers();
		
		// Override default basemap from MapConfig with property passed from directive HTML
		// Any option passed in must also be included in MapConfig or it will not be available to add. 
		if($scope.baseMapType){
			$scope.baseLayers.forEach(function(lyr){
				if(lyr.layerType.toUpperCase() === $scope.baseMapType.toUpperCase()){
					lyr.isActive = true;
				}
				else{
					lyr.isActive = false;
				}
			});
		}
		 
		controller.refreshBaseLayer();
		
		var hoverCallback = function(featureId){
	        $scope.$emit('hoverChange', {
	              id : featureId
	        });
	        $scope.$apply();
		}
		
		var featureClickCallback = function(feature, map){
			$scope.$emit('locationFocus', {
	              id : feature.getProperties().id
	        });
	        $scope.$apply();
		}
        
		controller.addVectorHoverEvents(hoverCallback);
		controller.addVectorClickEvents(featureClickCallback);
	 }
	 
	 controller.removeVectorData = function() {
		 mapService.removeAllVectorLayers();
	 }
	 
	 controller.getMapData = function(callback, data, workspace) {
		 mapService.getGeoJSONData(callback, data, workspace);
	 }
	 
	 controller.addVectorClickEvents = function(featureClickCallback) {
		 mapService.addVectorClickEvents(featureClickCallback);
	 }
	 
	 controller.addVectorHoverEvents = function(hoverCallback) {
		 mapService.addVectorHoverEvents(hoverCallback);
	 }
	 
	 controller.zoomToVectorDataExtent = function() {
		 mapService.zoomToVectorDataExtent();
	 }
	 
	 controller.zoomToExtentOfFeatures = function(featureGeoIds) {
		 mapService.zoomToExtentOfFeatures(featureGeoIds);
	 }
	 
	 controller.focusOnFeature = function(feature) {
		 mapService.focusOnFeature(feature);
	 }
	 
	 controller.focusOffFeature = function(feature) {
		 mapService.focusOffFeature(feature);
	 }
	  
	 controller.addVectorLayer = function(layerGeoJSON, styleObj, type, stackingIndex) {
		 mapService.addVectorLayer(layerGeoJSON, styleObj, type, stackingIndex);
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
	 
	 controller.refreshInteractiveLayers = function(triggeringEvent) {
		  if(!isEmptyJSONObject($scope.sharedGeoData)){
			  var data = $scope.sharedGeoData;
			
			  var targetCallback = function(data) {
				  for(var i=0; i<data.features.length; i++){
		    		var feature = data.features[i];
		    		feature.properties.isHoverable = true;
		    		feature.properties.isClickable = true;
				  }
				  
				  controller.addVectorLayer(data, $scope.targetStyle, "TARGET", 2);
		    	  controller.zoomToVectorDataExtent();
			  }
			  
			  
			  controller.removeVectorData();
			  
			  data.layers.forEach(function(layer){
				  controller.getMapData(targetCallback, layer, layer.workspace);
			  });
		  }
	 }
	 
	 
	 controller.refreshWithContextLayer = function(triggeringEvent) {
		  if(!isEmptyJSONObject($scope.sharedGeoData)){
			  var data = $scope.sharedGeoData;
			
			  var contextCallback = function(data) {
				  for(var i=0; i<data.features.length; i++){
	  	    		var feature = data.features[i];
	  	    		feature.properties.isHoverable = false;
	  	    		feature.properties.isClickable = false;
	  			  }
				  
				  controller.addVectorLayer(data, $scope.contextStyle, "CONTEXT", 1);
	    		  controller.zoomToVectorDataExtent();
			  }
			  
			  var targetCallback = function(data) {
				  for(var i=0; i<data.features.length; i++){
		    		var feature = data.features[i];
		    		feature.properties.isHoverable = true;
		    		feature.properties.isClickable = true;
				  }
				  
				  controller.addVectorLayer(data, $scope.targetStyle, "TARGET", 2);
		    	  controller.zoomToVectorDataExtent();
			  }
			  
			  
			  controller.removeVectorData();
			  
			  // get context geo data
			  controller.getMapData(contextCallback, data.layers[0], data.workspace);
			  
			  // get target geo data
			  controller.getMapData(targetCallback, data.layers[1], data.workspace);
		  }
	 }
	 
      function isEmptyJSONObject(obj) {
	    for(var prop in obj) {
	        if(obj.hasOwnProperty(prop))
	            return false;
	    }

	    return true && JSON.stringify(obj) === JSON.stringify({});
	  }
      
      
      
      $scope.$on('listHoverOver', function(event, data){
    	  controller.focusOnFeature(data);
      });
      
      $scope.$on('listHoverOff', function(event, data){
    	  controller.focusOffFeature(data);
      });
      
      $scope.$on('listItemClick', function(event, data){
    	  controller.zoomToExtentOfFeatures(data.geoIds)
      });
      
      
      // Recieve shared data from parent controller based on user selection of target location
      $scope.$on('sharedGeoData', function(event, data) {
    	  if(!isEmptyJSONObject(data)){
    		  
    		  $scope.sharedGeoData = data;
    		  
    		  controller.refreshWithContextLayer('sharedGeoData');
    	  }
    	  else if(!isEmptyJSONObject($scope.sharedGeoData)) {
    		  controller.refreshWithContextLayer('sharedGeoData');
    	  }
      });
      
      
      $scope.$on("layerChange", function(event, data){
    	  $scope.sharedGeoData = data;
    	  
    	  if($scope.includeContextLayer){
    		  controller.refreshWithContextLayer('layerChange');
    	  }
    	  else{
    		  controller.refreshInteractiveLayers('layerChange');
    	  }
      })

      
      controller.init();
  }
  
  
  function SimpleMap() {
    return {
      restrict: 'E',
      replace: true,
      templateUrl: com.runwaysdk.__applicationContextPath + '/partial/widgets/simple-map.jsp',
      scope: {
    	  includeContextLayer:'=includeContextLayer',
    	  baseMapType:"@baseMapType"
      },
      controller : SimpleMapController,
      controllerAs : 'ctrl',      
      link: function (scope, element, attrs, ctrl) {
      }
    }   
  }
  
  
  
  angular.module("simple-map", ["styled-inputs", "localization-service", "map-service", "location-service"]);
  angular.module("simple-map")
   .controller('simpleMapController', SimpleMapController)
   .directive('simpleMap', SimpleMap)
})();
